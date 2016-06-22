package shipz;

import javafx.stage.Stage;
import shipz.gui.GUI2;
import shipz.util.Event;
import shipz.util.GameEvent;
import shipz.util.GameEventListener;
import java.util.ArrayList;
import java.util.List;
import shipz.gamemode.*;

import static shipz.util.EventIds.FINISHED_ROUND;
import static shipz.util.EventIds.READY_EVENT;

/**
 * Spielverwaltung
 * @author Max
 * @version	0.1
 */
public class Game implements GameEventListener /*implements GameEventListener*/{

    //IV
    /** Spielfeld des 1. Spielers */
    public char[][] board1;
    /** Spielfeld des 2. Spielers */
    public char[][] board2;
    /** Verweis auf den 1. Spieler */
    private Player player1;
    /** Verweis auf den 2. Spieler */
    private Player player2;
    /** gibt an, ob Spieler 1 aktiv ist */
    private boolean player1active;
    /** Netzwerkverbindung */
    //private Network network;
    /** grafische Nutzeroberfläche */
    public GUI2 gui;
    /** Spielstandverwaltung */
    //private FileStream filestream;
    /** Liste mit den zu verwendenden Schiffen */
    public List<Integer> shipList;
    /** aktive x-Koordinate */
    private int aX;
    /** aktive y-Koordinate */
    private int aY;
    /** letztberechnetes Ergebnis */
    private byte aResult;
    /** gibt an, ob auf die GUI gewartet werden muss */
    public boolean waitForGUI;
    /** zählt die Spielzüge für Testzwecke */
    private int testCounter;

    //Constructor
    /**
     * erstellt ein neues Spiel mit leeren Feldern
     * @param width		Feldbreite
     * @param height	Feldhöhe
     */
    public Game(int width, int height, Stage primaryStage) {
        board1 = new char[width][height];
        board2 = new char[width][height];
        initiateBoards();
        gui = new GUI2(primaryStage);
        gui.setEventListener(this);
        player1active = true;
        testCounter = 1;
    }

    //Methoden
    /**
     * startet ein neues oder geladenes Spiel mit den ausgewählten Optionen
     */
    private void startGame(){};

    /**
     * setzt alle Zellen eines Felds auf Wasser
     */
    private void initiateBoards() {
        //1. Zähler
        int y;
        //2. Zähler
        int x;
        //doppelte Schleife für Durchlauf durch alle Felder
        for(y=0; y<board1.length; y++) {
            for(x=0; x<board1[y].length; x++) {
                board1[y][x] = 'w';
            }//Ende ySchleife
        }//Ende xSchleife
        for(y=0; y<board1.length; y++) {
            for(x=0; x<board1[y].length; x++) {
                board2[y][x] = 'w';
            }//Ende ySchleife
        }//Ende xSchleife
    }

    /**
     * überprüft das Ende des Spiels und leitet eventuelle Benachrichtigungen ein
     * @return gibt an, ob das Spiel beendet ist
     */
    private boolean checkGameOver() {return false;}

    /**
     * überprüft die übergebenen Koordinaten auf Schiffelemente und ruft eventuell sink() auf
     * @param x 	Koordinate
     * @param y 	Koordinate
     * @return 0	Wasser getroffen
     * @return 1	Schiff getroffen
     * @return 2	Schiff versenkt
     */
    private byte checkTile(int x, int y) {
        byte r = 0;
        if(player1active) {
            if (board2[y][x] == 'x') {
                r = 1;
                if (sink(x, y)) {
                    r = 2;
                }
                //System.out.println("Es wurde ein Schiffelement zerstört");
                return r;
            } else {
                //System.out.println("Es wurde kein Schiffelement zerstört");
                return r;
            }
        }
        else {
            if (board1[y][x] == 'x') {
                r = 1;
                if (sink(x, y)) {
                    r = 2;
                }
                //System.out.println("Es wurde ein Schiffelement zerstört");
                return r;
            } else {
                //System.out.println("Es wurde kein Schiffelement zerstört");
                return r;
            }
        }
    }

