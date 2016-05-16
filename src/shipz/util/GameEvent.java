package shipz.util;

import java.util.EventObject;

/**
 * Created by Paul on 06.05.2016.
 */
public class GameEvent extends EventObject {
    private int _evtId;

    public GameEvent(Object source, int evtId) {
        super(source);
        _evtId = evtId;
    }
}
