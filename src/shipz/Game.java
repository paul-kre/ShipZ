package shipz;

import javafx.stage.Stage;
import shipz.gui.GUI2;
import shipz.gui.newGUI;
import shipz.io.FileStream;
import shipz.network.Network;
import shipz.util.GameEvent;
import shipz.util.GameEventListener;
import shipz.util.NoDrawException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import shipz.ai.*;

/**
 * Spielverwaltung
 * @author Max
 * @version	0.1
 */
public class Game implements GameEventListener {

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
    private Network network;
    /** grafische Nutzeroberfläche */
    private newGUI gui;
    /** Spielstandverwaltung */
    private FileStream filestream;

    /** Liste mit den zu verwendenden Schiffen */
    public List<Integer> shipList;
    /** aktive x-Koordinate */
    private int aX;
    /** aktive y-Koordinate */
    private int aY;
    /** letztberechnetes Ergebnis */
    private byte aResult;
    /** gibt an, ob das Spiel zurzeit pausiert ist */
    private boolean gamePaused;
    /** Gibt an, in welchem Spielmodus das Spiel abläuft
     *  1   PvP
     *  2   PvK
     *  3   KvK
     */
    private byte mode = 0;

    //Constructor
    /**
     * erstellt ein neues Spiel mit leeren Feldern
     * @param width		    Feldbreite
     * @param height	    Feldhöhe
     * @param primaryStage  wird für das Erstellen der GUI verwendet
     */
    public Game(int width, int height, Stage primaryStage) {
        board1 = new char[width][height];
        board2 = new char[width][height];
        initiateBoards();
        gui = new newGUI(primaryStage);
        gui.setEventListener(this);
        player1active = true;
        gamePaused = false;
        filestream = new FileStream();
    }
/*
    //Methoden

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
     * überprüft die übergebenen Koordinaten auf Schiffelemente und ruft eventuell sink() auf
     * @param x 	Koordinate
     * @param y 	Koordinate
     * @return      0 Wasser getroffen, 1 Schiff getroffen, 2 Schiff versenkt
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
     * generiert eine zufällige Zahl
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
                    gui.draw(y, x, 1, 1);
                }
            }
        }
        for(y=0; y<board2.length; y++) {
            for (x = 0; x < board2[y].length; x++) {
                if(board2[y][x] == 'x') {
                    gui.draw(y, x, 2, 1);
                }
            }
        }
    }

    /**
     * führt die nächste Runde eine KI-Spielers aus
     */
    private void nextRoundAI() {
        Player activePlayer;
        //int aiTimer = filestream.getAiTimer();
        if (player1active) {
            activePlayer = player1;
        }
        else {
            activePlayer = player2;
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {@Override
            public void run() {
                aX = activePlayer.getX();
                aY = activePlayer.getY();
                aResult = checkTile(aX, aY);
                activePlayer.shootResult(aY, aX, aResult);
                System.out.println("Spieler " + activePlayer() + ": " + aY + "/" + aX + " => " + aResult);
                if(player1active) {
                    if (aResult == 0) {
                        gui.draw(aY, aX, 2, 0);
                    } else {
                        gui.draw(aY, aX, 2, 2);
                    }
                }
                else {
                    if (aResult == 0) {
                        gui.draw(aY, aX, 1, 0);
                    } else {
                        gui.draw(aY, aX, 1, 2);
                    }
                }
            }
    }, 1000);
    }

    private void nextRoundHuman() {
        aX = gui.getX();
        aY = gui.getY();
        aResult = checkTile(aX, aY);
        System.out.println("Spieler " + activePlayer() + ": " + aY + "/" + aX + " => " + aResult);
        if(player1active) {
            if (aResult == 0) {
                gui.draw(aY, aX, 2, 0);
            } else {
                gui.draw(aY, aX, 2, 2);
            }
        }
        else {
            if (aResult == 0) {
                gui.draw(aY, aX, 1, 0);
            } else {
                gui.draw(aY, aX, 1, 2);
            }
        }
    }


