package shipz.gamemode;

import java.util.concurrent.ThreadLocalRandom;

import shipz.MirrorField;
import shipz.Player;

/**
 * Spielverwaltung
 * @author Max
 * @version	0.1
 */
public class TempKiGame{

	//IV 
	/** Spielfeld des 1. Spielers */
	private char[][] board1;
	/** Spielfeld des 2. Spielers */
	private char[][] board2;
	/** Verweis auf den 1. Spieler */
	private Player player1;
	/** Verweis auf den 2. Spieler */
	private Player player2;
	/** Netzwerkverbindung */
	//private Network network;
	/** grafische Nutzeroberfl�che */
	//private GUI gui;
	/** Spielstandverwaltung */
	//private FileStream filestream;
	



	//Constructor
	/**
	 * erstellt ein neues Spiel mit leeren Feldern
	 * @param widht		Feldbreite
	 * @param height	Feldh�he
	 */
	private TempKiGame(int width, int height) {
		board1 = new char[width][height];
		board2 = new char[width][height];
		initiateBoard(board1);
		initiateBoard(board2);
	}
	
	
	//Methoden
	/**
	 * startet ein neues oder geladenes Spiel mit den ausgew�hlten Optionen
	 */
	private void startGame(){};
	
	/**
	 * setzt alle Zellen eines Felds auf Wasser
	 * @param board	zu f�llendes Feld
	 */
	private void initiateBoard(char[][] board) {
		//1. Z�hler
		int y;
		//2. Z�hler
		int x;
		//doppelte Schleife f�r Durchlauf durch alle Felder
		for(y=0; y<board.length; y++) {
			for(x=0; x<board[y].length; x++) {
				board[y][x] = 'w';
			}//Ende ySchleife
		}//Ende xSchleife
	}
	
	/**
	 * �berpr�ft das Ende des Spiels und leitet eventuelle Benachrichtigungen ein
	 * @return gibt an, ob das Spiel beendet ist
	 */
	private boolean checkGameOver() {return false;}
	
	/** 
	 * �berpr�ft die �bergebenen Koordinaten auf Schiffelemente und ruft eventuell sink() auf
	 * @param x 	Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return 0	Wasser getroffen
	 * @return 1	Schiff getroffen
	 * @return 2	Schiff versenkt
	 */
	public byte checkTile(int x, int y) {
		byte r = 0;
		if(board1[y][x] == 'x') {
			r = 1;
			if (sink(x, y, board1)) {
				r = 2;
			}
			//System.out.println("Es wurde ein Schiffelement zerst�rt");
			return r;
		}
		else {
			//System.out.println("Es wurde kein Schiffelement zerst�rt");
			return r;
		}
	}
	
	/**
	 * setzt die angegebene Zelle auf ein zerst�rtes Schiffelement
	 * @param x		Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return		gibt an, ob das gesamte Schiff versenkt wurde
	 */
	private boolean sink(int x, int y, char[][] board) {
		board[y][x] = 'z';
		if (checkShipDestroyed(x, y, board)) {
			//System.out.println("Es wurde ein gesamtes Schiff zerst�rt");
		}
		return (checkShipDestroyed(x, y, board));
	}
	
	/**
	 * pr�ft den Gesamtzustand des Schiffs
	 * @param x		Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return		gibt an, ob das gesamte Schiff versenkt wurde
	 */
	private boolean checkShipDestroyed(int x, int y, char[][] board) {
		return (checkShipDestroyedUp(x, y, board) && checkShipDestroyedRight(x, y, board) && checkShipDestroyedDown(x, y, board) && checkShipDestroyedLeft(x, y, board)); 
	}
	
	/**
	 * pr�ft den Gesamtzustand des Schiffs nach oben
	 * @param x		Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return		gibt an, ob das gesamte Schiff versenkt wurde
	 */
	private boolean checkShipDestroyedUp(int x, int y, char[][] board) {
		if(y-1 >= 0) {
			if(board[y-1][x] == 'w') {						//Wasser auf angrenzendem Feld
				return true;								//Schiff wurde vielleicht versenkt
			}
			else if(board[y-1][x] == 'x') {					//Schiffelement auf angrenzendem Feld
				return false;								//Schiff wurde nicht versenkt
			}
			else {											//zerst�rtes Schiffelement auf angrenzendem Feld
				return checkShipDestroyedUp(x, y-1, board);	//weitere Pr�fung vom angrenzenden Feld
			}
		}
		else {
			return true;
		}
	}
	
	/**
	 * pr�ft den Gesamtzustand des Schiffs nach rechts
	 * @param x		Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return		gibt an, ob das gesamte Schiff versenkt wurde
	 */
	private boolean checkShipDestroyedRight(int x, int y, char[][] board) {
		if(x+1 < board[y].length) {
			if(board[y][x+1] == 'w') {							//Wasser auf angrenzendem Feld
				return true;									//Schiff wurde vielleicht versenkt
			}
			else if(board[y][x+1] == 'x') {						//Schiffelement auf angrenzendem Feld
				return false;									//Schiff wurde nicht versenkt
			}
			else {												//zerst�rtes Schiffelement auf angrenzendem Feld
				return checkShipDestroyedRight(x+1, y, board);	//weitere Pr�fung vom angrenzenden Feld
			}
		}
		else {
			return true;
		}
	}
	
