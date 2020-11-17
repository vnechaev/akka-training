package playground;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import playground.painter.BitmapEntity;
import support.PainterHandlerImpl;
import support.web.RequestHandlerFactory;

public class SetUpPainter {
    public static RequestHandlerFactory setup(ActorSystem system) {
        ActorRef painterActor = system.actorOf(Props.create(BitmapEntity.class, () ->
                new BitmapEntity("single", 600, 600)
        ), "single_0x0");

        return connectionActorContext -> new PainterHandlerImpl(painterActor, connectionActorContext);
    }
}
