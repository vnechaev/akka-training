package playground;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import playground.accounts.AccountEntity;
import support.AccountHandlerImpl;
import support.web.RequestHandlerFactory;

public class SetUpAccount {
    public static RequestHandlerFactory setup(ActorSystem system) {
        ActorRef accountsActor = system.actorOf(Props.create(AccountEntity.class), "account1");
        ActorRef transfersActor = system.deadLetters();
        return connectionActorContext -> new AccountHandlerImpl(accountsActor, transfersActor, connectionActorContext.self());
    }
}