    /**
     * setzt die angegebene Zelle auf ein zerstörtes Schiffelement
     * @param x		Koordinate
     * @param y 	Koordinate
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean sink(int x, int y) {
        if(player1active) {
            board2[y][x] = 'z';
            return (checkShipDestroyed(x, y));
        }
        else {
            board1[y][x] = 'z';
            return (checkShipDestroyed(x, y));
        }
    }

    /**
     * prüft den Gesamtzustand des Schiffs
     * @param x		Koordinate
     * @param y 	Koordinate
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyed(int x, int y) {
        return (checkShipDestroyedUp(x, y) && checkShipDestroyedRight(x, y) && checkShipDestroyedDown(x, y) && checkShipDestroyedLeft(x, y));
    }

    /**
     * prüft den Gesamtzustand des Schiffs nach oben
     * @param x		Koordinate
     * @param y 	Koordinate
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyedUp(int x, int y) {
        if(y-1 >= 0) {
            if(player1active) {
                if (board2[y - 1][x] == 'w') {                        //Wasser auf angrenzendem Feld
                    return true;                                //Schiff wurde vielleicht versenkt
                } else if (board2[y - 1][x] == 'x') {                    //Schiffelement auf angrenzendem Feld
                    return false;                                //Schiff wurde nicht versenkt
                } else {                                            //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedUp(x, y - 1);    //weitere Prüfung vom angrenzenden Feld
                }
            }
            else {
                if (board1[y - 1][x] == 'w') {                        //Wasser auf angrenzendem Feld
                    return true;                                //Schiff wurde vielleicht versenkt
                } else if (board1[y - 1][x] == 'x') {                    //Schiffelement auf angrenzendem Feld
                    return false;                                //Schiff wurde nicht versenkt
                } else {                                            //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedUp(x, y - 1);    //weitere Prüfung vom angrenzenden Feld
                }
            }
        }
        else {
            return true;
        }
    }

    /**
     * prüft den Gesamtzustand des Schiffs nach rechts
     * @param x		Koordinate
     * @param y 	Koordinate
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyedRight(int x, int y) {
        if(player1active) {
            if (x + 1 < board2[y].length) {
                if (board2[y][x + 1] == 'w') {                            //Wasser auf angrenzendem Feld
                    return true;                                    //Schiff wurde vielleicht versenkt
                } else if (board2[y][x + 1] == 'x') {                        //Schiffelement auf angrenzendem Feld
                    return false;                                    //Schiff wurde nicht versenkt
                } else {                                                //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedRight(x + 1, y);    //weitere Prüfung vom angrenzenden Feld
                }
            } else {
                return true;
            }
        }
        else {
            if (x + 1 < board1[y].length) {
                if (board1[y][x + 1] == 'w') {                            //Wasser auf angrenzendem Feld
                    return true;                                    //Schiff wurde vielleicht versenkt
                } else if (board1[y][x + 1] == 'x') {                        //Schiffelement auf angrenzendem Feld
                    return false;                                    //Schiff wurde nicht versenkt
                } else {                                                //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedRight(x + 1, y);    //weitere Prüfung vom angrenzenden Feld
                }
            } else {
                return true;
            }
        }
    }

    /**
     * prüft den Gesamtzustand des Schiffs nach unten
     * @param x		Koordinate
     * @param y 	Koordinate
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyedDown(int x, int y) {
        if(player1active) {
            if (y + 1 < board2.length) {
                if (board2[y + 1][x] == 'w') {                        //Wasser auf angrenzendem Feld
                    return true;                                //Schiff wurde vielleicht versenkt
                } else if (board2[y + 1][x] == 'x') {                    //Schiffelement auf angrenzendem Feld
                    return false;                                //Schiff wurde nicht versenkt
                } else {                                            //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedDown(x, y + 1);    //weitere Prüfung vom angrenzenden Feld
                }
            } else {
                return true;
            }
        }
        else {
            if (y + 1 < board1.length) {
                if (board1[y + 1][x] == 'w') {                        //Wasser auf angrenzendem Feld
                    return true;                                //Schiff wurde vielleicht versenkt
                } else if (board1[y + 1][x] == 'x') {                    //Schiffelement auf angrenzendem Feld
                    return false;                                //Schiff wurde nicht versenkt
                } else {                                            //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedDown(x, y + 1);    //weitere Prüfung vom angrenzenden Feld
                }
            } else {
                return true;
            }
        }
    }

    /**
     * prüft den Gesamtzustand des Schiffs nach links
     * @param x		Koordinate
     * @param y 	Koordinate
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyedLeft(int x, int y) {
        if(x-1 >= 0) {
            if(player1active) {
                if (board2[y][x - 1] == 'w') {                            //Wasser auf angrenzendem Feld
                    return true;                                    //Schiff wurde vielleicht versenkt
                } else if (board2[y][x - 1] == 'x') {                        //Schiffelement auf angrenzendem Feld
                    return false;                                    //Schiff wurde nicht versenkt
                } else {                                                //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedLeft(x - 1, y);    //weitere Prüfung vom angrenzenden Feld
                }
            }
            else {
                if (board1[y][x - 1] == 'w') {                            //Wasser auf angrenzendem Feld
                    return true;                                    //Schiff wurde vielleicht versenkt
                } else if (board1[y][x - 1] == 'x') {                        //Schiffelement auf angrenzendem Feld
                    return false;                                    //Schiff wurde nicht versenkt
                } else {                                                //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedLeft(x - 1, y);    //weitere Prüfung vom angrenzenden Feld
                }
            }
        }
        else {
            return true;
        }
    }

    /**
     * zählt die Anzahl der Schiffe auf einem Feld
     * @return		Anzahl der Schiffe
     */
    private int shipCount() {return 0;}

