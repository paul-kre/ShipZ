package shipz.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Diese Klasse ist für die Punktevergabe zuständig,
 * außerdem für das Speichern der Punkte in der Datei
 * und zusätzlich für das Verwalten und Generieren der Highscore-Liste.
 * @author Florian Osterberg
 */
public class Score {

	// IV
	/** SaveLoad-Objekt, das benötigt wird, um einige Methoden auszuführen. */
	private SaveLoad saveload;
	/** Highscore-File */
	private File highscoreFile;
	/** Scanner, der aus einer Datei liest. */
	private Scanner scanner;
	/** Zählt die Combos des ersten Spielers */
	private double comboPlayer1;
	/** Zählt die Combos des zweiten Spielers */
	private double comboPlayer2;
	/** Trennzeichen zwischen Namen und Punkten.
	 * Format: <i>spielername=punkte</i> */
	private String scoreSeparator = "=";
	/** Punktestand des ersten Spielers */
	private int scorePlayer1;
	/** Punktestand des zweiten Spielers */
	private int scorePlayer2;
	
	// Konstruktor
	/**
	 * Konstruktor von Score.
	 */
	public Score() {
		saveload = new SaveLoad();
		highscoreFile = new File(saveload.fileDirectory() + File.separator + "highscore.shipz");
		saveload.makeDirectory(highscoreFile);
		comboPlayer1 = 1;
		comboPlayer2 = 1;
		scorePlayer1 = 0;
		scorePlayer2 = 0;
	}
	
	// IM
	/**
	 * Setzt die Punktzahl eines bestimmten Spielers zu einem bestimmten Event.
	 * @param gameName Name des Spielstands
	 * @param playerName Name des Spielers
	 * @param event Events: <b>u</b> für undo, <b>h</b> für hit, <b>s</b> für sink <i>(weitere folgen eventuell. Das liegt nicht in meiner Hand)</i>
	 */
	protected void setScore(int playerIndex, char event) {
		switch(event) {
		case 'u':
			if(playerIndex == 1) {
				scorePlayer1 -= 30;
			} else if(playerIndex == 2){
				scorePlayer2 -= 30;
			} else error("set");
			break;
		case 'h':
			combo(playerIndex, event);
			if(playerIndex == 1) scorePlayer1 += 50*comboPlayer1;
			else if(playerIndex == 2) scorePlayer2 += 50*comboPlayer2;
			break;
		case 's':
			combo(playerIndex, event);
			if(playerIndex == 1) scorePlayer1 += 300*comboPlayer1;
			else if(playerIndex == 2) scorePlayer2 += 300*comboPlayer2;
			break;
		default:
			error("set");
		}
	}
	
	/**
	 * Methode, die die Combos verwaltet,
	 * den Combo-Counter hochzählt.
	 * @param gameName Name des Spielstands
	 * @param playerName Spielername
	 * @param event Aktion, die sich auf den Combowert beeinflusst. 
	 * Zugelassene Werte: <b>h</b> für einen Treffer, <b>s</b> für ein versenktes Schiff.
	 */
	private void combo(int playerIndex, char event) {
		if(playerIndex == 1) {
			if(event == 'h') {
				comboPlayer1 = comboPlayer1*2;
				comboPlayer2 = 1;
			} else if (event == 's'){
				comboPlayer1 += 0.5;
				comboPlayer2 = 1;
			}
		} else if(playerIndex == 2) {
			if(event == 'h') {
				comboPlayer2 = comboPlayer2*2;
				comboPlayer1 = 1;
			} else if (event == 's') {
				comboPlayer2 += 0.5;
				comboPlayer1 = 1;
			}
		} else {
			error("combo");
		}
		
	}
	
	/**
	 * Ändert die Punkte eines Spielers playerName zum Parameter score.
	 * @param playerName Der Spieler dessen Punkte geändert werden sollen.
	 * @param score Die Punkte, die der Spieler bekommen soll.
	 */
	@Deprecated
	private void setScore(int playerIndex, int score) {
		if(playerIndex == 1) {
			scorePlayer1 = score;
		} else if(playerIndex == 2) {
			scorePlayer2 = score;
		} else {
			error("set");
		}
	}
	
