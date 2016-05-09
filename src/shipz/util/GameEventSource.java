package shipz.util;

import java.util.EventObject;

/**
 * Created by Paul on 06.05.2016.
 */
public abstract class GameEventSource {
    private GameEventListener _listener;

    public void setEventListener(GameEventListener listener) {
        _listener = listener;
    }

    protected void fireShootEvent(int x, int y) {
        ShootEvent evt = new ShootEvent(this, x, y);
        _listener.onShoot(evt);
    }

    protected void fireCloseEvent() {
        EventObject evt = new EventObject(this);
        _listener.onClose(evt);
    }

    protected void fireSurrenderEvent() {
        EventObject evt = new EventObject(this);
        _listener.onSurrender(evt);
    }

    protected void fireDisconnectEvent() {
        EventObject evt = new EventObject(this);
        _listener.onDisconnect(evt);
    }

    protected void fireMessageEvent(String msg) {
        MessageEvent evt = new MessageEvent(this, msg);
        _listener.onMessage(evt);
    }


    //public abstract void shotHappened();
}
