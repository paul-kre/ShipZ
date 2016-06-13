package shipz.io;

import java.util.Stack;

/**
 * Diese Klasse speichert alle Z�ge die get�tigt werden und behandelt die Undo-Redo-Funktion.
 * @author Florian Osterberg
 */
public class UndoRedo {

	// IV
	/** In diesem Stack werden alle Z�ge als Strings gespeichert. */
	private Stack<String> game;
	/** In diesem Stack werden alle r�ckg�ngig gemachten Z�ge als Strings gespeichert. */
	private Stack<String> redo;
	/** Score-Objekt */
	private Score score;
	/** SaveLoad-Objekt */
	private SaveLoad saveload;
	/** Der String, der einzelne Z�ge in der Datei trennt. */
	private String drawSeparator = ",";
	/** Stack, der bei jedem Undo z�hlt, wie viele Z�ge r�ckg�ngig gemacht wurden,
	 * und die Anzahl auf den Stapel legt. Notwendig f�r Redo. */
	private Stack<Integer> redoneDrawsCounter;
	
	// Konstruktor
	/**
	 * Initialisiert ein UndoRedo-Objekt.
	 * Die Stacks werden initialisiert.
	 * @param gameName 
	 */
	public UndoRedo() {
		game = new Stack<String>();
		redo = new Stack<String>();
		redoneDrawsCounter = new Stack<Integer>();
		score = new Score();
		saveload = new SaveLoad();
	}
	
	// IM
	/**
	 * Wenn ein neuer Zug get�tigt wird, wird dieser in den String, der den Spielverlauf speichert, geschrieben.
	 * @param draw Der Zug, der get�tigt wird als {@link String}
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 */
	protected void newDraw(String draw, int playerIndex) {
		game.push(playerIndex+"|"+draw);
	}
	
	/**
	 * Der letzte Zug wird r�ckg�ngig gemacht.
	 * Er wird daf�r aus der Liste, die den Spielverlauf speichert gel�scht und in eine
	 * separate Liste geschrieben, die die r�ckgangig gemachten Z�ge speichert.
	 * Falls ein Redo ausgef�hrt wird, wird auf eben diese Liste zur�ckgegriffen.
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 * @return Die letzten Z�ge der Spielverlaufs-Liste, der in die Redoliste geschrieben wird
	 */
	protected String undoDraw(int playerIndex) {
		String result = "";
		String draw = "";
		int i = 0;
		while(!(draw.startsWith(playerIndex+""))) {
			draw = game.pop();
			redo.push(draw);
			result += draw + ";";
			i++;
		}
		score.setScore(playerIndex, 'u');
		redoneDrawsCounter.push(i);
		
		return result;
	}
	
	/**
	 * Der letzte Eintrag aus der Redo-Liste wird gel�scht und wieder in die Liste geschrieben,
	 * die den Spielverlauf speichert.
	 * Der zuletzt r�ckg�ngig gemachte Zug wird also ausgef�hrt.
	 * @return Der letzte Zug der Redoliste als {@link String}, der in die Spielverlaufs-Liste geschrieben wird.
	 */
	protected String redoDraw() {
		String result = "";
		String draw = "";
		int r = redoneDrawsCounter.pop();
		for(int i = 0; i < r; i++) {
			draw = redo.pop();
			game.push(draw);
			result += draw + ";";
		}
		return result;
	}
	
	/**
	 * Gibt den Stack f�r die Z�ge des ersten Spielers als String zur�ck.
	 * @return die ArrayList f�r die Z�ge als String
	 */
	protected String getDraws() {
		return game.toString();
	}
	
	/**
	 * Gibt den Stack f�r die r�ckg�ngig gemachten Z�ge des ersten Spielers zur�ck.
	 * @return der Stack f�r die r�ckg�ngig gemachten Z�ge
	 */
	protected String getRedoneDraws() {
		return redo.toString();
	}
	
	/**
	 * Die Instanz-Variable, die die Z�ge der Spieler speichert,
	 * speichert hiermit ihren Inhalt in der Datei
	 * @param gameName Name des Spiels zur Zuordnung
	 */
	protected void saveToFile(String gameName) {
		saveload.setDraws(gameName, game.toString().replaceAll(", ", drawSeparator).replaceAll("]", "").substring(1));
	}
	
	/**
	 * Leert alle Stacks.
	 * Alle get�tigten Z�ge werden gel�scht,
	 * sie werden NICHT in der Datei gespeichert.
	 * Um die Daten zu speichern sollte vorher die Methode
	 * <i>.saveToFile()</i> aufgerufen werden.
	 */
	protected void clear() {
		game.clear();
		redo.clear();
	}
	
	/**
	 * main method
	 * @param args arguments
	 */
	public static void main(String[] args) {
		UndoRedo ur = new UndoRedo();
		ur.newDraw("eins1", 1);
		ur.newDraw("zwei1", 1);
		ur.newDraw("drei1", 1);
		ur.newDraw("eins2", 2);
		ur.newDraw("zwei2", 2);
		ur.newDraw("eins_1", 1);
		ur.newDraw("zwei_1", 1);
		ur.newDraw("eins_2", 2);
		System.out.println(ur.getDraws());
		System.out.println(ur.undoDraw(1));
		System.out.println(ur.getDraws());
		System.out.println(ur.getRedoneDraws());
		ur.redoDraw();
		System.out.println(ur.getDraws());
		
	}
	
}