	/**
	 * Liest die Punkte eines bestimmten Spielers aus der Highscore-Datei
	 * und gibt diese zurück.
	 * @param playerName gewünschter Spieler
	 * @return Die Punktzahl des Spielers
	 */
	@Deprecated
	private int getScore(int playerIndex) {
		if(playerIndex == 1) {
			return scorePlayer1;
		} else if(playerIndex == 2) {
			return scorePlayer2;
		} else {
			error("get");
			return 0;
		}
	}
	
	/**
	 * Gibt immer den aktuellsten Spielernamen zurück.
	 * Falls also ein Spielername mehrmals in der Datei auftaucht,
	 * wurden bereits die #-Zeichen angehangen,
	 * also werden diese mit ausgegeben, damit keine Fehler
	 * beim Ändern der Punkte auftreten.
	 * @param playerName Spielername dessen Name gesucht werden soll
	 * @return der gefundene aktuelle Spielername
	 */
	@Deprecated
	private String getCurrentPlayerName(String playerName) {
		if(doesPlayerExist(playerName))
			return getCurrentPlayerName(playerName+"#");
		
		return playerName.substring(0, playerName.length()-1);
	}
	
	/**
	 * Mit der Methode aus der SaveLoad-Klasse wird der aktuelle Highscore als {@link String}
	 * in die Instanz-Variable <i>highscore</i> geschrieben.
	 */
	@Deprecated
	protected String readHighscoreFile() {
		return saveload.readFile(highscoreFile);
	}
	
	/**
	 * Ein bestimmter Spieler wird in die Highscore-Liste eingefügt.
	 * @param playerName Der Spieler, der eingefügt werden soll.
	 * @param score Punktzahl, die gesetzt werden soll.
	 */
	private void addPlayerIntoHighscore(String playerName, int score) {
		if(doesPlayerExist(playerName) == false) {
			saveload.writeFile(highscoreFile, saveload.readFile(highscoreFile)+playerName+scoreSeparator+score);
		} else {
			addPlayerIntoHighscore(playerName+"#", score);
		}
	}
	
	/**
	 * Ein bestimmter Spieler wird in die Highscore-Liste eingefügt.
	 * @see shipz.io.Score#addPlayerIntoHighscore(String, score)
	 * @param playerName Der Spieler, der eingefügt werden soll.
	 */
	private void addPlayerIntoHighscore(String playerName) {
		addPlayerIntoHighscore(playerName, 0);
	}
	
