package shipz.util;

/**
 * Created by paulkretschel on 17/05/16.
 */
public interface EventIds {

    // General
    static final byte SHOOT_EVENT = 10;
    static final byte CLOSE_EVENT = 20;

    // Network
    static final byte DISCONNECT_EVENT = 30;
    static final byte SHOOT_RESULT = 31;
    
    // IO
    static final byte SAVE_EVENT = 110;
    static final byte LOAD_EVENT = 111;
    static final byte UNDO_EVENT = 112;
    static final byte REDO_EVENT = 113;
    static final byte HIGHSCORE_EVENT = 114;
    
    // IDs von 110-120 reserviert
    

}
