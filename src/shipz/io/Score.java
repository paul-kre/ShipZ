package shipz.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

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
	private int comboPlayer1;
	/** Zählt die Combos des zweiten Spielers */
	private int comboPlayer2;
	/** Trennzeichen zwischen Namen und Punkte */
	private String scoreSeparator = "=";
	
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
	}
	
	// IM
	/**
	 * Setzt die Punktzahl eines bestimmten Spielers zu einem bestimmten Event.
	 * @param playerName Name des Spielers
	 * @param event Events: u für undo, h für hit, s für sink <i>(weitere folgen eventuell. Das liegt nicht in meiner Hand)</i>
	 */
	protected void updateScoreOnEvent(String gameName, String playerName, char event) {
		byte b = saveload.getPlayersNumber(gameName, playerName);
		
		switch(event) {
		case 'u':
			setScore(playerName, getScore(playerName)-30);
			break;
		case 'h':
			combo(gameName, playerName, event);
				if(b == 1) setScore(playerName, getScore(playerName)+50*comboPlayer1);
				else if(b == 2) setScore(playerName, getScore(playerName)+50*comboPlayer2);
			break;
		case 's':
			combo(gameName, playerName, event);
				if(b == 1) setScore(playerName, getScore(playerName)+300*comboPlayer1);
				else if(b == 2) setScore(playerName, getScore(playerName)+300*comboPlayer2);
			break;
		default:
			System.err.println("Fehler beim Score-Update!");
		}
	}
	
	/**
	 * Methode, die die Combos verwaltet,
	 * den Combo-Counter hochzählt.
	 * @param playerName Spielername
	 */
	private void combo(String gameName, String playerName, char event) {
		byte b = saveload.getPlayersNumber(gameName, playerName);
		
		if(b == 1) {
			if(event == 'h') {
				comboPlayer1 += 0.1;
				comboPlayer2 = 1;
			} else if (event == 's'){
				comboPlayer1 += 0.5;
				comboPlayer2 = 1;
			}
		} else if(b == 2) {
			if(event == 'h') {
				comboPlayer2 += 0.1;
				comboPlayer1 = 1;
			} else if (event == 's'){
				comboPlayer2 += 0.5;
				comboPlayer1 = 1;
			}
		} else {
			System.err.println("Fehler bei Aktualisierung der Combos!");
		}
		
	}
	
	/**
	 * Ändert die Punkte eines Spielers playerName zum Parameter score.
	 * @param playerName Der Spieler dessen Punkte geändert werden sollen.
	 * @param score Die Punkte, die der Spieler bekommen soll.
	 */
	private void setScore(String playerName, int score) {
		String s = readHighscoreFile();
		s = s.replaceAll(playerName+scoreSeparator+getScore(playerName), playerName+scoreSeparator+score);
		saveload.writeFile(highscoreFile, s);
	}
	
	/**
	 * Liest die Punkte eines bestimmten Spielers aus der Highscore-Datei
	 * und gibt diese zurück.
	 * @param playerName gewünschter Spieler
	 * @return Die Punktzahl des Spielers
	 */
	private int getScore(String playerName) {
		String line = saveload.searchLine(highscoreFile, playerName);
		return Integer.parseInt(line.split(scoreSeparator)[1]);
	}
	
	/**
	 * Mit der Methode aus der SaveLoad-Klasse wird der aktuelle Highscore als {@link String}
	 * in die Instanz-Variable << highscore >> geschrieben.
	 */
	protected String readHighscoreFile() {
		return saveload.readFile(highscoreFile);
	}
	
	/**
	 * Ein bestimmter Spieler wird in die Highscore-Liste eingefügt.
	 * @param playerName Der Spieler, der eingefügt werden soll.
	 */
	protected void addPlayerIntoHighscore(String playerName) {
		if(doesPlayerExist(playerName) == false) {
			saveload.writeFile(highscoreFile, saveload.readFile(highscoreFile)+playerName+scoreSeparator+"0");
		} else {
			System.err.println("Fehler beim Einfügen in die Score-Datei! Dieser Spieler existiert bereits!");
		}
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
	 * Liest alle Punktzahlen aus dem Highscore-File
	 * und erstellt eine absteigend sortierte Highscore-Liste.
	 * 
	 * MUSS ÜBERARBEITET WERDEN.
	 * @return Die Highscore-Liste als String
	 */
	protected String highscore() {
		String[] h = saveload.readFile(highscoreFile).replaceAll("\n", "").split(scoreSeparator);
		String str = "";
		ArrayList<Integer> score = new ArrayList<Integer>();
		
		for(int i = 0; i < h.length/2; i++) {
			score.add(Integer.parseInt(h[i*2+1])); // liste füllen
		}
		
		Collections.sort(score, Collections.reverseOrder()); // absteigend sortieren
		
		for(int i = 0; i < score.size(); i++) {
			str += findPlayer(score.get(i)) + scoreSeparator + score.get(i) + ",";
		}
		
		// FORMAT des fertigen Highscore-Strings
		// "nameErster:punkteErster,
		//  nameZweiter:punkteZweiter,
		//  nameDritter:punkteDritter,"
		
		return str;
	}

	/**
	 * Nur eine
	 * (sehr beschissene)
	 * Übergangslösung.
	 * Funktioniert nicht ganz, für den Fall, dass zwei (oder mehr) Spieler die selbe Punktzahl haben,
	 * wird nur ein Spieler in den Highscore getragen, nicht alle.
	 * 
	 * MUSS ÜBERARBEITET WERDEN.
	 * @param score
	 * @return
	 */
	private String findPlayer(int score) {
		String str = "";
		String[] file = readHighscoreFile().replaceAll("\n", "").split(scoreSeparator);
		
		for(int i = 0; i < file.length-1; i++) {
			if(file[i+1].equalsIgnoreCase(""+score)) {
				str = file[i];
			}
		}
		
		return str;
	}
	
	/**
	 * Dient nur zum Test der Darstellung einer fertigen Highscoreliste.
	 * Die Darstellung muss später von der Haupt- und GUI-Klasse implementiert werden.
	 */
	@Deprecated
	private void highscoreTest() {
		// so könnte man aus dem erstellten String dann eine Highscoreliste darstellen
		
		String[] h = highscore().split(",");
		String[] p = null;
		
		System.out.println("No.\tPlayer Name\t\t\tScore");
		for(int i = 0; i < h.length; i++) {
			p = h[i].split(scoreSeparator);
			System.out.println(i+1+".\t" + p[0] + "\t\t\t" + p[1]);
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
		
		s.addPlayerIntoHighscore("testSpieler123");
		
	}

}