	/**
	 * Überprüft, ob ein Spieler bereits in der Score-Datei steht.
	 * @param playerName Der Spieler nach dem gesucht wird
	 * @return Steht der Spieler bereits in der Score-Datei?
	 */
	protected boolean doesPlayerExist(String playerName) {
		try {
			scanner = new Scanner(highscoreFile);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		boolean exists = false;
		while(exists == false && scanner.hasNextLine()) {
			String str = scanner.nextLine();
			
			if(str.startsWith(playerName+scoreSeparator)) {
				exists = true;
			}
		}
		return exists;
	}
	
	/**
	 * Erstellt aus der ArrayList, die die Namen und Punkte speichert,
	 * eine TreeMap mit den richtigen Key-Value-Paaren, wie es in
	 * der Score-Datei steht.
	 * @return Daten der Score-Datei als TreeMap
	 */
	private TreeMap<String, Integer> scoreMap() {
		TreeMap<String, Integer> map = new TreeMap<>();
		
		ArrayList<String> h = highscoreToArrayList();
		for(int i = 0; i < h.size(); i++) {
			map.put(h.get(i), Integer.parseInt(h.get(i+1)));
			i++;
		}
		
		return map;
	}
	
	/**
	 * Liest alle Punktzahlen aus dem Highscore-File
	 * und erstellt eine absteigend sortierte Highscore-Liste.
	 * Format: "spieler1=punkte1,spieler2=punkte2,spieler3=punkte3,..."
	 * @return Die Highscore-Liste als String
	 */
	protected String highscore() {
		TreeMap<String, Integer> tm = scoreMap();
		
		Comparator<String> comp = new Comparator<String>() {
			public int compare(String a, String b) {
				int result = tm.get(b).compareTo(tm.get(a));
				if(result == 0)
					return 1;
				else
					return result;
			}
		};
		
		Map<String, Integer> sortedMap = new TreeMap<>(comp);
		sortedMap.putAll(tm);
		
		String str = sortedMap.toString().replaceAll(" ", "").replaceAll("}", "").substring(1);
		String[] a = str.split(",");
		String result = "";
		if(a.length < 10) {
			for(int i = 0; i < a.length; i++) {
				if(a[i].contains((CharSequence)"#")) {
					String[] temp = a[i].split("=");
					result += a[i].split("#")[0] + "=" + temp[1] + ",";
				} else {
					result += a[i] + ",";
				}
			}
		} else {
			for(int i = 0; i < 10; i++) {
				if(a[i].contains((CharSequence)"#")) {
					String[] temp = a[i].split("=");
					result += a[i].split("#")[0] + "=" + temp[1] + ",";
				} else {
					result += a[i] + ",";
				}
			}
		}
		return result;
	}

	/**
	 * Liest alles aus der Score-Datei aus
	 * und fügt die Daten in eine ArrayList für Strings.
	 * @return die Daten der Score-Datei in einer ArrayList für String
	 */
	private ArrayList<String> highscoreToArrayList() {
		try {
			scanner = new Scanner(highscoreFile);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String line = "";
		ArrayList<String> result = new ArrayList<>();
		
		while(scanner.hasNextLine()) {
			line = scanner.nextLine();
			String[] s = line.split(scoreSeparator);
			result.add(s[0]);
			result.add(s[1]);
		}
		
		return result;
	}
	
	/**
	 * Ausgabe der Fehlermeldungen.
	 * @param e der aufgetretene Fehler
	 */
	private void error(String e) {
		if(e.equals("set")) {
			System.err.println("Fehler beim Setzen der Punkte! Unzulässiger Playerindex (1 oder 2 erlaubt)!");
		} else if(e.equals("combo")) {
			System.err.println("Fehler bei Aktualisierung der Combos! Unzulässiger Playerindex (1 oder 2 erlaubt)!");
		} else if(e.equals("get")) {
			System.err.println("Fehler beim getten der Punkte! Unzulässiger Playerindex (1 oder 2 erlaubt)!");
		} else {
			System.err.println("Fehler! Unzulässiger Playerindex (1 oder 2 erlaubt)!");
		}
	}
	
	/**
	 * Löscht alle Daten, die nicht im Highscore stehen,
	 * und somit nicht mehr benötigt werden.
	 * 
	 * Noch nicht vollständig und korrekt implementiert,
	 * daher noch nicht zur Benutzung geeignet.
	 */
	private void cleanHighscoreFile() {
		TreeMap<String, Integer> tm = scoreMap();
		
		Comparator<String> comp = new Comparator<String>() {
			public int compare(String a, String b) {
				int result = tm.get(b).compareTo(tm.get(a));
				if(result == 0)
					return 1;
				else
					return result;
			}
		};
		
		Map<String, Integer> sortedMap = new TreeMap<>(comp);
		sortedMap.putAll(tm);
		
		String str = sortedMap.toString().replaceAll(" ", "").replaceAll("}", "").substring(1);
		String[] a = str.split(",");
		String result = "";
		if(a.length < 10) {
			for(int i = 0; i < a.length; i++) {
				result += a[i] + ",";
			}
		} else {
			for(int i = 0; i < 10; i++) {
				result += a[i] + ",";
			}
		}
		saveload.writeFile(highscoreFile, result.replaceAll(",", "\n"));
	}
	
	/**
	 * Wenn das Spiel vorbei ist, wird diese Methode ausgeführt,
	 * damit die Punkte in der Datei abgespeichert werden.
	 * @param gameName 
	 * @param playerName
	 * @param opponentName
	 */
	protected void saveScoreToFile(String playerName, String opponentName) {
		addPlayerIntoHighscore(playerName, scorePlayer1);
		addPlayerIntoHighscore(opponentName, scorePlayer2);
		cleanHighscoreFile();
	}
	
	/**
	 * Der Wert der Combo wird zurückgegeben,
	 * damit die GUI darstellen kann, welche Combo der Spieler
	 * aktuell erreicht hat.
	 * @param playerIndex
	 * @return
	 */
	protected double getComboValue(int playerIndex) {
		if(playerIndex == 1) {
			return comboPlayer1;
		} else if(playerIndex == 2) {
			return comboPlayer2;
		} else {
			error("");
			return 0.0;
		}
	}
	
	/**
	 * main method
	 * @param args arguments
	 */
	public static void main(String[] args) {
		Score s = new Score();
//		s.addPlayerIntoHighscore("rwgw", 456454);
//		s.addPlayerIntoHighscore("idkgdfdfggmg", 43554353);
//		s.addPlayerIntoHighscore("Test", 123);
//		s.addPlayerIntoHighscore("hallo", 0);
//		System.out.println(s.getScore("Test"));
//		s.updateScoreOnEvent("Test", 'u');
//		s.updateScoreOnEvent("hallo", 's');
//		s.updateScore("Test", 666);
		//System.out.println(s.doesPlayerExist("fsdfvfdv"));
		//System.out.println(s.doesPlayerExist("hallo"));
//		System.out.println(s.doesPlayerExist("Test"));
//		System.out.println(s.doesPlayerExist("tretrhtbhgf"));
//		s.addPlayerIntoHighscore("TestName");
//		s.setScore("TestName", 3434543);
		
//		s.addPlayerIntoHighscore("Hallo");
//		s.updateScoreOnEvent("Hallo", 's');
//		System.out.println(s.getScore("Test"));
//		s.addPlayerIntoHighscore("TestSpieler365457");
//		s.addPlayerIntoHighscore("randomguy");
		
//		s.addPlayerIntoHighscore("Josef");
//		s.addPlayerIntoHighscore("Dieter");
//		s.setScore("Josef", 345);
//		s.setScore("Dieter", 1023);
//		s.setScore("TestSpieler365457", 54455);
//		s.setScore("randomguy", 83);
		
//		System.out.println(s.highscore());
		
//		s.highscoreTest();
		 
//		s.addPlayerIntoHighscore("R2D2");
/*		System.out.println(s.getScore("R2D2"));
		s.updateScoreOnEvent("testspiel", "R2D2", 'h');
		System.out.println(s.getScore("R2D2"));
		s.updateScoreOnEvent("testspiel", "R2D2", 'h');
		System.out.println(s.getScore("R2D2"));
		s.updateScoreOnEvent("testspiel", "R2D2", 's');
		System.out.println(s.getScore("R2D2"));*/
		
/*		System.out.println(s.comboPlayer1);
		System.out.println(s.comboPlayer2);
		s.combo("testspiel", "R2D2", 'h');
		System.out.println(s.comboPlayer1);
		System.out.println(s.comboPlayer2); */
		
//		s.highscoreTest();
		
//		System.out.println(s.getScore("C3PO"));
//		s.setScore("C3PO", 123);
//		System.out.println(s.getScore("C3PO"));
//		System.out.println(s.getScore("Dieter"));
//		System.out.println(s.findPlayer(0));
		
//		s.addPlayerIntoHighscore("testSpieler123");
		
/*		TreeMap<String, Integer> tm = s.scoreMap();
		System.out.println(tm.toString());*/
		
//		System.out.println(s.highscore());
//		s.addPlayerIntoHighscore("test");
		
//		s.setScore("Marc", 345);
//		s.cleanHighscoreFile();
		
		s.setScore(1, 'h');
		s.setScore(1, 'h');
		s.setScore(1, 'h');
		s.setScore(1, 's');
		s.setScore(2, 'h');
		s.setScore(2, 'h');
		s.setScore(2, 'h');
		s.saveScoreToFile("ABC", "DEF");
		
	}

}
