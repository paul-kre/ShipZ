package shipz.util;

import java.util.EventObject;

/**
 * Created by Paul on 06.05.2016.
 */
public class ShootEvent extends EventObject {
    private int _x;
    private int _y;

    public ShootEvent(Object source, int x, int y) {
        super(source);
        _x = x;
        _y = y;
    }

    public int x() {
        return _x;
    }

    public int y() {
        return _y;
    }
}
