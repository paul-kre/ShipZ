package shipz.util;

import java.util.EventObject;

/**
 * Created by Paul on 06.05.2016.
 */
public class GameEvent extends EventObject {
    private int _id;

    public GameEvent(Object source, int eventId) {
        super(source);
        _id = eventId;
    }

    public int getId() {
        return _id;
    }
}
