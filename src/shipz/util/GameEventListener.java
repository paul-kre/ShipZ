package shipz.util;

/**
 * Created by Paul on 05.05.2016.
 */
public interface GameEventListener extends Event {
    public void eventReceived(GameEvent e);
}
