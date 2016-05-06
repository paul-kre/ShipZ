

/**
 * Spielerverwaltung
 * @author Max Pallasch
 * @version	0.1
 */
public abstract class Player {

	//IV
	/** Spielername */
	protected String name;
	
	
	
	//Methoden
	/**
	 * @return	Name des Spielers
	 */
	public String getName() { return ""; }
	
	
	
	
	/**
	 * Initiiert Beschuss auf eine Zelle
	 * 
	 * @return Abzuschieﬂende Zelle auf dem Spielfeld
	 */
	public abstract int[] shootField();
}
