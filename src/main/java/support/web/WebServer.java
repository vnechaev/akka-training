package support.web;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.ServerBuilder;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.model.ws.TextMessage;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.japi.JavaPartialFunction;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class WebServer extends AllDirectives {
    //
    private final String host;
    private final int port;
    private final Map<String, RequestHandlerFactory> handlerFactories;
    //
    private final ActorSystem system;
    private Http http;
    private CompletionStage<ServerBinding> serverBindingFuture;

    public WebServer(ActorSystem system, String host, int port,
                     Map<String, RequestHandlerFactory> handlerFactories) {
        this.system = system;
        this.host = host;
        this.port = port;
        this.handlerFactories = handlerFactories;
    }

    public void start() {
        http = Http.get(system);
        ServerBuilder serverBuilder = http.newServerAt(host, port);
        serverBindingFuture = serverBuilder.bindFlow(
                createRoute().flow(system)
        );
        system.log().info("Server started at port " + port);
    }

    public Route createRoute() {
        return get(
                () -> concat(
                        pathSingleSlash(() -> getFromResource("index.html")),
                        pathPrefix("page", () ->
                                path(PathMatchers.segment(), s -> getFromResource(s + ".html"))
                        ),
                        pathPrefix("ws", () ->
                                path(PathMatchers.segment(),
                                        s -> handlerFactories.containsKey(s) ?
                                                handleWebSocketMessages(connectionFlow(s)) :
                                                complete(StatusCodes.NOT_FOUND)
                                )
                        )
                )).seal();
    }

    public Flow<Message, Message, NotUsed> connectionFlow(String s) {
        final ActorRef actorRef = system.actorOf(Props.create(ConnectionActor.class, () -> new ConnectionActor(handlerFactories.get(s))));
        Sink<Message, NotUsed> incoming = Flow.
                <Message>create().
                <TextMessage>collect(new JavaPartialFunction<Message, TextMessage>() {
                    @Override
                    public TextMessage apply(Message msg, boolean isCheck) {
                        if (isCheck)
                            if (msg.isText()) return null;
                            else throw noMatch();
                        else return msg.asTextMessage();
                    }
                }).
                flatMapConcat(data -> data.getStreamedText()
                        .fold(new StringBuilder(), StringBuilder::append)
                        .map(StringBuilder::toString)).
                to(Sink.actorRef(actorRef, ResponseMessage.CLOSE));
        Source<Message, NotUsed> outgoing = Source
                .<ResponseMessage>actorRef(
                        (a) -> Optional.empty(), (a) -> Optional.empty(),
                        1000, OverflowStrategy.fail())
                .mapMaterializedValue(ref -> {
                    actorRef.tell(ref, null);
                    return NotUsed.getInstance();
                })
                .map(responseMessage -> TextMessage.create(responseMessage.toMessageString()));
        return Flow.fromSinkAndSourceCoupled(incoming, outgoing);
    }
}
