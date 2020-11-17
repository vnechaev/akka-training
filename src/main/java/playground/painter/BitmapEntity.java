package playground.painter;

import akka.persistence.AbstractPersistentActor;
import akka.persistence.SnapshotOffer;

public class BitmapEntity extends AbstractPersistentActor {
    private final String bucket;
    private final int width;
    private final int height;
    private final int SNAPSHOT_EVERY = 500;
    private int events = 0;
    //
    private BitmapState state;

    //
    public BitmapEntity(String bucket, int width, int height) {
        this.bucket = bucket;
        this.width = width;
        this.height = height;
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        try {
            String id = persistenceId();
            int s = id.indexOf("_");
            int x = id.indexOf("x");
            int baseX = Integer.parseInt(id.substring(s + 1, x));
            int baseY = Integer.parseInt(id.substring(x + 1));
            this.state = new BitmapState(baseX, baseY, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSnapshot(SnapshotOffer offer) {
        state = (BitmapState) offer.snapshot();
    }

    @Override
    public String persistenceId() {
        return context().self().path().name();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Protocol.Paint.class, this::onPaintCommand)
                .match(Protocol.Show.class, this::onShowCommand)
                .build();
    }

    @Override
    public Receive createReceiveRecover() {
        return receiveBuilder()
                .match(Events.Painted.class, this::reactOnPaintedEvent)
                .match(SnapshotOffer.class, this::onSnapshot)
                .build();
    }

    private void onPaintCommand(Protocol.Paint cmd) {
        try {
            //Validation
            state.validatePaint(cmd.getX(), cmd.getY(), cmd.getC());
            //No exception? Create valid event!
            Events.Painted unsavedEvent = new Events.Painted(cmd.getX(), cmd.getY(), cmd.getC());
            //Save the event
            persist(unsavedEvent, savedEvent -> {
                //If we reached here, event has been saved.
                //Run event reaction without side effects
                reactOnPaintedEvent(savedEvent);
                //Perform side effects
                getSender().tell(new Response.OK(), getSelf());

                events++;
                if (events % SNAPSHOT_EVERY == 0) {
                    saveSnapshot(state);
                }
            });
        } catch (BitmapException e) {
            //Validation failed
            getSender().tell(new Response.ERROR(e.getMessage()), getSelf());
        }
    }

    private void onShowCommand(Protocol.Show cmd) {
        try {
            int[][] show = state.show(cmd.getX(), cmd.getY(), cmd.getW(), cmd.getH());
            getSender().tell(new Response.SHOW(show), getSelf());
        } catch (BitmapException e) {
            getSender().tell(new Response.ERROR(e.getMessage()), getSelf());
        }
    }

    private void reactOnPaintedEvent(Events.Painted evt) {
        try {
            state.paint(evt.getX(), evt.getY(), evt.getC());
        } catch (BitmapException e) {
            getContext().getSystem().log().error("This should not happen during event processing: " + e);
        }
    }
}
