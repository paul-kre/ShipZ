package shipz.util;

/**
 * Die einzelnen Event-IDs werden als Aufzählungstyp abgespeichert.
 * Zugriff auf die einzelnen Werte:
 * Event.SHOOT_EVENT.value
 * @author Florian Osterberg
 */
public enum Event {

	// General
	SHOOT_EVENT(10),
	CLOSE_EVENT(20),
	
	// Network
	DISCONNECT_EVENT(30),
	SHOOT_RESULT(31),
	
	// IO
	SAVE_EVENT(110),
	LOAD_EVENT(111),
	UNDO_EVENT(112),
	REDO_EVENT(113),
	HIGHSCORE_EVENT(114);
	
	// IV
	private int value;
	
	// Constructor
	private Event(int value) {
		this.value = value;
	}
	
	// IM
	public int value() {
		return this.value;
	}
	
}
