package support.web;

public interface RequestHandler {
    public void handleRequest(RequestMessage request);

    public void handleEvent(Object event);
}
