package shipz.util;

/**
 * Created by paulkretschel on 17/05/16.
 */
public interface EventIds {

    // General
    final byte SHOOT_EVENT = 10;
    final byte CLOSE_EVENT = 20;

    // Network
    final byte DISCONNECT_EVENT = 30;
    final byte SHOOT_RESULT = 31;
    
    // IO
    final byte SAVE_EVENT = 110;
    final byte LOAD_EVENT = 111;
    final byte UNDO_EVENT = 112;
    final byte REDO_EVENT = 113;
    final byte HIGHSCORE_EVENT = 114;
    
    // IDs von 110-120 reserviert
    

}
