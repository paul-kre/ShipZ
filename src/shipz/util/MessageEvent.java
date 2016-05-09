package shipz.util;

import java.util.EventObject;

/**
 * Created by Paul on 06.05.2016.
 */
public class MessageEvent extends EventObject {
    private String _msg;

    public MessageEvent(Object source, String msg) {
        super(source);
        _msg = msg;
    }

    public String msg() {
        return _msg;
    }
}
