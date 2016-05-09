package shipz.util;

import java.util.EventObject;

/**
 * Created by Paul on 05.05.2016.
 */
public interface GameEventListener {
    public void onShoot(ShootEvent e);
    public void onSurrender(EventObject e);

    public void onClose(EventObject e);
    public void onDisconnect(EventObject e);

    public void onMessage(MessageEvent e);
}