	/**
	 * pr�ft den Gesamtzustand des Schiffs nach unten
	 * @param x		Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return		gibt an, ob das gesamte Schiff versenkt wurde
	 */
	private boolean checkShipDestroyedDown(int x, int y, char[][] board) {
		if(y+1 < board.length) {
			if(board[y+1][x] == 'w') {						//Wasser auf angrenzendem Feld
				return true;								//Schiff wurde vielleicht versenkt
			}
			else if(board[y+1][x] == 'x') {					//Schiffelement auf angrenzendem Feld
				return false;								//Schiff wurde nicht versenkt
			}
			else {											//zerst�rtes Schiffelement auf angrenzendem Feld
				return checkShipDestroyedUp(x, y+1, board);	//weitere Pr�fung vom angrenzenden Feld
			}
		}
		else {
			return true;
		}
	}
	
	/**
	 * pr�ft den Gesamtzustand des Schiffs nach links
	 * @param x		Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return		gibt an, ob das gesamte Schiff versenkt wurde
	 */
	private boolean checkShipDestroyedLeft(int x, int y, char[][] board) {
		if(x-1 >= 0) {
			if(board[y][x-1] == 'w') {							//Wasser auf angrenzendem Feld
				return true;									//Schiff wurde vielleicht versenkt
			}
			else if(board[y][x-1] == 'x') {						//Schiffelement auf angrenzendem Feld
				return false;									//Schiff wurde nicht versenkt
			}
			else {												//zerst�rtes Schiffelement auf angrenzendem Feld
				return checkShipDestroyedLeft(x-1, y, board);	//weitere Pr�fung vom angrenzenden Feld
			}
		}
		else {
			return true;
		}
	}
	
	/**
	 * z�hlt die Anzahl der Schiffe auf einem Feld
	 * @param board	zu z�hlendes Feld
	 * @return		Anzahl der Schiffe
	 */
	private int shipCount(char[][] board) {return 0;}
	
	/**
	 * gibt beide Felder hintereinander auf der Konsole aus (f�r Testzwecke)
	 */
	public void displayBoards() {
		System.out.println("Spieler 1");
		displaySingleBoard(board1);
		System.out.println();
		System.out.println("Spieler 2");
		displaySingleBoard(board2);
	}
	
	/**
	 * gibt ein Feld auf der Konsole aus (f�r Testzwecke)
	 * @param board auszugebendes Feld
	 */
	public void displaySingleBoard(char[][] board) {
		//Ausgabe der oberen Feldbeschriftung
		System.out.println("  A B C D E F G H I J");
		//1. Z�hler
		int y;
		//2. Z�hler
		int x;
		//doppelte Schleife f�r Durchlauf durch alle Felder
		for(y=0; y<board.length; y++) {
			//Ausgabe der seitlichen Feldbeschriftung
			System.out.print(y);
			for(x=0; x<board[y].length; x++) {
				//Ausgabe der einzelnen Zellen
				System.out.print(" " + board[y][x]);
			}
			//Zeilenumbruch
			System.out.println();
		}
	}
	
	/**
	 * gibt ein Spiegel auf der Konsole aus (f�r Testzwecke)
	 * @param board auszugebendes Feld
	 */
	public void displaySingleBoard(boolean[][] board) {
		//Ausgabe der oberen Feldbeschriftung
		System.out.println("  A B C D E F G H I J");
		//1. Z�hler
		int y;
		//2. Z�hler
		int x;
		//doppelte Schleife f�r Durchlauf durch alle Felder
		for(y=0; y<board.length; y++) {
			//Ausgabe der seitlichen Feldbeschriftung
			System.out.print(y);
			for(x=0; x<board[y].length; x++) {
				//Ausgabe der einzelnen Zellen
				if(board[y][x]) {
					System.out.print(" " + "1");
				}
				else {
					System.out.print(" " + "0");
				}
			}
			//Zeilenumbruch
			System.out.println();
		}
	}
	
	/**
	 * platziert alle Schiffe eines Spielers auf seinem Feld
	 * @param board	Feld
	 */
	private void placeShips(char[][] board) {
		while(placeSingleShip(ThreadLocalRandom.current().nextInt(0, board.length + 1), ThreadLocalRandom.current().nextInt(0, board[0].length + 1), 5, board)) {}
	}
	
	/**
	 * platziert ein einzelnes Schiff
	 * @param y			Koordinate
	 * @param x			Koordinate
	 * @param length	Schiffl�nge
	 * @param board		Feld
	 * @return			gibt an, ob das Schiff erfolgreich platziert wurde
	 */
	private boolean placeSingleShip(int y, int x, int length, char[][] board) {
		boolean shipPlaced = false;
		boolean used0 = false;
		boolean used1 = false;
		boolean used2 = false;
		boolean used3 = false;
		boolean usedAll = false;
		while(usedAll == false && shipPlaced == false) {				//wird wiederholt, bis alle Richtungen probiert wurden oder das Schiff gesetzt wird
			int dir = ThreadLocalRandom.current().nextInt(0, 3 + 1);	//Richtung: 0=hoch, 1=recht, 2=runter, 3=links
			if(dir == 0 && used0 == false) {
				used0 = true;
			}
			if(dir == 1 && used1 == false) {
				used1 = true;
			}
			if(dir == 2 && used2 == false) {
				used2 = true;
			}
			if(dir == 3 && used3 == false) {
				used3 = true;
			}
			usedAll = used0 && used1 && used2 && used3;
		}
		return shipPlaced;
	}
	
	/**
	 * Main-Methode
	 * @param args
	 */
	public static void main(String[] args) {
		TempKiGame game = new TempKiGame(10, 10);
		
		Player ki = new Easy(10);
		
		int[] coords = ki.shootField(game);
		System.out.println(coords[0] + " | " + coords[1]);

	}//end main TempKiGame
	


	
}// //end TempKiGame

