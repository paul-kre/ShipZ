package gamemode;

import java.util.Random;
import shipz.MirrorField;
import shipz.Player;


/**
 * Hauptklasse für die Künstliche Intelligenz<br><br>
 * 
 * Die Klasse vererbt alle notwendigen Instanzvariablen und -methoden <br>
 * für die Schwierigkeitsgrade Easy, Normal und Hard <br>
 * 
 * @author Artur Hergert
 *
 */
public abstract class Computer extends Player implements MirrorField {

	
	//IV
	
	/** Die zuletzt getroffenen X und Y-Koordinaten der KI werden zur<br>
	 * Weiterverwendung in einem zwei-feld großem Array gespeichert*/
	private int[] lastFieldHit;
	
	/** Gibt aus ob bei der Prüfung der direkten Nachbarkoordinaten eines Treffers alle<br>
	 * Nachbarn auf weitere mögliche Treffer geprüft wurden
	 */
	private boolean allNeigboursChecked = false;
	
	
	/** Die Feldgröße des aktuellen Spiels. Standardmäßig ist es 10 x 10  */	
	protected int fieldSize = 10;
	
	/** 
	 * Zweidimensionales boolean-Array, welches als Spiegelfeld dient (Siehe Interface MirrorField)<br>
	 * Zur Speicherung der Felder die man schon getroffen hat und die man nicht mehr
	 * beschießen darf.<br>
	 *  */
	 protected boolean[][] mirrorField; 
	
	/**
	 *  Array zum überprüfen aller Richtungen um eine getroffene Koordinate. <br><br>
	 * 
	 *  Reihenfolge der Indices: Norden, Osten, Süden, Westen.<br><br>
	 *  
	 *  Je nach Zustand werden die Werte in den entsprechenden Indices geändert.<br><br>
	 *  
	 *  ZUSTÄNDE:<br>
	 *  0: Richung wurde noch nicht geprüft.<br>
	 *  -1: Richtung wurde geprüft und beeinhaltet keine beschiessbaren Koordinaten (mehr)<br>
	 *  1: Richtung wurde geprüft und beeinhaltet höchstwarscheinlich weitere beschiessbare Koordinaten<br><br>
	 *  
	 *  Sobald alle Richtungen geprüft und auf -1 gesetzt wurden oder sobald eine Richtung von 1 auf -1 wechselt<br>
	 * 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-> Alle Werte werden durch eine Methode auf 0 resettet für den nächsten Treffer
	 * 
	 *  
	 */
	private int[] allDirectionsChecked = new int[] {0, 0, 0, 0};
	
	/** Random-Object zur Generierung von zufälligen Integer Zahlen */
	protected Random random = new Random();
	
	
	public Computer (){}
	//Contructor 
	/**
	 * 
	 * @param newFieldSize Die Feldgröße des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
	 */
	public Computer(int newFieldSize){
		
		fieldSize = newFieldSize;
		initiateMirrorField(fieldSize);
		lastFieldHit = new int[2];
		
	}
	
			
	//IM
	
	/**
	 * Methode der KI zum erstellen der Zufallskoordinaten für einen<br>
	 * Beschuss des gegnerischen Spielfeldes<br>
	 * 
	 * @return Die Koordinaten die abgeschossen werden sollen.
	 */
	public abstract int[] shootField();
	
	
	/**
	 * Zur Erstellung von Zufallskoordinaten: public int nextInt(int n)
	 */
	
	
	/**
	 * Die direkten Nachbaarkoordinaten einer getroffenen Koordinate werden 
	 * zur Überprüfung ausgewählt
	 * 
	 * @return Eine Nachbarkoordinate des zuletzt getroffenen Feldes. Wird durch die Methode searchAllDirections erstellt.
	 */
	protected int[] selectNeighbourCoordinates(){
		
		
		return ( searchAllDirections( 0, 0) );
	}
	
	
	/**
	 * Es wird von einer Koordinate aus in Nord-, Süd-, West- oder Ost-Richtung
	 * gesucht
	 * 
	 * @param xAxis Richtungskoordinate für die Bewegung innerhalb der X-Achse
	 * @param yAxis Richtungskoordinate für die Bewegung innerhalb der Y-Achse
	 * 
	 * @return Die nächstliegende Koordinate in einer Richtung
	 */
	protected int[ ] searchAllDirections ( int xAxis, int yAxis){
		
		return null;
	}
	
	
	/**
	 * Redefenierung der Methode aus dem Interface MirrorField
	 * 
	 * Initalisierung des Spiegelfeldes.<br>
	 * 
	 * Alle Koordinaten werden auf false gesetzt und sind 
	 * standardmäßig beschießbar.
	 * 
	 * @param fieldSize Feldgröße des Spiegelfeldes. Sollte die gleiche Größe wie das benutzte Spieler- bzw KI-Feld sein.
	 */
	protected void initiateMirrorField (int fieldSize){
		
		this.mirrorField = new boolean[fieldSize][fieldSize];
		
		for(int i= 0; i < mirrorField.length; i++ ){
			
			for ( int j = 0; j < mirrorField[i].length; j++ ){
				
				mirrorField[i][j] = false;
			}
		}
	}
	
	
	/**
	 * Redefenierung der Methode aus dem Interface MirrorField
	 * 
	 * Koordinaten im Spiegelfeld unbeschiessbar setzen
	 * 
	 * @param yCoord Y-Koordinate der Zelle
	 * @param xCoord X-Koordinate der Zelle
	 * 
	 */
	protected void fillMirrorField(int yCoord, int xCoord) { 
		
		this.mirrorField[yCoord][xCoord] = true;
	}
	
	/**
	 * Redefenierung der Methode aus dem Interface MirrorField
	 * 
	 * Prüfen ob eine Zelle schon besetzt wurde oder
	 * noch zum Abschuss frei gegeben ist
	 * 
	 * @param yCoord Y-Koordinate der Zelle
	 * @param xCoord X-Koordinate der Zelle
	 * 
	 * @return Ob eine Koordinate schon besetzt ist oder nicht
	 */
	protected boolean isCoordinateOccupied (int yCoord, int xCoord){
		
		return this.mirrorField[yCoord][xCoord];
		
	}
	
}//end class Computer
