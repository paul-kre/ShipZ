package shipz.util;

import java.util.EventObject;

/**
 * Created by Paul on 06.05.2016.
 */
public class ShootEvent extends EventObject {
    private int _x;
    private int _y;
    private char _hit;

    public ShootEvent(Object source, int x, int y) {
        super(source);
        _x = x;
        _y = y;
        _hit = ' ';
    }

    public int x() {
        return _x;
    }

    public int y() {
        return _y;
    }

    public char getHit() {
        return _hit;
    }

    public void setHit(char c) {
        _hit = c;
    }

    public boolean isMissed() {
        return _hit == 'w';
    }

    public boolean isHit() {
        return _hit == 'x';
    }

    public boolean isSunk() {
        return _hit == 'z';
    }
}
