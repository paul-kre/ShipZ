package shipz;

import shipz.util.GameEventSource;

/**
 * Created by Paul on 06.05.2016.
 */
public abstract class Player extends GameEventSource implements Runnable {
    private String _name;

    public Player(String name) {
        _name = name;
    }

    public String name() {
        return _name;
    }
}
