package shipz.util;

/**
 * Created by paulkretschel on 17/05/16.
 */

public interface Event {

	// General
	byte SHOOT_EVENT = 10;
	byte CLOSE_EVENT = 20;

	// Network
	byte SEND_EVENT = 30;
	byte NET_SHOOT_EVENT = 31;
	byte DISCONNECT_EVENT = 32;
	byte SHOOT_RESULT = 33;

	// AI
	byte AI_SHOOT_EVENT = 40;

	// GUI
	byte GUI_SHOOT_EVENT = 50;
	byte FILL_EVENT = 51;
	byte FINISHED_ROUND = 52;
	byte READY_EVENT = 53;
	byte PAUSE_EVENT = 54;
	byte PVP_EVENT = 55;
	byte PVK_EVENT = 56;
	byte KVK_EVENT = 57;
	byte CONNECT_EVENT = 58;



	// IO
	byte SAVE_EVENT = 110;
	byte LOAD_EVENT = 111;
	byte UNDO_EVENT = 112;
	byte REDO_EVENT = 113;
	byte HIGHSCORE_EVENT = 114;

	// IDs von 110-120 reserviert
    
}
