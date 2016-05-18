package shipz.util;

import java.util.EventObject;

/**
 * Created by Paul on 06.05.2016.
 */
public class GameEvent extends EventObject {
    private byte _id;

    public GameEvent(Object source, byte eventId) {
        super(source);
        _id = eventId;
    }

    public byte getId() {
        return _id;
    }
}
