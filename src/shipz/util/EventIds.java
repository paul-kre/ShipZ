package shipz.util;

/**
 * Created by paulkretschel on 17/05/16.
 */

public interface EventIds {

    // General
    byte SHOOT_EVENT = 10;
    byte CLOSE_EVENT = 20;

    // Network
    byte NET_SHOOT_EVENT = 30;
    byte DISCONNECT_EVENT = 31;
    byte SHOOT_RESULT = 32;

    // AI
    byte AI_SHOOT_EVENT = 40;

    // GUI
    byte GUI_SHOOT_EVENT = 50;
    byte FILL_EVENT = 51;
    
    // IO
    byte SAVE_EVENT = 110;
    byte LOAD_EVENT = 111;
    byte UNDO_EVENT = 112;
    byte REDO_EVENT = 113;
    byte HIGHSCORE_EVENT = 114;
    
    // IDs von 110-120 reserviert
    

}
