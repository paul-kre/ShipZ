package shipz.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

/**
 * Diese Klasse verwaltet die Speicherprozesse der anderen Klassen.
 * @author Florian Osterberg
 */
public class SaveLoad {

	// IV
	/** Datei, in der die einzelnen Spielst�nde gespeichert werden. */
	private File file;
	/** BufferedWriter, mit dem in die Dateien geschrieben wird. */
	private BufferedWriter writer;
	/** Scanner, der eine Datei liest. */
	private Scanner scanner;
	/** Writer, der in die XML-Datei schreibt. */
	private XMLOutputter xmlOutput;
	/** Objekt, welches das XML-Dokument speichert */
	private Document document;
	/** Wurzelelement des XML-Dokuments */
	private Element root;
	/** SAXBuilder-Objekt, der das ausgelesene XML in das Document-Objekt schreibt. */
	private SAXBuilder builder;
	
	// Konstruktor
	/**
	 * Konstruktor der Klasse, der das File-Objekt initialisiert und diesem einen Dateipfad zuordnet.
	 * Au�erdem wird die Grundstruktur des XML-Dokuments erstellt.
	 */
	public SaveLoad() {
		file = new File(fileDirectory() + File.separator + "saves.xml");
		makeDirectory(file);
		document = new Document();
		root = new Element("saves");
		document.setContent(root);
		builder = new SAXBuilder();
		xmlOutput = new XMLOutputter();
	}
	
	// IM
	/**
	 * Methode, die mit allen ben�tigten Informationen ein neues Spiel im XML erstellt.
	 * @param gameName Dateiname f�r den Spielstand
	 * @param playerName Name des ersten Spielers
	 * @param opponentName Name des zweiten Spielers bzw. des Gegners
	 * @param boardPlayer1 Das gesamte Feld des ersten Spielers als {@link String} gespeichert. Au�erdem wird die Feldgr��e gespeichert.
	 * @param boardPlayer2 Das gesamte Feld des zweiten Spielers als {@link String} gespeichert. Au�erdem wird die Feldgr��e gespeichert.
	 * @param boardsize Gr��e des Feldes. Format: "H�he,Breite"
	 * @param activePlayer Spieler der gerade am Zug ist
	 * @param preferences Einstellungen des Spiels, die am Anfang gesetzt wurden
	 * @param mirrorField Spielfeld der KI
	 */
	protected void saveGame(String gameName, String playerName, String opponentName, String boardPlayerOne, String boardPlayerTwo, int boardsize, int activePlayer, String preferences, String mirrorFieldOne, String mirrorFieldTwo) {
		updateXML();
		if(!doesGameExist(gameName)) {
			Element gameElement = new Element("game");
			gameElement.addContent(new Element("gameName").setText(gameName));
			gameElement.addContent(new Element("time").setText(timestamp()));
			gameElement.addContent(new Element("playerName").setText(playerName));
			gameElement.addContent(new Element("opponentName").setText(opponentName));
			gameElement.addContent(new Element("boardPlayerOne").setText(boardPlayerOne));
			gameElement.addContent(new Element("boardPlayerTwo").setText(boardPlayerTwo));
			gameElement.addContent(new Element("boardsize").setText(boardsize+""));
			gameElement.addContent(new Element("draws").setText("null"));
			gameElement.addContent(new Element("activePlayer").setText(activePlayer+""));
			gameElement.addContent(new Element("preferences").setText(preferences));
			gameElement.addContent(new Element("mirrorFieldOne").setText(mirrorFieldOne));
			gameElement.addContent(new Element("mirrorFieldTwo").setText(mirrorFieldTwo));
			
			root.addContent(gameElement);
			document.setContent(root);
			writeXML();
		} else {
			setNode(gameName, "activePlayer", activePlayer+"");
			setNode(gameName, "time", timestamp());
			setNode(gameName, "boardPlayerOne", boardPlayerOne);
			setNode(gameName, "boardPlayerTwo", boardPlayerTwo);
			setNode(gameName, "playerName", playerName);
			setNode(gameName, "opponentName", opponentName);
			setNode(gameName, "boardsize", boardsize+"");
			setNode(gameName, "preferences", preferences);
			setNode(gameName, "mirrorFieldOne", mirrorFieldOne);
			setNode(gameName, "mirrorFieldTwo", mirrorFieldTwo);
		}
	}
	
