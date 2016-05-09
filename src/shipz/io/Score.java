package shipz.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
	/** Writer, mit dem Dateien geschrieben werden können. */
	private BufferedWriter writer;
	/** Scanner, der Dateien ausliest. */
	private Scanner scanner;
	/** Zählt die Combos des ersten Spielers */
	private int comboPlayer1;
	/** Zählt die Combos des zweiten Spielers */
	private int comboPlayer2;
	
	// Konstruktor
	/**
	 * Konstruktor von Score.
	 */
	public Score() {
		saveload = new SaveLoad();
		highscoreFile = new File(saveload.userDirectory() + File.separator + "shipZ" + File.separator + "highscore.shipz");
		saveload.makeDirectory(highscoreFile);
	}
	
	// IM
	/**
	 * Setzt die Punktzahl eines bestimmten Spielers zu einem bestimmten Event.
	 * @param playerName Name des Spielers
	 * @param event Events: u für undo, h für hit, s für sink (weitere folgen)
	 */
	protected void updateScoreOnEvent(String playerName, char event) {
		switch(event) {
			case 'u':	setScore(playerName, getScore(playerName)-30); break;
			case 'h':	setScore(playerName, getScore(playerName)+50); break; 
			case 's':	setScore(playerName, getScore(playerName)+300); break;
			default:	System.err.println("Fehler beim Score-Update!");
		}
	}
	
	/**
	 * Ändert die Punkte eines Spielers playerName zum Parameter score.
	 * @param playerName Der Spieler dessen Punkte geändert werden sollen.
	 * @param score Die Punkte, die der Spieler bekommen soll.
	 */
	private void setScore(String playerName, int score) {
		String s = saveload.readFile(highscoreFile);
		s = s.replaceAll(playerName+":"+getScore(playerName)+":", playerName+":"+score+":");
		try {
			writer = new BufferedWriter(new FileWriter(highscoreFile.getAbsolutePath()));
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Liest die Punkte eines bestimmten Spielers aus der Highscore-Datei
	 * und gibt diese zurück.
	 * @param playerName gewünschter Spieler
	 * @return Die Punktzahl des Spielers
	 */
	private int getScore(String playerName) {
		String s = "";
		try {
			scanner = new Scanner(highscoreFile);
		} catch (FileNotFoundException x) {
			x.printStackTrace();
		}
		while(scanner.hasNextLine()) {
			s += scanner.nextLine();
		}
		
		String[] a = s.split(":");
		
		int c = 0;
		for(int i = 0; i < a.length; i++) {
			if(a[i].equalsIgnoreCase(playerName)) {
				c = Integer.parseInt(a[i+1]);
			}
		}
		return c;
	}
	
	/**
	 * Mit der Methode aus der SaveLoad-Klasse wird der aktuelle Highscore als String
	 * in die Instanz-Variable << highscore >> geschrieben.
	 */
	protected String readHighscore() {
		String highscore = "";
		
		try {
			scanner = new Scanner(highscoreFile);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(scanner.hasNextLine()) {
			highscore += scanner.nextLine() + "\n";
		}
		
		return highscore;
	}
	
	/**
	 * Ein bestimmter Spieler wird in die Highscore-Liste eingefügt.
	 * @param playerName Der Spieler, der eingefügt werden soll.
	 * @param score Die Punkte des Spielers
	 */
	protected void addPlayerIntoHighscore(String playerName) {
		if(!(highscoreFile.exists())) {
			try {
				highscoreFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(doesPlayerExist(playerName) == false) {
			saveload.writeFile(highscoreFile, saveload.readFile(highscoreFile)+playerName+":0:");
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
		boolean exists = false;
		String s = saveload.readFile(highscoreFile).replaceAll("\n", "");
		String[] a = s.split(":");
		for(int i = 0; i < a.length; i++) {
			if(a[i].equalsIgnoreCase(playerName)) {
				exists = true;
			}
		}
		return exists;
	}
	
	/**
	 * Leert die gesamte Highscore-Datei.
	 */
	protected void clearHighscoreFile() {
		try {
			writer = new BufferedWriter(new FileWriter(highscoreFile.getAbsolutePath()));
			writer.write("");
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
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
		System.out.println(s.doesPlayerExist("Test"));
//		s.addPlayerIntoHighscore("TestName");
//		s.setScore("TestName", 3434543);
		
//		s.addPlayerIntoHighscore("Hallo");
		s.updateScoreOnEvent("Hallo", 's');
		System.out.println(s.getScore("Test"));
		s.addPlayerIntoHighscore("TestSpieler365457");
	}

}
