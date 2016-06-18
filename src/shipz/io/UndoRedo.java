package shipz.io;

import java.util.Stack;

import shipz.util.NoDrawException;

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
	/** SaveLoad-Objekt */
	private SaveLoad saveload;
	/** Der String, der einzelne Z�ge in der Datei trennt. */
	private String drawSeparator = ",";
	
	// Konstruktor
	/**
	 * Initialisiert ein UndoRedo-Objekt.
	 * Die Stacks werden initialisiert.
	 * @param gameName 
	 */
	public UndoRedo() {
		game = new Stack<String>();
		redo = new Stack<String>();
		saveload = new SaveLoad();
	}
	
	// IM
	/**
	 * Wenn ein neuer Zug get�tigt wird, wird dieser in den String, der den Spielverlauf speichert, geschrieben.
	 * @param draw Der Zug, der get�tigt wird als {@link String}. Format: x,y
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 * @param result 0=wasser, 1=treffer, 2=versenkt, 3=undo
	 */
	protected void newDraw(int x, int y, int playerIndex, byte result) {
		game.push(playerIndex + "|" + x + "," + y + "|" + result);
	}
	
	/**
	 * Der letzte Zug wird r�ckg�ngig gemacht.
	 * Er wird daf�r aus der Liste, die den Spielverlauf speichert gel�scht und in eine
	 * separate Liste geschrieben, die die r�ckgangig gemachten Z�ge speichert.
	 * Falls ein Redo ausgef�hrt wird, wird auf eben diese Liste zur�ckgegriffen.
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 * @return Die letzten Z�ge der Spielverlaufs-Liste, der in die Redoliste geschrieben wird
	 * @throws NoDrawException tritt auf falls keine weiteren Z�ge auf dem Stack sind die r�ckg�ngig gemacht werden k�nnen.
	 */
	protected String undoDraw(int playerIndex) throws NoDrawException {
		String result = "";
		String draw = "";
		while(!(draw.startsWith(playerIndex+""))) {
			if(game.isEmpty() == false) {
				draw = game.pop();
				redo.push(draw);
				result += draw + ";";
			} else {
				throw new NoDrawException("Keine weiteren Z�ge vorhanden!");
			}
		}
		return result;
	}
	
	/**
	 * Der letzte Eintrag aus der Redo-Liste wird gel�scht und wieder in die Liste geschrieben,
	 * die den Spielverlauf speichert.
	 * Der zuletzt r�ckg�ngig gemachte Zug wird also ausgef�hrt.
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 * @return Der letzte Zug der Redoliste als {@link String}, der in die Spielverlaufs-Liste geschrieben wird.
	 * @throws NoDrawException tritt auf, falls keine weiteren Z�ge auf dem Stack sind, die wiederholt werden k�nnen.
	 */
	protected String redoDraw(int playerIndex) throws NoDrawException {
		String result = "";
		String draw = "";
		while(!(draw.startsWith(playerIndex+""))) {
			if(redo.empty() == false) {
				draw = redo.pop();
				game.push(draw);
				result += draw + ";";
			} else {
				throw new NoDrawException("Keine weiteren Z�ge vorhanden!");
			}
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
		ur.newDraw(8, 8, 1, (byte)1);
		ur.newDraw(4, 5, 1, (byte)1);
		ur.newDraw(1, 1, 1, (byte)0);
		ur.newDraw(7, 6, 2, (byte)1);
		ur.newDraw(2, 2, 2, (byte)0);
		ur.newDraw(9, 9, 1, (byte)1);
		ur.newDraw(1, 3, 1, (byte)0);
		ur.newDraw(2, 7, 2, (byte)0);
		try {
			System.out.println(ur.getDraws());
			System.out.println(ur.undoDraw(1));
			System.out.println(ur.getDraws());
			System.out.println(ur.getRedoneDraws());
			System.out.println(ur.redoDraw(1));
			System.out.println(ur.getDraws());
			System.out.println(ur.getRedoneDraws());
			System.out.println(ur.redoDraw(2));
			System.out.println(ur.getDraws());
			System.out.println(ur.getRedoneDraws());
		} catch(NoDrawException e) {
			e.printStackTrace();
		}

	}
	
}
