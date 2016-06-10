package shipz.util;

/**
 * Die einzelnen Event-IDs werden als Aufzählungstyp abgespeichert.
 * Zugriff auf die einzelnen Werte:
 * Event.SHOOT_EVENT.value
 * @author Florian Osterberg
 */
public enum Event {

	// General
	SHOOT_EVENT,
	CLOSE_EVENT,
	
	// Network
	DISCONNECT_EVENT,
	SHOOT_RESULT,
	
	// IO
	SAVE_EVENT,
	LOAD_EVENT,
	UNDO_EVENT,
	REDO_EVENT,
	HIGHSCORE_EVENT;
	
}
