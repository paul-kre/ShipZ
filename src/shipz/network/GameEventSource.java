/**
 * Created by Paul on 06.05.2016.
 */
public class GameEventSource {
    private GameEventListener _listener;

    public void setEventListener(GameEventListener listener) {
        _listener = listener;
    }

    protected void received(String msg) {
        MsgEvent evt = new MsgEvent(this, msg);
        _listener.onReceived(evt);
    }
}
