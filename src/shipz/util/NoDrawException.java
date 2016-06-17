package shipz.util;

/**
 * Exception die geworfen wird, wenn keine weiteren Z�ge
 * r�ckg�ngig gemacht werden oder wiederholt werden k�nnen,
 * weil keine Z�ge mehr auf dem Stack liegen.
 * @author Florian Osterberg
 */
public class NoDrawException extends Exception {
	
	public NoDrawException() {}
	
	public NoDrawException(String message) {
		super(message);
	}
	
	public NoDrawException(Throwable cause) {
		super(cause);
	}
	
	public NoDrawException(String message, Throwable cause) {
		super(message, cause);
	}

}
