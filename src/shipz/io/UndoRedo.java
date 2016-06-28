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
	
	// Konstanten
	/** Der String, der einzelne Züge in der Datei trennt. */
	private final static String DRAW_SEPARATOR = ";";
	
	// Konstruktor
	/**
	 * Initialisiert ein UndoRedo-Objekt.
	 * Die Stacks werden initialisiert.
	 */
	public UndoRedo() {
		game = new Stack<String>();
		redo = new Stack<String>();
		saveload = new SaveLoad();
	}
	
	// IM
	/**
	 * Wenn ein neuer Zug getätigt wird, wird dieser in den String, der den Spielverlauf speichert, geschrieben.
	 * Format des fertigen Strings:
	 * <b>playerIndex</b>~<b>x</b>,<b>y</b>~<b>result</b>
	 * @param x x-Koordinate des Zugs
	 * @param y y-Koordinate des Zugs
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @param result 0=wasser, 1=treffer, 2=versenkt, 3=undo
	 */
	protected void newDraw(int x, int y, int playerIndex, int result) {
		game.push(playerIndex + "~" + x + "," + y + "~" + result); // Zug wird auf den Stack gelegt
		
		System.out.println(game.toString()); // nur zu Testzwecken
	}
	
	/**
	 * Der letzte Zug wird rückgängig gemacht.
	 * Er wird dafür aus der Liste, die den Spielverlauf speichert gelöscht und in eine
	 * separate Liste geschrieben, die die rückgängig gemachten Züge speichert.
	 * Falls ein Redo ausgeführt wird, wird auf eben diese Liste zurückgegriffen.
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Die letzten Züge der Spielverlaufs-Liste, der in die Redoliste geschrieben wird
	 * @throws NoDrawException tritt auf falls keine weiteren Züge auf dem Stack sind die rückgängig gemacht werden können.
	 */
	protected String undoDraw(int playerIndex) throws NoDrawException {
		String result = "";
		String draw = "";
		while(!(draw.startsWith(playerIndex+""))) {
			if(!game.isEmpty()) {
				draw = game.pop();
				redo.push(draw);
				result += draw + DRAW_SEPARATOR;
			} else {
				throw new NoDrawException();
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
			if(!redo.empty()) {
				draw = redo.pop();
				game.push(draw);
				result += draw + DRAW_SEPARATOR;
			} else {
				throw new NoDrawException();
			}
		}
		return result;
	}
	
	/**
	 * Gibt den Stack für die Züge des ersten Spielers als String zurï¿½ck.
	 * @return die ArrayList für die Züge als String
	 */
	protected String getDraws() {
		return game.toString();
	}
	
	/**
	 * Gibt den Stack für die rückgängig gemachten Züge des ersten Spielers zurï¿½ck.
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
		saveload.setDraws(gameName, game.toString().replaceAll(", ", DRAW_SEPARATOR).replaceAll("]", "").substring(1));
	}
	
	/**
	 * Leert alle Stacks und lädt die Züge aus einer Datei in die Instanz-Variablen.
	 * Wird verwendet, wenn ein Spielstand geladen wird.
	 * @param gameName Name des Spielstands
	 */
	protected void loadDraws(String gameName) {
		game.clear();
		redo.clear();
		
		String[] draws = saveload.getDraws(gameName).split(DRAW_SEPARATOR);
		int x = 0, y = 0, playerIndex = 0;
		byte result = 0;
		for(int i = 0; i < draws.length; i++) {
			x = Integer.parseInt(draws[i].split("~")[1].split(",")[0]);
			y = Integer.parseInt(draws[i].split("~")[1].split(",")[1]);
			result = Byte.parseByte(draws[i].split("~")[2]);
			playerIndex = Integer.parseInt(draws[i].split("~")[0]);
			newDraw(x, y, playerIndex, result);
		}
	}
	
	/**
	 * main method
	 * @param args arguments
	 */
	public static void main(String[] args) {
		UndoRedo ur = new UndoRedo();
		ur.newDraw(8, 8, 1, 1);
		ur.newDraw(4, 5, 1, 1);
		ur.newDraw(1, 1, 1, 0);
		ur.newDraw(7, 6, 2, 1);
		ur.newDraw(2, 2, 2, 0);
		ur.newDraw(9, 9, 1, 1);
		ur.newDraw(1, 3, 1, 0);
		ur.newDraw(2, 7, 2, 0);
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
