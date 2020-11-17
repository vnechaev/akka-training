package playground.transfers;

import akka.persistence.AbstractPersistentActor;


public class TransferEntity extends AbstractPersistentActor {
    public String persistenceId() {
        return context().self().path().name();
    }

    public Receive createReceive() {
        return receiveBuilder().
                matchAny(this::whenEmpty).
                build();
        //TODO
    }

    public Receive createReceiveRecover() {
        return receiveBuilder()
                .build();
    }

    public void whenEmpty(Object command) {
        sender().tell(new TransferResponse.ERROR(null, "Not initialized"), self());
    }
}
