package shipz.network;

/**
 * Created by Paul on 03.05.2016.
 */
public class Timer {
    private int _duration;
    private long _startTime;

    public Timer(int duration) {
        _duration = duration;
        _startTime = System.currentTimeMillis();
    }

    public boolean hasTime() {
        return ( System.currentTimeMillis() - _startTime ) < _duration;
    }
}
