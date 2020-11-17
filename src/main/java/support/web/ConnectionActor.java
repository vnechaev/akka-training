package support.web;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;

public class ConnectionActor extends AbstractActorWithStash {

    private final RequestHandlerFactory handlerFactory;

    private ActorRef writeTo;
    private RequestHandler handler;

    public ConnectionActor(RequestHandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    @Override
    public Receive createReceive() {
        return createWaitingBehavior();
    }

    private Receive createWaitingBehavior() {
        return receiveBuilder().create().
                match(ActorRef.class, ref -> {
                    writeTo = ref;
                    handler = handlerFactory.create(getContext());
                    getContext().become(createNormalBehavior(), true);
                }).
                matchAny(msg -> stash()).
                build();
    }

    private Receive createNormalBehavior() {
        return receiveBuilder().
                match(String.class, message -> handler.handleRequest(RequestMessage.parse(message))).
                match(ResponseMessage.class, request -> writeTo.tell(request, null)).
                matchAny(event -> handler.handleEvent(event)).
                build();
    }
}