	/**
	 * Falls die Ordner und Dateien noch nicht vorhanden sind, werden sie erstellt.
	 * @param file Datei, die erstellt werden soll.
	 */
	protected void makeDirectory(File file) {
		if(!(file.exists())) {
			try{
				new File(fileDirectory()).mkdir();
			} catch (SecurityException x) {
				x.printStackTrace();
			}
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Diese Methode gibt den Inhalt eines Knotens eines Spielstands
	 * als String zur�ck.
	 * @param gameName Name des Spiels
	 * @param node Knoten dessen Text zur�ckgegeben werden soll
	 * @return Inhalt des Knotens als String
	 */
	private String getNode(String gameName, String node) {
		updateXML();
		String str = "";
		
		List<Element> list = root.getChildren();
		for(Element e : list) {
			if(e.getChild("gameName").getText().equals(gameName)) {
				str = e.getChild(node).getText();
			}
		}
		
		return str;
	}
	
	/**
	 * Mit dieser Methode l�sst sich ein einzelner Knoten eines Spielstands bearbeiten.
	 * @param gameName Name des Spielstands
	 * @param node zu bearbeitender Knoten
	 * @param text Text, der im Knoten eingef�gt werden soll
	 */
	private void setNode(String gameName, String node, String text) {
		updateXML();
		List<Element> list = root.getChildren();
		for(Element e : list) {
			if(e.getChild("gameName").getText().equals(gameName)) {
				e.getChild(node).setText(text);
			}
		}
		document.setContent(root);
		writeXML();
	}
	
	/**
	 * Das XML-Dokument wird ausgelesen und die Instanz-Variablen,
	 * die Wurzel-Element und das Dokument speichern,
	 * werden somit aktualisiert.
	 */
	private void updateXML() {
		if(root.getChildren() != new Element("saves").getChildren()) {
			try {
				InputStream inputStream = new FileInputStream(file);
				Reader reader = new InputStreamReader(inputStream, "UTF-8");
				InputSource is = new InputSource(reader);
				document = builder.build(is);
				root = document.detachRootElement();
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("Fehler beim Bearbeiten des XML-Dokuments!");
		}
	}
	
	/**
	 * Das aktuell in den Instanz-Variablen abgespeicherte XML-Dokument
	 * wird in die XML-Datei geschrieben.
	 */
	private void writeXML() {
		try {
			FileWriter filewriter = new FileWriter(file);
			Format format = Format.getPrettyFormat();
			format.setEncoding("UTF-8");
			xmlOutput.setFormat(format);
			xmlOutput.output(document, filewriter);
			filewriter.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * L�scht einen bestimmten Spielstand aus der Datei.
	 * @param gameName Name des Spielstands
	 */
	protected void deleteGame(String gameName) {
		updateXML();
		if(doesGameExist(gameName)) {
			List<Element> list = root.getChildren();
			Element temp = null;
			for(Element e : list) {
				if(e.getChild("gameName").getText().equals(gameName)) {
					temp = e;
				}
			}
			root.removeContent(temp);
			document.setContent(root);
			writeXML();
		} else {
			throw new RuntimeException("Fehler beim L�schen des Spielstands! Dieser Spielstand existiert nicht!");
		}
	}
	
	/**
	 * Gibt die Namen aller Spielst�nde als {@link String} zur�ck.
	 * Dies wird f�r die Auflistung aller Spielst�nde wichtig sein.
	 * Da die Weitergabe von Arrays nicht erlaubt ist,
	 * muss die Game-Klasse selbst aus dem String ein Array machen.
	 * Dies geht ganz einfach mit 
	 * getAllGameNames().split(",")
	 * @return Die Namen aller Spielst�nde als {@link String}
	 */
	protected String getAllGameNames() {
		String str = "";
		updateXML();
		
		List<Element> list = root.getChildren();
		for(Element e : list) {
			str += e.getChild("gameName").getText() + ",";
		}
		
		return str;
	}
	
	/**
	 * Diese Methode l�dt aus dem gespeicherten Spielstand das gespeicherte Spielfeld des ersten Spielers heraus.
	 * Dieses Spielfeld wird dann als {@link String} zur�ckgegeben.
	 * @param gameName Dateiname zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	protected String getBoardPlayerOne(String gameName) {
		return getNode(gameName, "boardPlayerOne");
	}
	
	/**
	 * Diese Methode l�dt aus dem gespeicherten Spielstand das gespeicherte Spielfeld des zweiten Spielers heraus.
	 * Dieses Spielfeld wird dann als {@link String} zur�ckgegeben.
	 * @param gameName Dateiname zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	protected String getBoardPlayerTwo(String gameName) {
		return getNode(gameName, "boardPlayerTwo");
	}
	
	/**
	 * Gibt den Spielernamen eines Spielstands zur�ck.
	 * @param gameName der gew�nschte Spielstand
	 * @return Spielername eines Spielstand
	 */
	protected String getPlayerName(String gameName) {
		return getNode(gameName, "playerName");
	}
	
	/**
	 * Gibt den Namen des Gegners eines bestimmten Spielstands zur�ck.
	 * @param gameName der gew�nschte Spielstand
	 * @return Namen des Gegners eines bestimmten Spielstands
	 */
	protected String getOpponentName(String gameName) {
		return getNode(gameName, "opponentName");
	}
	
	/**
	 * Gibt die Feldgr��e eines bestimmten Spielstands zur�ck.
	 * @param gameName der gew�nschte Spielstand
	 * @return Feldgr��e eines bestimmten Spielstands als {@link String}
	 */
	protected int getBoardsize(String gameName) {
		return Integer.parseInt(getNode(gameName, "boardsize"));
	}
	
	/**
	 * Gibt den String aus einem Spielstand zur�ck, der die Spielz�ge speichert.
	 * @param gameName der gew�nschte Spielstand
	 * @return Die Spielz�ge als {@link String}
	 */
	protected String getDraws(String gameName) {
		return getNode(gameName, "draws");
	}
	
	/**
	 * Liest den aktuellen Spieler eines Spiels aus der Datei aus und gibt ihn zur�ck.
	 * @param gameName Name des Spielstands
	 * @return der aktive Spieler
	 */
	protected int getActivePlayer(String gameName) {
		return Integer.parseInt(getNode(gameName, "activePlayer"));
	}
	
	/**
	 * Gibt die gespeicherte Uhrzeit eines Spielstands zur�ck.
	 * @param gameName der gew�nschte Spielstand
	 * @return die gespeicherte Uhrzeit als {@link String}
	 */
	protected String getTime(String gameName) {
		return getNode(gameName, "time");
	}
	
	/**
	 * Gibt die Einstellungen eines Spielstands zur�ck.
	 * @param gameName Name des Spielstands
	 * @return Einstellungen als String
	 */
	protected String getPreferences(String gameName) {
		return getNode(gameName, "preferences");
	}
	
	/**
	 * Gibt das Spielfeld für die KI zurück.
	 * @param gameName Name des Spielstands
	 * @return Spielfeld für die KI als String
	 */
	protected String getMirrorFieldOne(String gameName) {
		return getNode(gameName, "mirrorFieldOne");
	}
	
	/**
	 * Gibt das Spielfeld für die KI zurück.
	 * @param gameName Name des Spielstands
	 * @return Spielfeld für die KI als String
	 */
	protected String getMirrorFieldTwo(String gameName) {
		return getNode(gameName, "mirrorFieldTwo");
	}
	
	/**
	 * Speichert den Spielverlauf in einem Spielstand.
	 * @param gameName der Spielstand
	 * @param draws der neue Spielverlauf des ersten Spielers
	 */
	protected void setDraws(String gameName, String draws) {
		setNode(gameName, "draws", draws);
	}
	
	/**
	 * Überprüft, ob ein Spielstand mit bestimmtem Namen vorhanden ist.
	 * @param gameName der gew�nschte Spielstand
	 * @return Ist der Spielstand vorhanden?
	 */
	private boolean doesGameExist(String gameName) {
		updateXML();
		boolean doesGameExist = false;
		List<Element> list = root.getChildren();
		
		for(Element e : list) {
			if(e.getChild("gameName").getText().equals(gameName)) {
				doesGameExist = true;
			}
		}
		
		return doesGameExist;
	}
	
	/**
	 * Sucht in einer Datei nach einer bestimmten Zeile.
	 * Es wird mit nach einem Prefix gesucht.
	 * Wenn die Zeile gefunden wurde, wird sie als String zur�ckgegeben.
	 * @param file Die Datei, in der gesucht werden soll.
	 * @param prefix Prefix der Zeile, nach der gesucht werden soll.
	 * @return Die gefundene Zeile
	 */
	protected String searchLine(File file, String prefix) {
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String line = scanner.nextLine();
		while(!(line.startsWith(prefix))) {
			line = scanner.nextLine(); // geht so lange weiter, bis er die Zeile gefunden hat
		}
		
		return line;
	}
	
	/**
	 * Liest aus einer Datei den gesamten Inhalt aus
	 * und gibt ihn als {@link String} zur�ck.
	 * @param file Die gew�nschte Datei.
	 * @return Inhalt der Datei als {@link String}.
	 */
	protected String readFile(File file) {
		String s = "";
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(scanner.hasNextLine()) {
			s += scanner.nextLine() + "\n";
		}
		return s;
	}
	
	/**
	 * �berschreibt eine Datei komplett mit einem String.
	 * @param file die zu �berschreibende Datei.
	 * @param str der {@link String}, der in die Datei geschrieben werden soll.
	 */
	protected void writeFile(File file, String str) {
		try {
			writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
			writer.write(str);
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gibt den Dateipfad zur�ck, in dem Dateien des Spiels gespeichert werden.
	 * Der Dateipfad ist auf Windows in der Regel:
	 * C:\Users\<i>"Benutzer"</i>\Documents\shipZ
	 * @return Pfad, in dem die Dateien gespeichert werden
	 */
	protected String fileDirectory() {
		JFileChooser jf = new JFileChooser();
		return jf.getFileSystemView().getDefaultDirectory().toString() + File.separator + "shipZ";
	}
	
	/**
	 * Methode, die die aktuelle Zeit berechnet und als String zur�ckgibt. <br>
	 * Format: <b>dd.MM.y_HH:mm:ss</b> <br>
	 * Beispiel: <i>05.07.2016_18:12:23</i>
	 * @return die aktuelle Zeit als String
	 */
	protected String timestamp() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.y_HH:mm:ss");
		return sdf.format(c.getTime());
	}
	
}