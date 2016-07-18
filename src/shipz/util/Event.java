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
	byte NET_ENABLE_GUI = 34;
	byte NET_SHOOT_REQUEST = 35;
	byte NET_HIGHSCORE = 36;
	byte NET_GAMEOVER = 37;
	byte NET_STARTGAME = 38;
	byte SETTINGS_EVENT = 121;
	byte SETTINGS2_EVENT = 122;

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
	byte LOCK_EVENT = 59;
	byte LOAD_TABLE_EVENT = 120;

	// IO
	byte SAVE_EVENT = 110;
	byte LOAD_EVENT = 111;
	byte UNDO_EVENT = 112;
	byte REDO_EVENT = 113;
	byte HIGHSCORE_EVENT = 114;
	
}