    /**
     * Überprüft den aktuellen Spielstand
     * @return gibt an, ob das Spiel beendet wurde und wer gewonnen hat
     */
    private byte gameFinished() {
        boolean r1 = true;
        boolean r2 = true;
        //1. Zähler
        int y;
        //2. Zähler
        int x;

        //doppelte Schleife für Durchlauf durch alle Felder
        for (y = 0; y < board1.length; y++) {
            for (x = 0; x < board1[y].length; x++) {
                if (board1[y][x] == 'x') {
                    r1 = false;
                }
            }//Ende ySchleife
        }//Ende xSchleife

        //doppelte Schleife für Durchlauf durch alle Felder
        for (y = 0; y < board2.length; y++) {
            for (x = 0; x < board2[y].length; x++) {
                if (board2[y][x] == 'x') {
                    r2 = false;
                }
            }//Ende ySchleife
        }//Ende xSchleife

        if(r1 && !r2) {
            return 2;
        }
        else if(!r1 && r2) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * Ersatz der Main-Methode (für Testzwecke)
     */
    protected void test() {

        /*player1 = new Hard(10, false, shipList);
        player2 = new Hard(10, false, shipList);
        nextRoundHuman();
        System.out.println("Mode = " + mode);*/

    }

    private void cycle() {
        if(mode == 1) {
            if(player1active) {
                gui.setEnableField(2);
            }
            else {
                gui.setEnableField(1);
            }
        }
        if(mode == 2) {
            if(player1active) {
                gui.setEnableField(2);
            }
            else {
                nextRoundAI();
            }
        }
        if(mode == 3) {
            nextRoundAI();
        }
    }


    /**
     * Main-Methode
     * @param args
     */
   public static void main(String[] args) {}

    @Override
    /**
     * verwaltet die eingehenden Events
     * @param e zu bearbeitendes Event
     */
    public void eventReceived(GameEvent e) {
        int id = e.getId();

        switch(id) {
            case GUI_SHOOT_EVENT:
                gui.setEnableField(0);
                nextRoundHuman();
                cycle();
                break;
            case FILL_EVENT:
                shipList = createShipList("5443332");
                placeShips(1);
                placeShips(2);
                displayBoards();
                //drawShipOnGUI();
                if(mode == 2 || mode == 3) {
                    player2 = new Hard(10, false, shipList);
                }
                if(mode == 3) {
                    player1 = new Easy(10, false, shipList);
                }

                cycle();
                break;
            case READY_EVENT:
                test();
                break;
            case FINISHED_ROUND:
            	filestream.newDraw(aX, aY, activePlayer(), aResult);
                if(gameFinished() == 0) {
                    if(aResult == 0) {
                        changeActivePlayer();
                    }
                    cycle();
                }
                else {
                    System.out.println("Spieler " + gameFinished() + " hat das Spiel gewonnen");
                }
                break;
            case PAUSE_EVENT:
                if(gamePaused) {
                    nextRoundAI();
                }
                break;
            case UNDO_EVENT:
            	try {
            		undo(filestream.undoDraw(activePlayer()));
            	} catch (NoDrawException x) {
            		x.printStackTrace();
            		// Dialog wird auf der GUI ausgegeben
            		// dass keine Z�ge mehr r�ckg�ngig gemacht werden k�nnen
            	}
            	break;
            case REDO_EVENT:
				try {
					redo(filestream.redoDraw(activePlayer()));
				} catch (NoDrawException x) {
					x.printStackTrace();
            		// Dialog wird auf der GUI ausgegeben
            		// dass keine Z�ge mehr wiederholt werden k�nnen
				}
				break;
            case SAVE_EVENT:
            	filestream.saveGame("testName", "test1", "test2", boardToString(1), boardToString(2), (int)boardSize(), activePlayer(), null);
            	// Name des Spielstands muss noch irgendwie �bergeben werden
            	// Spielernamen m�ssen noch korrekt zur�ckgegeben werden
            	break;
            case LOAD_EVENT:
            	loadGame(null);
            	break;
            case PVP_EVENT:
                mode = 1;
                System.out.println("Mode = " + mode);   //Test
                break;
            case PVK_EVENT:
                mode = 2;
                System.out.println("Mode = " + mode);   //Test
                break;
            case KVK_EVENT:
                mode = 3;
                System.out.println("Mode = " + mode);   //Test
                break;
        }
    }

    /**
     * Macht die ZÜge rückgängig.
     * @param str String, der die rückgängig gemachten Züge speichert.
     */
    private void undo(String str) {
    	String[] draws = str.split(";");
    	int x, y, result, playerIndex;
    	for(int i = 0; i < draws.length; i++) {
            System.out.println("test " + draws[i]);
            System.out.println("TEST " + draws[i].split("/")[1].split(",")[0]);
    		x = Integer.parseInt(draws[i].split("/")[1].split(",")[0]);
    		y = Integer.parseInt(draws[i].split("/")[1].split(",")[1]);
    		result = Integer.parseInt(draws[i].split("/")[2]);
    		playerIndex = Integer.parseInt(draws[i].split("/")[0]);
    		if(playerIndex == 1) {
                //Wasser
                if(result == 0){
                    board2[y][x] = 'w';
                    gui.draw(y, x, 2, 3);
                }
                //Treffer/versenkt
                else {
                    board2[y][x] = 'x';
                    gui.draw(y, x, 2, 3);
                }
            }
            else {
                //Wasser
                if(result == 0){
                    board1[y][x] = 'w';
                    gui.draw(y, x, 1, 3);
                }
                //Treffer/versenkt
                else {
                    board1[y][x] = 'x';
                    gui.draw(y, x, 1, 3);
                }
            }
    	}
    }
    
    /**
     * Wiederholt zurückgenommene Züge.
     * @param str String, der die zu wiederholenden Züge speichert.
     */
    private void redo(String str) {
    	String[] draws = str.split(";");
    	int x, y, result, playerIndex;
    	for(int i = 0; i < draws.length; i++) {
    		x = Integer.parseInt(draws[i].split("/")[1].split(",")[0]);
    		y = Integer.parseInt(draws[i].split("/")[1].split(",")[1]);
    		result = Integer.parseInt(draws[i].split("/")[2]);
    		playerIndex = Integer.parseInt(draws[i].split("/")[0]);

    	}

    }

    /**
     * Erstellt aus einem Spielfeld einen String
     * @param playerIndex   Spieler, dessen  Spielfeld bearbeitet werden soll
     * @return              Spielfeld des Spielers als String
     */
    private String boardToString(int playerIndex) {
    	String str = "";
    	char[][] activeBoard;
    	if(playerIndex == 1) {
    		activeBoard = board1;
    	} else if(playerIndex == 2) {
    		activeBoard = board2;
    	} else {
    		throw new RuntimeException("Ungültiger PlayerIndex");
    	}

        for(int i = 0; i < activeBoard.length; i++) {
            for(int j=0; j<activeBoard[i].length; j++) {
                str += activeBoard[j][i];
            }
        }
        return str;
    }

    /**
     * Wenn der Name eines Spielstands angegeben wird,
     * wird das Spiel geladen, in dem die IVs aktualisiert werden.
     * @param gameName Name unter dem das Spiel abgespeichert ist
     */
    private void loadGame(String gameName) {
    	filestream.loadDrawsAndScore(gameName); // aktualisiert die IVs in den Klassen f�r Punkte und Z�ge
    	char[] board1 = filestream.getBoardPlayerOne(gameName).toCharArray();
    	char[] board2 = filestream.getBoardPlayerTwo(gameName).toCharArray();
    	int boardsize = filestream.getBoardsize(gameName);

    	for(int i = 0; i < boardsize; i++) {
    		for(int j = 0; j < boardsize; j++) {
    			this.board1[j][i] = board1[i];
    			/*
    			 * Irgendwie sowas. Sollte noch mal gr�ndlich
    			 * gecodet werden, das hier ist nur der Ansatz.
    			 */
    		}
    	}

//     	player1 = new Player(filestream.getPlayerName(gameName));
//    	player2 = new Player(filestream.getOpponentName(gameName)); // geht nicht, warum?

    	if(filestream.getActivePlayer(gameName) == 1) {
    		player1active = true;
    	} else {
    		player1active = false;
    	}
    }

    /**
     * berechnet die Seitenlänge der Spielfelder
     * @return Seitenlänge
     */
    private double boardSize() {
    	return Math.sqrt(boardToString(1).length());
    }

    /**
     * @return  gibt den aktive Spieler anhand eines int an
     */
    private int activePlayer() {
    	if(player1active) {
    		return 1;
    	}else {
    		return 2;
    	}
    }
    
    /**
     * wechselt den aktiven Spieler
     */
    private void changeActivePlayer() {
        if(player1active) {
            player1active = false;
        }
        else {
            player1active = true;
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


/*
 * w = Wasser
 * x = Schiff
 * z = zerstörtes Schiff
 * b = blockierte Zelle
 */
