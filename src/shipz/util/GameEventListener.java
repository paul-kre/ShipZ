package shipz.util;

import java.util.EventObject;

/**
 * Created by Paul on 05.05.2016.
 */
public interface GameEventListener extends EventIds {
    public void eventReceived(GameEvent e);
}
