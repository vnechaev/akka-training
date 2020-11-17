package support;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import playground.painter.PixelPaintedEvent;
import playground.painter.Protocol;
import playground.painter.Response;
import support.PublishSubscribeMagic;
import support.web.RequestHandler;
import support.web.RequestMessage;
import support.web.ResponseMessage;

import java.time.Duration;

public class PainterHandlerImpl implements RequestHandler {
    private final ActorRef painterActor;
    private final ActorRef connectionActor;
    private final ObjectMapper mapper = new ObjectMapper();

    public PainterHandlerImpl(ActorRef painterActor, ActorContext context) {
        this.painterActor = painterActor;
        this.connectionActor = context.self();
        PublishSubscribeMagic.subscribe(context, PixelPaintedEvent.class);
    }

    @Override
    public void handleRequest(RequestMessage message) {
        String[] data = message.getData();
        switch (message.getCommand()) {
            case "paint":
                if (data != null && data.length < 3)
                    connectionActor.tell(ResponseMessage.error("3 parameters"), null);
                else {
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    int c = Integer.parseInt(data[2]);
                    Patterns.ask(painterActor, new Protocol.Paint(x, y, c), Duration.ofSeconds(5))
                            .thenApply(v -> (Response) v)
                            .thenAccept(r -> {
                                if (r.getResponse() == Response.Status.OK) {
                                    connectionActor.tell(ResponseMessage.reply(""), null);
                                } else
                                    connectionActor.tell(ResponseMessage.error(""), null);
                            });
                }
                break;
            case "show":
                if (data != null && data.length < 4)
                    connectionActor.tell(ResponseMessage.error("4 parameters"), null);
                else {
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    int w = Integer.parseInt(data[2]);
                    int h = Integer.parseInt(data[3]);
                    Patterns.ask(painterActor, new Protocol.Show(x, y, w, h), Duration.ofSeconds(5))
                            .thenApply(v -> (Response) v)
                            .thenAccept(r -> {
                                if (r.getResponse() == Response.Status.OK) {
                                    Response.SHOW res = (Response.SHOW) r;
                                    try {
                                        String v = mapper.writeValueAsString(res.getBitmap());
                                        connectionActor.tell(ResponseMessage.reply("show " + x + " " + y + " " + v), null);
                                    } catch (JsonProcessingException e) {
                                        connectionActor.tell(ResponseMessage.error(e.getMessage()), null);
                                    }
                                } else
                                    connectionActor.tell(ResponseMessage.error(""), null);
                            }).exceptionally(e -> {
                        e.printStackTrace();
                        return null;
                    });
                }
                break;
            default:
                connectionActor.tell(ResponseMessage.error("wrong command"), null);
                break;
        }
    }

    @Override
    public void handleEvent(Object event) {
        if (event instanceof PixelPaintedEvent) {
            PixelPaintedEvent evt = (PixelPaintedEvent) event;
            connectionActor.tell(ResponseMessage.reply("pixel " + evt.getBucket() + " " + evt.getX() + " " + evt.getY() + " " + evt.getC()), null);
        }
    }
}