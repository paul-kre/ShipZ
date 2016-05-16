package shipz.network;

/**
 * Created by Paul on 16.05.2016.
 */
public class Shot {
    private int _x;
    private int _y;
    private char _hit;

    public Shot(int x, int y) {
        _x = x;
        _y = y;
        _hit = 0;
    }

    public Shot(int x, int y, char hit) {
        _x = x;
        _y = y;
        _hit = hit;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public char getHit() {
        return _hit;
    }

    public void setHit(char c) {
        _hit = c;
    }

    public String toString() {
        return _x + ":" + _y + ":" + _hit;
    }
}
