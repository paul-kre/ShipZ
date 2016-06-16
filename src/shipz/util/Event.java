package shipz.util;

/**
 * Created by paulkretschel on 17/05/16.
 */

public interface Event {

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
    byte NO_DRAWS_TO_UNDO_EVENT = 80;
    byte NO_DRAWS_TO_REDO_EVENT = 81;
    
    // IDs von 110-120 reserviert
    

}
