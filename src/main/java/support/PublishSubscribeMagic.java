package support;

import akka.actor.ActorContext;

public class PublishSubscribeMagic {
    private static final String TOPIC = "painter";
    private static final boolean USE_CLUSTER = false;

    public static <T> void subscribe(ActorContext context, Class<T> eventClass) {
        if (USE_CLUSTER) {
//            ActorRef mediator = DistributedPubSub.get(context.system()).mediator();
//            mediator.tell(new DistributedPubSubMediator.Subscribe(TOPIC, context.self()), context.self());
        } else
            context.system().getEventStream().subscribe(context.self(), eventClass);
    }

    public static <T> void publish(ActorContext context, T message) {
        if (USE_CLUSTER) {
//            ActorRef mediator = DistributedPubSub.get(context.system()).mediator();
//            mediator.tell(new DistributedPubSubMediator.Publish(TOPIC, message), context.self());
        } else
            context.system().getEventStream().publish(message);
    }
}