    /**
     * gibt beide Felder hintereinander auf der Konsole aus (für Testzwecke)
     */
    public void displayBoards() {
        System.out.println("Spieler 1");
        displaySingleBoard(board1);
        System.out.println();
        System.out.println("Spieler 2");
        displaySingleBoard(board2);
    }

    /**
     * gibt ein Feld auf der Konsole aus (für Testzwecke)
     * @param board auszugebendes Feld
     */
    public void displaySingleBoard(char[][] board) {
        //Ausgabe der oberen Feldbeschriftung
        System.out.println("  A B C D E F G H I J");
        //1. Zähler
        int y;
        //2. Zähler
        int x;
        //doppelte Schleife für Durchlauf durch alle Felder
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
     * platziert alle Schiffe eines Spielers auf seinem Feld
     * @param player	Feld
     */
    public void placeShips(int player) {
        for(Integer i : shipList) {
            if(player == 1) {
                while (placeSingleShip(randomNumber(0, board1.length), randomNumber(0, board1[0].length), i, player) == false) {
                }
            }
            else {
                while (placeSingleShip(randomNumber(0, board2.length), randomNumber(0, board2[0].length), i, player) == false) {
                }
            }
        }
        swapChars('b', 'w', player);
    }

    /**
     * platziert ein einzelnes Schiff
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean placeSingleShip(int y, int x, int length, int player) {
        boolean shipPlaced = false;
        boolean used0 = false;
        boolean used1 = false;
        boolean used2 = false;
        boolean used3 = false;
        boolean usedAll = false;
        while(usedAll == false && shipPlaced == false) {				//wird wiederholt, bis alle Richtungen probiert wurden oder das Schiff gesetzt wird
            int dir = randomNumber(0, 3);	//Richtung: 0=hoch, 1=recht, 2=runter, 3=links
            if(dir == 0 && used0 == false) {
                shipPlaced = checkShipUp(y, x, length, player);
                used0 = true;
            }
            if(dir == 1 && used1 == false) {
                shipPlaced = checkShipRight(y, x, length, player);
                used1 = true;
            }
            if(dir == 2 && used2 == false) {
                shipPlaced = checkShipDown(y, x, length, player);
                used2 = true;
            }
            if(dir == 3 && used3 == false) {
                shipPlaced = checkShipLeft(y, x, length, player);
                used3 = true;
            }
            usedAll = used0 && used1 && used2 && used3;
        }
        return shipPlaced;
    }

    /**
     * überprüft, ob ein einzelnes Schiff gesetzt werden kann (nach oben), und ruft eventuell placeShip auf
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean checkShipUp(int y, int x, int length, int player) {
        boolean placeable = false;
        //Prüfung, ob alle zu prüfenden Zellen auf dem Feld liegen
        if(player == 1) {
            if (y >= 0 && y < board1.length && x >= 0 && x < board1[y].length && y - length + 1 >= 0) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board1[y - i][x] != 'w') {
                        placeable = false;
                    }
                }
            }
        }
        else {
            if (y >= 0 && y < board2.length && x >= 0 && x < board2[y].length && y - length + 1 >= 0) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board2[y - i][x] != 'w') {
                        placeable = false;
                    }
                }
            }
        }
        if(placeable) {
            placeShipUp(y, x, length, player);
            return true;
        }
        return false;
    }

    /**
     * platziert ein einzelnes Schiff (nach oben)
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     */
    private void placeShipUp(int y, int x, int length, int player) {
        for(int i = 0; i < length; i++) {
            if(player == 1) {
                board1[y - i][x] = 'x';
            }
            else {
                board2[y - i][x] = 'x';
            }
        }
        blockTiles(player, 0, length, x, y);
    }

    /**
     * überprüft, ob ein einzelnes Schiff gesetzt werden kann (nach rechts), und ruft eventuell placeShip auf
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean checkShipRight(int y, int x, int length, int player) {
        boolean placeable = false;
        if(player == 1) {
            //Prüfung, ob alle zu prüfenden Zellen auf dem Feld liegen
            if (y >= 0 && y < board1.length && x >= 0 && x < board1[y].length && x + length - 1 < board1[y].length) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board1[y][x + i] != 'w') {
                        placeable = false;
                    }
                }
            }
            if (placeable) {
                placeShipRight(y, x, length, player);
                return true;
            }
            return false;
        }
        else {
            //Prüfung, ob alle zu prüfenden Zellen auf dem Feld liegen
            if (y >= 0 && y < board2.length && x >= 0 && x < board2[y].length && x + length - 1 < board2[y].length) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board2[y][x + i] != 'w') {
                        placeable = false;
                    }
                }
            }
            if (placeable) {
                placeShipRight(y, x, length, player);
                return true;
            }
            return false;
        }
    }

    /**
     * platziert ein einzelnes Schiff (nach rechts)
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     */
    private void placeShipRight(int y, int x, int length, int player) {
        for(int i = 0; i < length; i++) {
            if (player == 1) {
                board1[y][x + i] = 'x';
            }
            else {
                board2[y][x + i] = 'x';
            }
        }
        blockTiles(player, 1, length, x, y);
    }

    /**
     * überprüft, ob ein einzelnes Schiff gesetzt werden kann (nach unten), und ruft eventuell placeShip auf
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean checkShipDown(int y, int x, int length, int player) {
        boolean placeable = false;
        if(player == 1) {
            //Prüfung, ob alle zu prüfenden Zellen auf dem Feld liegen
            if (y >= 0 && y < board1.length && x >= 0 && x < board1[y].length && y + length - 1 < board1.length) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board1[y + i][x] != 'w') {
                        placeable = false;
                    }
                }
            }
            if (placeable) {
                placeShipDown(y, x, length, player);
                return true;
            }
            return false;
        }
        else{
            //Prüfung, ob alle zu prüfenden Zellen auf dem Feld liegen
            if (y >= 0 && y < board2.length && x >= 0 && x < board2[y].length && y + length - 1 < board2.length) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board2[y + i][x] != 'w') {
                        placeable = false;
                    }
                }
            }
            if (placeable) {
                placeShipDown(y, x, length, player);
                return true;
            }
            return false;
        }
    }

    /**
     * platziert ein einzelnes Schiff (nach unten)
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     */
    private void placeShipDown(int y, int x, int length, int player) {
        for(int i = 0; i < length; i++) {
            if(player == 1) {
                board1[y + i][x] = 'x';
            }
            else {
                board2[y + i][x] = 'x';
            }
        }
        blockTiles(player, 2, length, x, y);
    }

    /**
     * überprüft, ob ein einzelnes Schiff gesetzt werden kann (nach links), und ruft eventuell placeShip auf
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean checkShipLeft(int y, int x, int length, int player) {
        boolean placeable = false;
        //Prüfung, ob alle zu prüfenden Zellen auf dem Feld liegen
        if(player == 1) {
            if (y >= 0 && y < board1.length && x >= 0 && x < board1[y].length && x - length + 1 >= 0) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board1[y][x - i] != 'w') {
                        placeable = false;
                    }
                }
            }
        }
        else {
            if (y >= 0 && y < board2.length && x >= 0 && x < board2[y].length && x - length + 1 >= 0) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board2[y][x - i] != 'w') {
                        placeable = false;
                    }
                }
            }
        }
        if(placeable) {
            placeShipLeft(y, x, length, player);
            return true;
        }
        return false;
    }

    /**
     * platziert ein einzelnes Schiff (nach links)
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     */
    private void placeShipLeft(int y, int x, int length, int player) {
        for(int i = 0; i < length; i++) {
            if(player == 1) {
                board1[y][x - i] = 'x';
            }
            else {
                board2[y][x - i] = 'x';
            }
        }
        blockTiles(player, 3, length, x, y);
    }

    /**
     * blockiert Zellen auf dem Spielfeld, sodass Schiffe nicht aneinander gesetzt werden
     * @param player	Feld
     * @param dir		Richtung in die das zu blockierende Schiff gesetzt wurde
     * @param length	Schiffslänge
     * @param x			Koordinate, von der aus das Schiff gesetzt wurde
     * @param y			Koordinate, von der aus das Schiff gesetzt wurde
     */
    private void blockTiles(int player, int dir, int length, int x, int y) {
        //Positon der Schiffsenden
        int posAX;
        int posAY;
        int posBX;
        int posBY;
        //Schiff wurde nach oben gesetzt
        if(dir == 0){
            posAX = x;
            posAY = y-length+1;
            posBX = x;
            posBY = y;
        }
        //Schiff wurde nach unten gesetzt
        else if(dir == 2){
            posAX = x;
            posAY = y;
            posBX = x;
            posBY = y+length-1;
        }
        //Schiff wurde nach rechts gesetzt
        else if(dir == 1){
            posAX = x;
            posAY = y;
            posBX = x+length-1;
            posBY = y;
        }
        //Schiff wurde nach links gesetzt
        else{
            posAX = x-length+1;
            posAY = y;
            posBX = x;
            posBY = y;
        }
        //Schleife zur Aktualisierung des char[][]
        for(y=posAY-1; y<=posBY+1; y++) {
            for(x=posAX-1; x<=posBX+1; x++) {
                if(player == 1) {
                    //Prüfung, ob die berechnete Koordinate im Feld liegt
                    if (y >= 0 && y < board1.length && x >= 0 && x < board1[y].length) {
                        //Prüfung, ob das zu blockierende Feld noch unbelegt ist
                        if (board1[y][x] == 'w') {
                            board1[y][x] = 'b';
                        }
                    } //Ende der Prüfungen
                }
                else {
                    //Prüfung, ob die berechnete Koordinate im Feld liegt
                    if (y >= 0 && y < board2.length && x >= 0 && x < board2[y].length) {
                        //Prüfung, ob das zu blockierende Feld noch unbelegt ist
                        if (board2[y][x] == 'w') {
                            board2[y][x] = 'b';
                        }
                    } //Ende der Prüfungen
                }
            }
        } //Ende der Schleifen
    }

    /**
     * ersetzt alle chars auf einem Spielfeld
     * @param a		    ursprüngliche chars
     * @param b		    neue chars
     * @param player    Feld
     */
    public void swapChars(char a, char b, int player) {
        int x, y;
        if(player == 1) {
            for (y = 0; y < board1.length; y++) {
                for (x = 0; x < board1[y].length; x++) {
                    if (board1[y][x] == a) {
                        board1[y][x] = b;
                    }
                }
            }
        }
        else {
            for (y = 0; y < board2.length; y++) {
                for (x = 0; x < board2[y].length; x++) {
                    if (board2[y][x] == a) {
                        board2[y][x] = b;
                    }
                }
            }
        }
    }

    /**
     * @param min	kleinstmögliche gewünschte Zahl
     * @param max	größtmögliche gewünschte Zahl
     * @return		zufällige Zahl zwischen min und max
     */
    private int randomNumber(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    /**
     * erstellt aus einem String eine Schiffsliste
     * @param s	numerischer Wert eines chars steht für die Länge eines Schiffs
     * @return	Integer-Liste mit einem Eintrag für jedes Schiff
     */
    public List<Integer> createShipList(String s) {
        List<Integer> r = new ArrayList<Integer>();
        for(int i=0; i<s.length(); i++) {
            r.add(Character.getNumericValue(s.charAt(i)));
        }
        return r;
    }

    /**
     * zeichnet alle Schiffe auf die GUI
     */
    public void drawShipOnGUI() {
        int x, y;
        for(y=0; y<board1.length; y++) {
            for (x = 0; x < board1[y].length; x++) {
                if(board1[y][x] == 'x') {
                    gui.drawShip(y, x, 1);
                }
            }
        }
        for(y=0; y<board2.length; y++) {
            for (x = 0; x < board2[y].length; x++) {
                if(board2[y][x] == 'x') {
                    gui.drawShip(y, x, 2);
                }
            }
        }
    }

    private void nextRound() {
        if (player1active) {
            try {
                Thread.sleep(1000);
                //TimerTask
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waitForGUI = true;
            aX = player1.getX();
            aY = player1.getY();
            aResult = checkTile(aX, aY);
            player1.shootResult(aY, aX, aResult);
            System.out.println("Zug" + testCounter + ": " + aY + "/" + aX + " => " + aResult);
            testCounter++;
            if (aResult == 0) {
                gui.drawWater(aY, aX, 2);
            } else {
                gui.drawExplosion(aY, aX, 2);
            }
        }
    }

    /**
     * @return gibt an, ob das Spiel beendet wurde
     */
    private boolean gameFinished() {
        boolean r = true;
        //1. Zähler
        int y;
        //2. Zähler
        int x;

        //doppelte Schleife für Durchlauf durch alle Felder
        for(y=0; y<board1.length; y++) {
            for(x=0; x<board1[y].length; x++) {
                if(r && board1[y][x] == 'x') {
                    r = false;
                }
            }//Ende ySchleife
        }//Ende xSchleife

        //doppelte Schleife für Durchlauf durch alle Felder
        for(y=0; y<board2.length; y++) {
            for(x=0; x<board2[y].length; x++) {
                if(r && board2[y][x] == 'x') {
                    r = false;
                }
            }//Ende ySchleife
        }//Ende xSchleife
        return r;
    }

    protected void test() {

        player1 = new Easy(10, false, shipList);
        player2 = new Easy(10, false, shipList);
        nextRound();

        //Schleife zum Test der KI
        /*int x;
        int y;
        byte result;
        for(int i = 0; i<20; i++) {
            if(player1active) {
                x = player1.getX();
                y = player1.getY();
                result = checkTile(x, y);
                player1.shootResult(y, x, result);
                gui.drawExplosion(x, y, 2);
            }
            else {
                x = player2.getX();
                y = player2.getY();
                result = checkTile(x, y);
                player2.shootResult(y, x, result);
                gui.drawExplosion(x, y, 1);
            }
            if(player1active) {
                player1active = false;
            }
            else {
                player1active = true;

            }
        }*/
    }


    /**
     * Main-Methode
     * @param args
     */
    public static void main(String[] args) {
    }

    @Override
    public void eventReceived(GameEvent e) {
        int id = e.getId();

        switch(id) {
            case FILL_EVENT:
                shipList = createShipList("5443332");
                placeShips(1);
                placeShips(2);
                displayBoards();
                drawShipOnGUI();
                break;
            case READY_EVENT:
                test();
                break;
            case FINISHED_ROUND:
                if(testCounter <= 10) {
                    nextRound();
                }
                else {
                    System.out.println("Ende");
                }
        }
    }


/*    @Override
    public void start(Stage primaryStage) {
        gui.start(primaryStage);
    }*/

    /*public void eventReceived(GameEvent e) {
        Player source = e.getSource();
        byte id = e.getId();

        switch(id) {
            case SHOOT_EVENT:
                if(isHost) {
                    source.shootResult();
                }

                break;
        }
    }*/
}

/*
 * w = Wasser
 * x = Schiff
 * z = zerstörtes Schiff
 * b = blockierte Zelle
 */
