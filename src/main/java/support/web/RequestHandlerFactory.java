package support.web;


import akka.actor.ActorContext;

public interface RequestHandlerFactory {
    RequestHandler create(ActorContext context);
}
