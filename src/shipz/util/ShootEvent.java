package shipz.util;

import java.util.EventObject;

/**
 * Created by Paul on 06.05.2016.
 */
public class ShootEvent extends EventObject {
    private int _x;
    private int _y;
    private byte _hit;

    public ShootEvent(Object source, int x, int y) {
        super(source);
        _x = x;
        _y = y;
        _hit = -1;
    }

    public int x() {
        return _x;
    }

    public int y() {
        return _y;
    }

    public boolean isMissed() {
        return _hit == 0;
    }

    public boolean isHit() {
        return _hit == 1;
    }

    public boolean isSunk() {
        return _hit == 2;
    }
}
