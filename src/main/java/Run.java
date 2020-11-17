import akka.actor.ActorSystem;
import playground.SetUpAccount;
import playground.SetUpCloudPainter;
import playground.SetUpPainter;
import support.web.RequestHandlerFactory;
import support.web.WebServer;

import java.util.HashMap;
import java.util.Map;

public class Run {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("labs");

        Map<String, RequestHandlerFactory> factories = new HashMap<>();
        factories.put("painter", SetUpPainter.setup(system));
        factories.put("cloudpainter", SetUpCloudPainter.setup(system));
        factories.put("account", SetUpAccount.setup(system));

        WebServer server = new WebServer(system, "0.0.0.0", 9000, factories);
        server.start();
    }
}
