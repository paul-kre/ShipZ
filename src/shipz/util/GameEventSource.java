package shipz.util;

import java.util.EventObject;

/**
 * Created by Paul on 06.05.2016.
 */
public class GameEventSource {
    private GameEventListener _listener;

    public void setEventListener(GameEventListener listener) {
        _listener = listener;
    }

    protected void fireShootEvent(int x, int y) {
        ShootEvent evt = new ShootEvent(this, x, y);
        _listener.onShoot(evt);
    }

    protected void fireSurrenderEvent() {
        EventObject evt = new EventObject(this);
        _listener.onSurrender(evt);
    }

    protected void fireDisconnectEvent() {
        EventObject evt = new EventObject(this);
        _listener.onDisconnect(evt);
    }
}
