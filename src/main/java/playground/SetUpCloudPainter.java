package playground;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.japi.Creator;
import playground.painter.BitmapEntity;
import playground.painter.CanvasSetup;
import support.PainterHandlerImpl;
import support.web.RequestHandlerFactory;

public class SetUpCloudPainter {
    private static final CanvasSetup SETUP = new CanvasSetup(10, 200, 200);
    private static final String BUCKET = "cloud";

    public static RequestHandlerFactory setup(ActorSystem system) {
        ActorRef root = system.deadLetters();
        return connectionActorContext -> new PainterHandlerImpl(root, connectionActorContext);
    }

    static class BitmapEntityCreator implements Creator<BitmapEntity> {
        private final CanvasSetup setup;

        public BitmapEntityCreator(CanvasSetup setup) {
            this.setup = setup;
        }

        @Override
        public BitmapEntity create() {
            return new BitmapEntity(BUCKET, setup.getTileWidth(), setup.getTileHeight());
        }
    }
}
