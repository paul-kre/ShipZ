package shipz.util;

/**
 * Created by Paul on 06.05.2016.
 */
public abstract class GameEventSource {
    private GameEventListener _listener;

    public void setEventListener(GameEventListener listener) {
        _listener = listener;
    }

    protected void fireShootEvent(int evtIndex) {
        GameEvent evt = new GameEvent(this, evtIndex);
        _listener.eventReceived(evt);
    }
}
