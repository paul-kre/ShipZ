import java.util.EventObject;

/**
 * Created by Paul on 06.05.2016.
 */
public class MsgEvent extends EventObject {
    private String _msg;

    public MsgEvent(Object source, String msg) {
        super(source);
        _msg = msg;
    }

    public String msg() {
        return _msg;
    }
}
