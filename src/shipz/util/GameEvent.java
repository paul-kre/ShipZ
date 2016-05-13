package shipz.util;

import java.util.EventObject;

/**
 * Created by Paul on 06.05.2016.
 */
public class GameEvent extends EventObject {
    private int _evtIndex;

    public GameEvent(Object source, int evtIndex) {
        super(source);
        _evtIndex = evtIndex;
    }
}
