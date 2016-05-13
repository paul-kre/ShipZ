package shipz.gamemode;

import java.util.Random;
import shipz.MirrorField;
import shipz.Player;


/**
 * Hauptklasse f�r die K�nstliche Intelligenz<br><br>
 * 
 * Die Klasse vererbt alle notwendigen Instanzvariablen und -methoden <br>
 * f�r die Schwierigkeitsgrade Easy, Normal und Hard <br>
 * 
 * @author Artur Hergert
 *
 */
public abstract class Computer extends Player implements MirrorField {

	
	//IV
	
	/** Die zuletzt getroffenen X und Y-Koordinaten der KI werden zur<br>
	 * Weiterverwendung in einem zwei-feld gro�em Array gespeichert*/
	private int[] lastFieldHit;
	
	/** Gibt aus ob bei der Pr�fung der direkten Nachbarkoordinaten eines Treffers alle<br>
	 * Nachbarn auf weitere m�gliche Treffer gepr�ft wurden
	 */
	private boolean allNeigboursChecked = false;
	
	
	/** Die Feldgr��e des aktuellen Spiels. Standardm��ig ist es 10 x 10  */	
	protected int fieldSize = 10;
	
	/** 
	 * Zweidimensionales boolean-Array, welches als Spiegelfeld dient (Siehe Interface MirrorField)<br>
	 * Zur Speicherung der Felder die man schon getroffen hat und die man nicht mehr
	 * beschie�en darf.<br>
	 *  */
	 protected boolean[][] mirrorField; 
	
	/**
	 *  Array zum �berpr�fen aller Richtungen um eine getroffene Koordinate. <br><br>
	 * 
	 *  Reihenfolge der Indices: Norden, Osten, S�den, Westen.<br><br>
	 *  
	 *  Je nach Zustand werden die Werte in den entsprechenden Indices ge�ndert.<br><br>
	 *  
	 *  ZUST�NDE:<br>
	 *  0: Richung wurde noch nicht gepr�ft.<br>
	 *  -1: Richtung wurde gepr�ft und beeinhaltet keine beschiessbaren Koordinaten (mehr)<br>
	 *  1: Richtung wurde gepr�ft und beeinhaltet h�chstwarscheinlich weitere beschiessbare Koordinaten<br><br>
	 *  
	 *  Sobald alle Richtungen gepr�ft und auf -1 gesetzt wurden oder sobald eine Richtung von 1 auf -1 wechselt<br>
	 * 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-> Alle Werte werden durch eine Methode auf 0 resettet f�r den n�chsten Treffer
	 * 
	 *  
	 */
	private int[] allDirectionsChecked = new int[] {0, 0, 0, 0};
	
	/** Random-Object zur Generierung von zuf�lligen Integer Zahlen */
	protected Random random = new Random();
	
	
	//Contructor 
	/**
	 * 
	 * @param newFieldSize Die Feldgr��e des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
	 */
	public Computer(int newFieldSize){
		
		fieldSize = newFieldSize;
		initiateMirrorField(fieldSize);
		lastFieldHit = new int[2];
		
	}
	
			
	//IM
	
	/**
	 * Implementierung der shootField-Methode von Player f�r die KI 
	 * mit Body
	 * 
	 * Methode der KI zum erstellen der Zufallskoordinaten f�r einen<br>
	 * Beschuss des gegnerischen Spielfeldes<br>
	 * 
	 * @return Die Koordinaten die abgeschossen werden sollen.
	 */
	public abstract int[] shootField(TempKiGame game);
	
	
	/**
	 * Implementierung der shootField-Methode von
	 * Player ohne Inhalt.
	 */
	public int[] shootField() { return new int[] {0,0}; }
	
	
	/**
	 * Zur Erstellung von Zufallskoordinaten: public int nextInt(int n)
	 */
	
	
	/**
	 * Die direkten Nachbaarkoordinaten einer getroffenen Koordinate werden 
	 * zur �berpr�fung ausgew�hlt
	 * 
	 * @return Eine Nachbarkoordinate des zuletzt getroffenen Feldes. Wird durch die Methode searchAllDirections erstellt.
	 */
	protected int[] selectNeighbourCoordinates(){
		
		
		return ( searchAllDirections( 0, 0) );
	}
	
	
	/**
	 * Es wird von einer Koordinate aus in Nord-, S�d-, West- oder Ost-Richtung
	 * gesucht
	 * 
	 * @param xAxis Richtungskoordinate f�r die Bewegung innerhalb der X-Achse
	 * @param yAxis Richtungskoordinate f�r die Bewegung innerhalb der Y-Achse
	 * 
	 * @return Die n�chstliegende Koordinate in einer Richtung
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
	 * standardm��ig beschie�bar.
	 * 
	 * @param fieldSize Feldgr��e des Spiegelfeldes. Sollte die gleiche Gr��e wie das benutzte Spieler- bzw KI-Feld sein.
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
	 * Pr�fen ob eine Zelle schon besetzt wurde oder
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
	
	/**
	 * �berpr�ft ob eine Koordinate sich im Spielfeld
	 * oder au�erhalb befindet.
	 * Zur Vorbeugung eines ArrayOutOfBounds
	 * 
	 * @param yCoord Y-Koordinate der Zelle
	 * @param xCoord X-Koordinate der Zelle
	 * @return Ob eine Koordinate sich im Spielfeld befindet oder nicht
	 */
	protected boolean isCoordinateInField(int yCoord, int xCoord){
		
		return false;
	}
	
	/**
	 * Umgebung eines Schiffes in das Spiegelfeld speichern
	 * 
	 * @param yCoord Y-Koordinate der Zelle
	 * @param xCoord X-Koordinate der Zelle
	 */
	protected void saveShipVicinity(int yCoord, int xCoord, TempKiGame game){
		
		int currentY = yCoord;
		int currentX = xCoord;
		
		//Zuerst wird Vertikal auf Schiffsteile �berpr�ft
		//Die n�rdliche und s�dliche Koordinate der zu �berpr�fenden Koordinate wird
		//�berp�ft, ob sie �berhaupt im Feld ist
		if ( isCoordinateInField(currentY-1, currentX) && isCoordinateInField (currentY+1,currentX)){
			
			//�berpr�fen ob n�rdliche und s�dliche Koord. schonmal getroffen wurden
			if ( game.checkTile(currentX, currentY-1) == 1  && game.checkTile(currentX, currentY+1) == 1 ){
				
			}
			
			
		} else {
			
			
		}
	}
	
}//end class Computer
