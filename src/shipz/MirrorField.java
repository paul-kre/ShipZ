package shipz;

/**
 * Methoden zur Verwaltung eines leeren boolean-Spielfelds, damit sich Benutzer 
 * einen �berblick �ber die Felder beschaffen k�nnen, die man
 * beschossen hat und �ber die Felder mit denen
 * man nicht mehr interagieren kann.<br>
 * 
 * 
 * Zur Nutzung der Methoden:<br>
 * 
 * Definieren eines zweidimensionalen boolean-Arrays: Beispiel: boolean[][] mirror; <br>
 * Array initialisieren: Aufruf der Methode initiateMirrorField( <�bergebenes boolean[][]>, <Gr��e des Feldes> ) <br>
 * Danach k�nnen die Methoden benutzt werden.
 * 
 * @author Artur Hergert
 *
 */
public interface MirrorField {

	
	 
	//IM
	
	/**
	* Umgebung eines Schiffes speichern
	* 
	* @param yCoord Y-Koordinate eines getroffenen Schiffsteils
	* */
	default void saveShipVicinity(){
		
	}
	
	/**
	 * Koordinaten im Spiegelfeld unbeschiessbar setzen
	 * 
	 * @param mirrorField Zweidimensionales Spiegelfeld-Array
	 * @param yCoord Y-Koordinate der Zelle
	 * @param xCoord X-Koordinate der Zelle
	 * 
	 */
	default void fillMirrorField(boolean[][] mirrorField, int yCoord, int xCoord) { 
		
		mirrorField[yCoord][xCoord] = true;
	}
	
	/**
	 * Pr�fen ob eine Zelle schon besetzt wurde oder
	 * noch zum Abschuss frei gegeben ist
	 * 
	 * @param mirrorField Zweidimensionales Spiegelfeld-Array
	 * @param yCoord Y-Koordinate der Zelle
	 * @param xCoord X-Koordinate der Zelle
	 * 
	 * @return Ob eine Koordinate schon besetzt ist oder nicht
	 */
	default boolean isCoordinateOccupied (boolean[][] mirrorField,int yCoord, int xCoord){
		
		return mirrorField[yCoord][xCoord];
		
	}
	
	/**
	 * Initalisierung des Spiegelfeldes.<br>
	 * 
	 * Alle Koordinaten werden auf false gesetzt und sind 
	 * standardm��ig beschie�bar.
	 * 
	 * @param mirrorField Zweidimensionales Spiegelfeld-Array
	 * @param fieldSize Feldgr��e des Spiegelfeldes. Sollte die gleiche Gr��e wie das benutzte Spieler- bzw KI-Feld sein.
	 */
	default void initiateMirrorField (boolean[][] mirrorField, int fieldSize){
		
		
		for(int i= 0; i < mirrorField.length; i++ ){
			
			for ( int j = 0; j < mirrorField[i].length; j++ ){
				
				mirrorField[i][j] = false;
			}
		}
		
		
	}
	
	
}//end interface MirrorField
