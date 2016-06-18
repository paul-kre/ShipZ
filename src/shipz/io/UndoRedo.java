package shipz.io;

import java.util.Stack;

import shipz.util.NoDrawException;

/**
 * Diese Klasse speichert alle Züge die getätigt werden und behandelt die Undo-Redo-Funktion.
 * @author Florian Osterberg
 */
public class UndoRedo {

	// IV
	/** In diesem Stack werden alle Züge als Strings gespeichert. */
	private Stack<String> game;
	/** In diesem Stack werden alle rückgängig gemachten Züge als Strings gespeichert. */
	private Stack<String> redo;
	/** SaveLoad-Objekt */
	private SaveLoad saveload;
	/** Der String, der einzelne Züge in der Datei trennt. */
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
	 * Wenn ein neuer Zug getätigt wird, wird dieser in den String, der den Spielverlauf speichert, geschrieben.
	 * @param draw Der Zug, der getätigt wird als {@link String}. Format: x,y
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @param result 0=wasser, 1=treffer, 2=versenkt, 3=undo
	 */
	protected void newDraw(int x, int y, int playerIndex, byte result) {
		game.push(playerIndex + "|" + x + "," + y + "|" + result);
	}
	
	/**
	 * Der letzte Zug wird rückgängig gemacht.
	 * Er wird dafür aus der Liste, die den Spielverlauf speichert gelöscht und in eine
	 * separate Liste geschrieben, die die rückgangig gemachten Züge speichert.
	 * Falls ein Redo ausgeführt wird, wird auf eben diese Liste zurückgegriffen.
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Die letzten Züge der Spielverlaufs-Liste, der in die Redoliste geschrieben wird
	 * @throws NoDrawException tritt auf falls keine weiteren Züge auf dem Stack sind die rückgängig gemacht werden können.
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
				throw new NoDrawException("Keine weiteren Züge vorhanden!");
			}
		}
		return result;
	}
	
	/**
	 * Der letzte Eintrag aus der Redo-Liste wird gelöscht und wieder in die Liste geschrieben,
	 * die den Spielverlauf speichert.
	 * Der zuletzt rückgängig gemachte Zug wird also ausgeführt.
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Der letzte Zug der Redoliste als {@link String}, der in die Spielverlaufs-Liste geschrieben wird.
	 * @throws NoDrawException tritt auf, falls keine weiteren Züge auf dem Stack sind, die wiederholt werden können.
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
				throw new NoDrawException("Keine weiteren Züge vorhanden!");
			}
		}
		return result;
	}
	
	/**
	 * Gibt den Stack für die Züge des ersten Spielers als String zurück.
	 * @return die ArrayList für die Züge als String
	 */
	protected String getDraws() {
		return game.toString();
	}
	
	/**
	 * Gibt den Stack für die rückgängig gemachten Züge des ersten Spielers zurück.
	 * @return der Stack für die rückgängig gemachten Züge
	 */
	protected String getRedoneDraws() {
		return redo.toString();
	}
	
	/**
	 * Die Instanz-Variable, die die Züge der Spieler speichert,
	 * speichert hiermit ihren Inhalt in der Datei
	 * @param gameName Name des Spiels zur Zuordnung
	 */
	protected void saveToFile(String gameName) {
		saveload.setDraws(gameName, game.toString().replaceAll(", ", drawSeparator).replaceAll("]", "").substring(1));
	}
	
	/**
	 * Leert alle Stacks.
	 * Alle getätigten Züge werden gelöscht,
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
