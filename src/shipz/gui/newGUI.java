package shipz.gui;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.Duration;
import shipz.util.GameEventSource;

import java.util.Random;


import java.awt.*;
import java.util.ArrayList;


/**
 * Created by nnamf on 26.06.2016.
 */
public class newGUI extends GameEventSource {

    //IV

    // Hinzufügen einer VBox zum layouten
    VBox root = new VBox();

    //Hinzufügen von Panes
    AnchorPane header = new AnchorPane();
    AnchorPane body = new AnchorPane();
    AnchorPane foot = new AnchorPane();

    //Scenegrößen
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    //double width = screenSize.getWidth();
    //double height = screenSize.getHeight();
    double width = 1200;
    double height = 800;

    //Spielfelder
    int fieldSize = 10;
    ImageView[][] field1;
    ImageView[][] field2;

    //Elemente
    Text hlOverall = new Text("Project: shipZ");
    ImageView startbildschirm = new ImageView();
    Image startbild = new Image(newGUI.class.getResource("Startbildschirm.png").toExternalForm());
    ImageView explosionStart = new ImageView();
    ImageView btnMenu = new ImageView();
    Image menu = new Image(newGUI.class.getResource("btnMenu.png").toExternalForm());
    ImageView mainMenu = new ImageView();
    Image imgMainMenu = new Image(newGUI.class.getResource("Menu.png").toExternalForm());
    Button btnPlay = new Button("Play");
    Button btnHighscore = new Button("Highscore");
    Button btnSettings = new Button("Settings");
    Button btnNGame = new Button("New Game");
    Button btnLGame = new Button("Load Game");
    Button btnPvP = new Button("Player vs Player");
    Button btnPvK = new Button("Player vs Ki");
    Button btnKvK = new Button("Ki vs Ki");
    TextField txtfPlayer1 = new TextField("Player 1");
    TextField txtfPlayer2 = new TextField("Player 2");
    CheckBox cboxNetGame = new CheckBox("Networkgame");
    final ToggleGroup group1 = new ToggleGroup();
    RadioButton rbEasy1 = new RadioButton("Easy");
    RadioButton rbNormal1 = new RadioButton("Normal");
    RadioButton rbHard1 = new RadioButton("Hard");
    final ToggleGroup group2 = new ToggleGroup();
    RadioButton rbEasy2 = new RadioButton("Easy");
    RadioButton rbNormal2 = new RadioButton("Normal");
    RadioButton rbHard2 = new RadioButton("Hard");
    final ToggleGroup group3 = new ToggleGroup();
    RadioButton rb10x10 = new RadioButton("Fieldsize 10x10");
    RadioButton rb15x15 = new RadioButton("Fieldsize 15x15");
    RadioButton rb20x20 = new RadioButton("Fieldsize 20x20");
    Button btnGo = new Button("Go!");
    Button btnHost = new Button("Host");
    Button btnClient = new Button("Client");
    TextField txtfIp = new TextField("127.0.0.1");
    TextField txtfPort = new TextField("5555");
    Button btnConnect = new Button("Connect");
    Button btnUndo = new Button("Undo");
    Button btnRedo = new Button("Redo");
    Button btnSave = new Button("Save");
    Button btnLoad = new Button("Load");
    Button btnRndm = new Button("Random");
    Rectangle dragBox = new Rectangle();
    ImageView[] ivSrcGes = new ImageView[5];
    ImageView ivSrc1 = new ImageView();
    ImageView ivSrc2 = new ImageView();
    ImageView ivSrc3 = new ImageView();
    ImageView ivSrc4 = new ImageView();
    ImageView ivSrc5 = new ImageView();
    Button btnLock = new Button("Lock");
    Button btnEGame = new Button("End game");
    Text txtEndScreen = new Text("Nothing");
    Button btnPlayAgain = new Button("Play again");
    Button btnLeave = new Button("Leave");
    Text playername = new Text();

    //Images
    Image white = new Image(newGUI.class.getResource("White.png").toExternalForm());
    Image black = new Image(newGUI.class.getResource("Blackout.png").toExternalForm());
    Image water = new Image(newGUI.class.getResource("shipZ_spielfeld.png").toExternalForm());
    Image ship = new Image(newGUI.class.getResource("shipZ_1ershipIntact.png").toExternalForm());
    Image ship2 = new Image(newGUI.class.getResource("shipZ_2erShipWhole.png").toExternalForm());
    Image ship21 = new Image(newGUI.class.getResource("shipZ_2erShip_Part1.png").toExternalForm());
    Image ship22 = new Image(newGUI.class.getResource("shipZ_2erShip_Part2.png").toExternalForm());
    Image ship3 = new Image(newGUI.class.getResource("shipZ_3erShipWhole.png").toExternalForm());
    Image ship31 = new Image(newGUI.class.getResource("shipZ_3erShip_Part1.png").toExternalForm());
    Image ship32 = new Image(newGUI.class.getResource("shipZ_3erShip_Part2.png").toExternalForm());
    Image ship33 = new Image(newGUI.class.getResource("shipZ_3erShip_Part3.png").toExternalForm());
    Image ship4 = new Image(newGUI.class.getResource("shipZ_4erShipWhole.png").toExternalForm());
    Image ship41 = new Image(newGUI.class.getResource("shipZ_4erShip_Part1.png").toExternalForm());
    Image ship42 = new Image(newGUI.class.getResource("shipZ_4erShip_Part2.png").toExternalForm());
    Image ship43 = new Image(newGUI.class.getResource("shipZ_4erShip_Part3.png").toExternalForm());
    Image ship44 = new Image(newGUI.class.getResource("shipZ_4erShip_Part4.png").toExternalForm());
    Image ship5 = new Image(newGUI.class.getResource("shipZ_5erShipWhole.png").toExternalForm());
    Image ship51 = new Image(newGUI.class.getResource("shipZ_5erShip_Part1.png").toExternalForm());
    Image ship52 = new Image(newGUI.class.getResource("shipZ_5erShip_Part2.png").toExternalForm());
    Image ship53 = new Image(newGUI.class.getResource("shipZ_5erShip_Part3.png").toExternalForm());
    Image ship54 = new Image(newGUI.class.getResource("shipZ_5erShip_Part4.png").toExternalForm());
    Image ship55 = new Image(newGUI.class.getResource("shipZ_5erShip_Part5.png").toExternalForm());
    Image explosion = new Image(newGUI.class.getResource("shipZ_explosion2.gif").toExternalForm());
    Image explosion2 = new Image(newGUI.class.getResource("Explosion.png").toExternalForm());
    //Image explosion2 = new Image(newGUI.class.getResource("shipZ_explosion3.gif").toExternalForm());
    //Image explosion3 = new Image(newGUI.class.getResource("shipZ_explosion4.gif").toExternalForm());

    private Random random = new Random();

    //Spielfeld sperren
    int enableField = 2;

    //Koordinaten
    int xC;
    int yC;

    //Alertbox
    AlertBox alertbox = new AlertBox(this);
    String filename = "";

    //Score und Combo Texte
    Text txtPoints1 = new Text("Score P1 : "+0);
    Text txtPoints2 = new Text("Score P2 : "+0);
    Text txtCombo1 = new Text("Combo P1 : "+1);
    Text txtCombo2 = new Text("Combo P2 : "+1);

    //Spielernamen
    String playername1 = "Spieler 1";
    String playername2 = "Spieler 2";

    //KI Schwierigkeitsstufen
    int ki1Mode;
    int ki2Mode;

    //IP und Port
    String ip;
    String port;

    //Highscore Tabelle
    TableView tbHighscore;
    private ObservableList data;
    ArrayList list = new ArrayList();

    //SavedGames Tabelle
    TableView tbSavedGames;
    private ObservableList data2;
    ArrayList list2 = new ArrayList();
    String gamename;

    //Network
    private boolean isHost;
    private boolean connected = false;
    private boolean isNetwork;

    Text txtConnected = new Text("Unknown");
    Button btnContinue = new Button("Continue");
    Button btnTryAgain = new Button("Try again");


    //IM
    public void setEndScreen () {
        body.getChildren().clear();
        body.getChildren().addAll(txtEndScreen, btnPlayAgain, btnLeave);
    }

    public void setEndMessage (int player) {
        if(player == 1){
            txtEndScreen.setText(playername1+" has won the game!"+"\b"+"Congratulations.");
        } else {
            txtEndScreen.setText(playername2+" has won the game!"+"\b"+"Congratulations.");
        }
    }

    public void setFieldSize (int size) {
        fieldSize = size;
        field1 = new ImageView[fieldSize][fieldSize];
        field2 = new ImageView[fieldSize][fieldSize];
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public void setConnected(boolean b) {
        connected = b;
    }

    public void setIsNetwork(boolean b) {
        isNetwork = b;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setIsHost(boolean b) {
        isHost = b;
    }

    public void setPlayernames (String player1, String player2) {

        playername1 = player1;
        playername2 = player2;

    }


    public String getPlayername (int i) {

        if(i == 1) {
            return playername1;
        }
        else {
            return playername2;
        }

    }


    public int getKi1Mode () {

        return ki1Mode;

    }

    public int getKi2Mode () {

        return ki2Mode;

    }


    public String getPort () {

        return port;

    }

    public String getIp () {

        return ip;

    }

    public String getGamename() {
        return gamename;
    }


    /**
     * Methode zum setzen des Filenamens
     * @param str Filename
     */
    public void setFilename(String str) {
        this.filename = str;
        fireGameEvent(SAVE_EVENT);
    }


    /**
     * Methode zum zurückgeben des Filenamens
     * @return String Filename
     */
    public String getFilename() {
        return filename;
    }


    /**
     * Methode zum erstellen der Spielfelder
     */
    public void createField () {
        //Spielfeld erstellen
        for(int i = 0; i < field1.length; i++){

            for(int j = 0; j < field1[i].length; j++){
                int x = i;
                int y = j;

                //ImageView erstellen und hinzuf�gen
                ImageView oneField = new ImageView();
                Image oneFieldImg = new Image(newGUI.class.getResource("White.png").toExternalForm());
                oneField.setImage(oneFieldImg);
                field1[i][j] = oneField;

                //ImageView platzieren
                oneField.setFitWidth(width*0.015);
                oneField.setPreserveRatio(true);
                oneField.setTranslateX(j * width*0.015);
                oneField.setTranslateY(i * width*0.015);
                oneField.layoutXProperty().setValue(width*0.25);
                oneField.layoutYProperty().setValue(height*0.18);

                body.getChildren().addAll(oneField);

                //auf click
                field1[i][j].setOnMouseClicked(event -> {

                    if(event.getButton() == MouseButton.PRIMARY) {
                        if(enableField==1) {
                            //Event zum �bergeben der Koordinaten
                            //Auf R�ckmeldung
                            setCoordinates(y, x);
                            fireGameEvent(GUI_SHOOT_EVENT);

                        }
                    }
                    else if(event.getButton() == MouseButton.SECONDARY) {

                        oneField.setImage(explosion);


                    }
                });

                //DragandDropTargets
                field1[i][j].setOnDragDetected(new EventHandler <javafx.scene.input.MouseEvent>() {
                    public void handle(javafx.scene.input.MouseEvent event) {
                        /* drag was detected, start drag-and-drop gesture*/
                        System.out.println("onDragDetected");

                        /* allow any transfer mode */
                        Dragboard db = oneField.startDragAndDrop(TransferMode.MOVE);

                        /* put a string on dragboard */
                        if(oneField.getImage()==ship) {
                            ClipboardContent content = new ClipboardContent();
                            content.putString("s1");
                            content.putImage(ship);
                            db.setContent(content);
                        }else if(oneField.getImage()==ship21 || oneField.getImage()==ship22) {
                            ClipboardContent content = new ClipboardContent();
                            content.putString("s2");
                            content.putImage(ship2);
                            db.setContent(content);
                        }else if(oneField.getImage()==ship31 || oneField.getImage()==ship32 || oneField.getImage()==ship33) {
                            ClipboardContent content = new ClipboardContent();
                            content.putString("s3");
                            content.putImage(ship3);
                            db.setContent(content);
                        }else if(oneField.getImage()==ship41 || oneField.getImage()==ship42 || oneField.getImage()==ship43 || oneField.getImage()==ship44) {
                            ClipboardContent content = new ClipboardContent();
                            content.putString("s4");
                            content.putImage(ship4);
                            db.setContent(content);
                        }else if(oneField.getImage()==ship51 || oneField.getImage()==ship52 || oneField.getImage()==ship53 || oneField.getImage()==ship54 || oneField.getImage()==ship55) {
                            ClipboardContent content = new ClipboardContent();
                            content.putString("s5");
                            content.putImage(ship5);
                            db.setContent(content);
                        }


                        event.consume();
                    }
                });

                field1[i][j].setOnDragDone(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* the drag-and-drop gesture ended */
                        System.out.println("onDragDone");
                        /* if the data was successfully moved, clear it */
                        if (event.getTransferMode() == TransferMode.MOVE) {

                            if(oneField.getImage()==ship) {
                                oneField.setImage(white);
                            }else if(oneField.getImage()==ship21) {
                                oneField.setImage(white);
                                field1[x][y+1].setImage(white);
                            }else if(oneField.getImage()==ship22) {
                                oneField.setImage(white);
                                field1[x][y-1].setImage(white);
                            }else if(oneField.getImage()==ship31) {
                                oneField.setImage(white);

                            }else if(oneField.getImage()==ship32) {
                                oneField.setImage(white);

                            }else if(oneField.getImage()==ship33) {
                                oneField.setImage(white);

                            }else if(oneField.getImage()==ship41) {
                                oneField.setImage(white);

                            }else if(oneField.getImage()==ship42) {
                                oneField.setImage(white);

                            }else if(oneField.getImage()==ship43) {
                                oneField.setImage(white);

                            }else if(oneField.getImage()==ship44) {
                                oneField.setImage(white);

                            }else if(oneField.getImage()==ship51) {
                                oneField.setImage(white);

                            }else if(oneField.getImage()==ship52) {
                                oneField.setImage(white);

                            }else if(oneField.getImage()==ship53) {
                                oneField.setImage(white);

                            }else if(oneField.getImage()==ship54) {
                                oneField.setImage(white);

                            }else if(oneField.getImage()==ship55) {
                                oneField.setImage(white);

                            }

                        }

                        event.consume();
                    }
                });

                field1[i][j].setOnDragOver(new EventHandler <DragEvent>() {public void handle(DragEvent event) {
                        /* data is dragged over the target */
                    System.out.println("onDragOver");

                        /* accept it only if it is  not dragged from the same node
                         * and if it has a string data */
                    if (event.getGestureSource() != oneField) {
                            /* allow for both copying and moving, whatever user chooses */
                        event.acceptTransferModes(TransferMode.MOVE);
                    }

                    event.consume();
                }
                });

                field1[i][j].setOnDragEntered(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* the drag-and-drop gesture entered the target */
                        System.out.println("onDragEntered");
                        /* show to the user that it is an actual gesture target */
                        if (event.getGestureSource() != oneField) {

                        }

                        event.consume();
                    }
                });

                field1[i][j].setOnDragExited(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* mouse moved away, remove the graphical cues */


                        event.consume();
                    }
                });

                field1[i][j].setOnDragDropped(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* data dropped */
                        System.out.println("onDragDropped");
                        /* if there is a string data on dragboard, read it and use it */
                        Dragboard db = event.getDragboard();
                        boolean success = false;
                        if(db.getString() == "s1") {
                            oneField.setImage(ship);
                            success = true;
                        }
                        if (db.getString() == "s2") {
                            field1[x][y+1].setImage(ship22);
                            oneField.setImage(ship21);
                            success = true;
                        }
                        if (db.getString() == "s3") {
                            field1[x][y+2].setImage(ship33);
                            field1[x][y+1].setImage(ship32);
                            oneField.setImage(ship31);
                            success = true;
                        }
                        if (db.getString() == "s4") {
                            field1[x][y+3].setImage(ship44);
                            field1[x][y+2].setImage(ship43);
                            field1[x][y+1].setImage(ship42);
                            oneField.setImage(ship41);
                            success = true;
                        }
                        if (db.getString() == "s5") {
                            field1[x][y+4].setImage(ship54);
                            field1[x][y+3].setImage(ship54);
                            field1[x][y+2].setImage(ship53);
                            field1[x][y+1].setImage(ship52);
                            oneField.setImage(ship51);
                            success = true;
                        }
                        /* let the source know whether the string was successfully
                         * transferred and used */
                        event.setDropCompleted(success);

                        event.consume();
                    }
                });

            }

        }

        for(int i = 0; i < field2.length; i++){

            for(int j = 0; j < field2[i].length; j++){
                int x = i;
                int y = j;

                //ImageView erstellen und hinzuf�gen
                ImageView oneField = new ImageView();
                Image oneFieldImg = new Image(newGUI.class.getResource("White.png").toExternalForm());
                oneField.setImage(oneFieldImg);
                field2[i][j] = oneField;

                //ImageView platzieren
                oneField.setFitWidth(width*0.015);
                oneField.setPreserveRatio(true);
                oneField.setTranslateX(j * width*0.015);
                oneField.setTranslateY(i * width*0.015);
                oneField.layoutXProperty().setValue(width*0.65);
                oneField.layoutYProperty().setValue(height*0.18);

                body.getChildren().addAll(oneField);

                //auf click
                field2[i][j].setOnMouseClicked(event -> {

                    if(event.getButton() == MouseButton.PRIMARY) {
                        if(enableField==2) {
                            //Event zum �bergeben der Koordinaten
                            //Auf R�ckmeldung
                            setCoordinates(y, x);
                            fireGameEvent(GUI_SHOOT_EVENT);
                        }
                    }
                    else if(event.getButton() == MouseButton.SECONDARY) {
                        oneField.setImage(water);
                    }
                });

                //DragandDropTargets
                field2[i][j].setOnDragDetected(new EventHandler <javafx.scene.input.MouseEvent>() {
                    public void handle(javafx.scene.input.MouseEvent event) {
                        /* drag was detected, start drag-and-drop gesture*/
                        System.out.println("onDragDetected");

                        /* allow any transfer mode */
                        Dragboard db = oneField.startDragAndDrop(TransferMode.MOVE);

                        /* put a string on dragboard */
                        if(oneField.getImage()==ship) {
                            ClipboardContent content = new ClipboardContent();
                            content.putString("s1");
                            content.putImage(ship);
                            db.setContent(content);
                        }else if(oneField.getImage()==ship21) {
                            field2[x][y+1].setImage(white);
                            ClipboardContent content = new ClipboardContent();
                            content.putString("s2");
                            content.putImage(ship2);
                            db.setContent(content);
                        }else if(oneField.getImage()==ship22) {
                            field2[x][y-1].setImage(white);
                            ClipboardContent content = new ClipboardContent();
                            content.putString("s2");
                            content.putImage(ship2);
                            db.setContent(content);
                        }


                        event.consume();
                    }
                });

                field2[i][j].setOnDragDone(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* the drag-and-drop gesture ended */
                        System.out.println("onDragDone");
                        /* if the data was successfully moved, clear it */
                        if (event.getTransferMode() == TransferMode.MOVE) {

                            oneField.setImage(white);

                        }

                        event.consume();
                    }
                });

                field2[i][j].setOnDragOver(new EventHandler <DragEvent>() {public void handle(DragEvent event) {
                        /* data is dragged over the target */
                    System.out.println("onDragOver");

                        /* accept it only if it is  not dragged from the same node
                         * and if it has a string data */
                    if (event.getGestureSource() != oneField) {
                            /* allow for both copying and moving, whatever user chooses */
                        event.acceptTransferModes(TransferMode.MOVE);
                    }

                    event.consume();
                }
                });

                field2[i][j].setOnDragEntered(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* the drag-and-drop gesture entered the target */
                        System.out.println("onDragEntered");
                        /* show to the user that it is an actual gesture target */
                        if (event.getGestureSource() != oneField) {

                        }

                        event.consume();
                    }
                });

                field2[i][j].setOnDragExited(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* mouse moved away, remove the graphical cues */


                        event.consume();
                    }
                });

                field2[i][j].setOnDragDropped(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* data dropped */
                        System.out.println("onDragDropped");
                        /* if there is a string data on dragboard, read it and use it */
                        Dragboard db = event.getDragboard();
                        boolean success = false;
                        if(db.getString() == "s1") {
                            oneField.setImage(ship);
                            success = true;
                        }
                        if (db.getString() == "s2") {
                            oneField.setImage(ship21);
                            field2[x][y+1].setImage(ship22);
                            success = true;
                        }
                        /* let the source know whether the string was successfully
                         * transferred and used */
                        event.setDropCompleted(success);

                        event.consume();
                    }
                });

            }

        }

        //Dragzone
        for(int n = 0; n < 5; n++) {
            int number = n;
            switch (n) {
                case 0:
                    ivSrcGes[number] = ivSrc1;
                    break;
                case 1:
                    ivSrcGes[number] = ivSrc2;
                    break;
                case 2:
                    ivSrcGes[number] = ivSrc3;
                    break;
                case 3:
                    ivSrcGes[number] = ivSrc4;
                    break;
                case 4:
                    ivSrcGes[number] = ivSrc5;
                    break;
            }


            ivSrcGes[number].setOnDragDetected(new EventHandler <javafx.scene.input.MouseEvent>() {
                public void handle(javafx.scene.input.MouseEvent event) {
                        /* drag was detected, start drag-and-drop gesture*/
                    System.out.println("onDragDetected");

                        /* allow any transfer mode */
                    Dragboard db = ivSrcGes[number].startDragAndDrop(TransferMode.MOVE);

                        /* put a string on dragboard */
                    ClipboardContent content = new ClipboardContent();
                    if(ivSrcGes[number].getImage()!=white) {
                        switch (number) {
                            case 0:
                                content.putString("s1");
                                content.putImage(ship);
                                break;
                            case 1:
                                content.putString("s2");
                                content.putImage(ship2);
                                break;
                            case 2:
                                content.putString("s3");
                                content.putImage(ship3);
                                break;
                            case 3:
                                content.putString("s4");
                                content.putImage(ship4);
                                break;
                            case 4:
                                content.putString("s5");
                                content.putImage(ship5);
                                break;
                        }
                    }
                    db.setContent(content);

                    event.consume();
                }
            });

            ivSrcGes[number].setOnDragDone(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                        /* the drag-and-drop gesture ended */
                    System.out.println("onDragDone");
                        /* if the data was successfully moved, clear it */
                    if (event.getTransferMode() == TransferMode.MOVE) {

                        ivSrcGes[number].setImage(white);

                    }

                    event.consume();
                }
            });

            ivSrcGes[number].setOnDragOver(new EventHandler <DragEvent>() {public void handle(DragEvent event) {
                        /* data is dragged over the target */
                System.out.println("onDragOver");

                        /* accept it only if it is  not dragged from the same node
                         * and if it has a string data */
                if (event.getGestureSource() != ivSrcGes[number]) {
                            /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            }
            });

            ivSrcGes[number].setOnDragEntered(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                        /* the drag-and-drop gesture entered the target */
                    System.out.println("onDragEntered");
                        /* show to the user that it is an actual gesture target */
                    if (event.getGestureSource() != ivSrcGes[number]) {

                    }

                    event.consume();
                }
            });

            ivSrcGes[number].setOnDragExited(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                        /* mouse moved away, remove the graphical cues */


                    event.consume();
                }
            });

            ivSrcGes[number].setOnDragDropped(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                        /* data dropped */
                    System.out.println("onDragDropped");
                        /* if there is a string data on dragboard, read it and use it */
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasImage()) {
                        switch(number){
                            case 0:
                                ivSrc1.setImage(db.getImage());
                                success = true;
                                break;
                            case 1:
                                ivSrc2.setImage(db.getImage());
                                success = true;
                                break;
                            case 2:
                                ivSrc3.setImage(db.getImage());
                                success = true;
                                break;
                            case 3:
                                ivSrc4.setImage(db.getImage());
                                success = true;
                                break;
                            case 4:
                                ivSrc5.setImage(db.getImage());
                                success = true;
                                break;
                        }

                    }
                        /* let the source know whether the string was successfully
                         * transferred and used */
                    event.setDropCompleted(success);

                    event.consume();
                }
            });

        }





    } // Ende create Field

    /**
     * Methode zum setzen eines Image auf das Spielfeld
     * @param x     X-Koordinate
     * @param y     Y-Koordinate
     * @param field Spielfeld Nummer
     * @param v     Wert zur Angabe der Trefferart
     */
    public void draw (int x, int y, int field, int v) {
        if(field == 1) {
            switch (v) {
                case 0:
                    field1[x][y].setImage(water);
                    if(isHost || !isNetwork) fireGameEvent(FINISHED_ROUND);
                    break;
                case 1:
                    field1[x][y].setImage(ship);
                    break;
                case 2:
                    field1[x][y].setImage(explosion);
                    if(isHost || !isNetwork) fireGameEvent(FINISHED_ROUND);
                    break;
                case 3:
                    field1[x][y].setImage(explosion2);
                    if(isHost || !isNetwork) fireGameEvent(FINISHED_ROUND);
                    break;
                default:
                    field1[x][y].setImage(white);
                    break;
            }
        }
        if(field == 2) {
            switch (v) {
                case 0:
                    field2[x][y].setImage(water);
                    if(isHost || !isNetwork) fireGameEvent(FINISHED_ROUND);
                    break;
                case 1:
                    field2[x][y].setImage(ship);
                    break;
                case 2:
                    field2[x][y].setImage(explosion);
                    if(isHost || !isNetwork) fireGameEvent(FINISHED_ROUND);
                    break;
                case 3:
                    field2[x][y].setImage(explosion2);
                    if(isHost || !isNetwork) fireGameEvent(FINISHED_ROUND);
                    break;
                default:
                    field2[x][y].setImage(white);
                    break;
            }
        }
    }


    public void drawShip(int x, int y, int field, int length, char dir) {

        if(field == 1) {
            switch (length) {

                case 1:
                    //1er Schiff
                    field1[x][y].setImage(ship);
                    break;

                case 2:
                    //2er Schiff

                    //oben
                    if (dir == 'u') {
                        field1[x][y].setImage(ship21);
                        field1[x][y - 1].setImage(ship22);
                    }
                    //unten
                    if (dir == 'd') {
                        field1[x][y].setImage(ship21);
                        field1[x][y + 1].setImage(ship22);
                    }
                    //rechts
                    else if (dir == 'r') {
                        field1[x][y].setImage(ship21);
                        field1[x + 1][y].setImage(ship22);
                    }
                    //unten
                    if (dir == 'l') {
                        field1[x][y].setImage(ship22);
                        field1[x - 1][y].setImage(ship21);
                    }
                    break;

                case 3:
                    //3er Schiff

                    //oben
                    if (dir == 'u') {
                        field1[x][y].setImage(ship31);
                        field1[x][y - 1].setImage(ship32);
                        field1[x][y - 2].setImage(ship33);
                    }
                    //unten
                    if (dir == 'd') {
                        field1[x][y].setImage(ship31);
                        field1[x][y + 1].setImage(ship32);
                        field1[x][y + 2].setImage(ship33);
                    }
                    //rechts
                    if (dir == 'r') {
                        field1[x][y].setImage(ship31);
                        field1[x + 1][y].setImage(ship32);
                        field1[x + 2][y].setImage(ship33);
                    }
                    //unten
                    if (dir == 'l') {
                        field1[x][y].setImage(ship33);
                        field1[x - 1][y].setImage(ship32);
                        field1[x - 2][y].setImage(ship31);
                    }
                    break;
                case 4:
                    //4er Schiff

                    //oben
                    if (dir == 'u') {
                        field1[x][y].setImage(ship41);
                        field1[x][y - 1].setImage(ship42);
                        field1[x][y - 2].setImage(ship43);
                        field1[x][y - 3].setImage(ship44);
                    }
                    //unten
                    if (dir == 'd') {
                        field1[x][y].setImage(ship41);
                        field1[x][y + 1].setImage(ship42);
                        field1[x][y + 2].setImage(ship43);
                        field1[x][y + 3].setImage(ship44);
                    }
                    //rechts
                    if (dir == 'r') {
                        field1[x][y].setImage(ship41);
                        field1[x + 1][y].setImage(ship42);
                        field1[x + 2][y].setImage(ship43);
                        field1[x + 3][y].setImage(ship44);
                    }
                    //unten
                    if (dir == 'l') {
                        field1[x][y].setImage(ship44);
                        field1[x - 1][y].setImage(ship43);
                        field1[x - 2][y].setImage(ship42);
                        field1[x - 3][y].setImage(ship41);
                    }
                    break;
                case 5:
                    //5er Schiff

                    //oben
                    if (dir == 'u') {
                        field1[x][y].setImage(ship51);
                        field1[x][y - 1].setImage(ship52);
                        field1[x][y - 2].setImage(ship53);
                        field1[x][y - 3].setImage(ship54);
                        field1[x][y - 4].setImage(ship55);
                    }
                    //unten
                    if (dir == 'd') {
                        field1[x][y].setImage(ship51);
                        field1[x][y + 1].setImage(ship52);
                        field1[x][y + 2].setImage(ship53);
                        field1[x][y + 3].setImage(ship54);
                        field1[x][y + 4].setImage(ship55);
                    }
                    //rechts
                    if (dir == 'r') {
                        field1[x][y].setImage(ship51);
                        field1[x + 1][y].setImage(ship52);
                        field1[x + 2][y].setImage(ship53);
                        field1[x + 3][y].setImage(ship54);
                        field1[x + 4][y].setImage(ship55);
                    }
                    //unten
                    if (dir == 'l') {
                        field1[x][y].setImage(ship55);
                        field1[x - 1][y].setImage(ship54);
                        field1[x - 2][y].setImage(ship53);
                        field1[x - 3][y].setImage(ship52);
                        field1[x - 4][y].setImage(ship51);
                    }
                    break;
            }
        }
        if(field == 2) {
            switch (length) {

                case 1:
                    //1er Schiff
                    field2[x][y].setImage(ship);
                    break;

                case 2:
                    //2er Schiff
                    field2[x][y].setImage(ship21);
                    //oben
                    if (dir == 'u')
                        field2[x][y - 1].setImage(ship22);
                    //unten
                    if (dir == 'd')
                        field2[x][y + 1].setImage(ship22);
                        //rechts
                    else if (dir == 'r')
                        field2[x + 1][y].setImage(ship22);
                    //unten
                    if (dir == 'l')
                        field2[x - 1][y].setImage(ship22);
                    break;

                case 3:
                    //3er Schiff
                    field2[x][y].setImage(ship31);
                    //oben
                    if (dir == 'u') {
                        field2[x][y - 1].setImage(ship32);
                        field2[x][y - 2].setImage(ship33);
                    }
                    //unten
                    if (dir == 'd') {
                        field2[x][y + 1].setImage(ship32);
                        field2[x][y + 2].setImage(ship33);
                    }
                    //rechts
                    if (dir == 'r') {
                        field2[x + 1][y].setImage(ship32);
                        field2[x + 2][y].setImage(ship33);
                    }
                    //unten
                    if (dir == 'l') {
                        field2[x - 1][y].setImage(ship32);
                        field2[x - 2][y].setImage(ship33);
                    }
                    break;
                case 4:
                    //4er Schiff
                    field2[x][y].setImage(ship41);
                    //oben
                    if (dir == 'u') {
                        field2[x][y - 1].setImage(ship42);
                        field2[x][y - 2].setImage(ship43);
                        field2[x][y - 3].setImage(ship44);
                    }
                    //unten
                    if (dir == 'd') {
                        field2[x][y + 1].setImage(ship42);
                        field2[x][y + 2].setImage(ship43);
                        field2[x][y + 3].setImage(ship44);
                    }
                    //rechts
                    if (dir == 'r') {
                        field2[x + 1][y].setImage(ship42);
                        field2[x + 2][y].setImage(ship43);
                        field2[x + 3][y].setImage(ship44);
                    }
                    //unten
                    if (dir == 'l') {
                        field2[x - 1][y].setImage(ship42);
                        field2[x - 2][y].setImage(ship43);
                        field2[x - 3][y].setImage(ship44);
                    }
                    break;
                case 5:
                    //5er Schiff
                    field2[x][y].setImage(ship51);
                    //oben
                    if (dir == 'u') {
                        field2[x][y - 1].setImage(ship52);
                        field2[x][y - 2].setImage(ship53);
                        field2[x][y - 3].setImage(ship54);
                        field2[x][y - 4].setImage(ship55);
                    }
                    //unten
                    if (dir == 'd') {
                        field2[x][y + 1].setImage(ship52);
                        field2[x][y + 2].setImage(ship53);
                        field2[x][y + 3].setImage(ship54);
                        field2[x][y + 4].setImage(ship55);
                    }
                    //rechts
                    if (dir == 'r') {
                        field2[x + 1][y].setImage(ship52);
                        field2[x + 2][y].setImage(ship53);
                        field2[x + 3][y].setImage(ship54);
                        field2[x + 4][y].setImage(ship55);
                    }
                    //unten
                    if (dir == 'l') {
                        field2[x - 1][y].setImage(ship52);
                        field2[x - 2][y].setImage(ship53);
                        field2[x - 3][y].setImage(ship54);
                        field2[x - 4][y].setImage(ship55);
                    }
                    break;
            }
        }

    }


    /**
     * Methode zum �ndern des Zustandes der GUI (enable/disable)
     * @param v Wert 0 = disabled, 1 = enabled
     */
    public void setEnableField (int v) {
        if(v >= 0 && v <= 2) {
            enableField = v;
            if(v == 1) {
                drawName(2);
            } else if(v == 2) {
                drawName(1);
            }
        }
    }


    /**
     * Methode zum setzen der x- und y-Koordinaten
     */
    public void setCoordinates (int _x, int _y) {
        xC = _x;
        yC = _y;
    }


    public int getX() {
        return xC;
    }

    public int getY() {
        return yC;
    }


    /**
     * Methode zum Anzeigen welcher Player am Zug ist
     * @param player    Spielernummer
     */
    public void drawName (int player) {

        playername.layoutYProperty().setValue(height*0.05);
        playername.layoutXProperty().setValue(width*0.025);
        playername.setStroke(Color.WHITE);
        playername.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));

        FadeTransition ft = new FadeTransition(Duration.millis(3000), playername);
        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        ft.play();

        if(player == 1) {
            playername.setText(playername1+" it's your turn!");
            body.getChildren().add(playername);
        } else {
            playername.setText(playername2+" it's your turn!");
            body.getChildren().add(playername);
        }


    }


    /**
     * Methode zum setzen der Punkte von Spieler 1
     * @param score Punkte
     */
    public void setScoreLabel (int score, int playerIndex) {
        if(playerIndex == 1) {
            txtPoints1.setText("Score P1: "  + score);
        } else if(playerIndex == 2) {
            txtPoints2.setText("Score P2: " + score);
        } else {
            throw new RuntimeException("Ung�ltiger Playerindex!");
        }
    }

    /**
     * Methode zum setzen der Combo von Spieler 1
     * @param combo Kombo
     */
    public void setComboLabel (int combo, int playerIndex) {
        if(playerIndex == 1) {
            txtCombo1.setText("Combo P1: " + combo);
        } else if(playerIndex == 2) {
            txtCombo2.setText("Combo P2: " + combo);
        } else {
            throw new RuntimeException("Ung�ltiger Playerindex!");
        }
    }


    private ObservableList getInitialTableData() {


        /** Test
         list.add(new Highscore("1", "Paul", "2000", "30.09.09"));
         list.add(new Highscore("2", "Flo", "1000", "31.09.09"));
         list.add(new Highscore("3", "Sonnenschein", "900", "32.09.09"));
         list.add(new Highscore("4", "Max", "700", "33.09.09"));
         list.add(new Highscore("5", "Arthur", "500", "34.09.09"));
         */

        ObservableList data = FXCollections.observableList(list);

        return data;
    }


    public ObservableList setNewRow(String pos, String name, String score, String date) {



        String s1 = pos;
        String s2 = name;
        String s3 = score;
        String s4 = date;

        list.add(new Highscore(s1, s2, s3, s4));

        return FXCollections.observableList(list);

    }

    public ObservableList setSavedGame(String gamename, String name1, String name2, String mode, String date) {

        list.add(new SavedGames(gamename, name1, name2, mode, date));
        return FXCollections.observableList(list2);

    }

    public void clearTables() {
        list.clear();
        list2.clear();
    }

    //Constructor
    public newGUI (Stage primaryStage) {


        //Anpassen der Panes
        header.setPrefHeight(height*0.25);

        body.setPrefHeight(height*0.8);
        body.setId("body");

        foot.setPrefHeight(height*0.05);


        // Hinzufügen der Panes zur VBox
        root.getChildren().add(header);
        root.getChildren().add(body);
        root.getChildren().add(foot);


        //Haupt�berschrift
        hlOverall.layoutXProperty().setValue(width*0.03);
        hlOverall.layoutYProperty().setValue(height*0.14);
        //hlOverall.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.12));
        hlOverall.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.12));


        //Startbildschirm
        startbildschirm.setImage(startbild);
        startbildschirm.setFitWidth(body.getWidth());
        startbildschirm.setFitHeight(body.getHeight());
        startbildschirm.setTranslateX(width*0.15);



        //Explosion
        explosionStart.setImage(explosion);


        //Men�button
        btnMenu.setImage(menu);
        btnMenu.setFitWidth(100);
        btnMenu.setFitHeight(100);
        btnMenu.setTranslateX(width*0.9);
        btnMenu.setTranslateY(height*0.05);


        //Hauptmen�
        mainMenu.setImage(imgMainMenu);
        mainMenu.setFitWidth(width*0.239);
        mainMenu.setFitHeight(height*0.385);
        mainMenu.setTranslateX(width*0.35);
        mainMenu.setTranslateY(height*0.15);


        //Play Button
        btnPlay.layoutXProperty().setValue(width*0.378);
        btnPlay.layoutYProperty().setValue(height*0.195);
        btnPlay.setPrefWidth(width*0.181);
        btnPlay.setPrefHeight(height*0.07);
        btnPlay.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.03));


        //Highscore Button
        btnHighscore.layoutXProperty().setValue(width*0.378);
        btnHighscore.layoutYProperty().setValue(height*0.305);
        btnHighscore.setPrefWidth(width*0.181);
        btnHighscore.setPrefHeight(height*0.07);
        btnHighscore.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.03));


        //Settings Button
        btnSettings.layoutXProperty().setValue(width*0.378);
        btnSettings.layoutYProperty().setValue(height*0.415);
        btnSettings.setPrefWidth(width*0.181);
        btnSettings.setPrefHeight(height*0.07);
        btnSettings.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.03));


        //New Game Button
        btnNGame.layoutXProperty().setValue(width*0.20);
        btnNGame.layoutYProperty().setValue(height*0.25);
        btnNGame.setPrefWidth(width*0.181);
        btnNGame.setPrefHeight(height*0.07);
        btnNGame.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.03));


        //Load Game Button
        btnLGame.layoutXProperty().setValue(width*0.60);
        btnLGame.layoutYProperty().setValue(height*0.25);
        btnLGame.setPrefWidth(width*0.181);
        btnLGame.setPrefHeight(height*0.07);
        btnLGame.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.03));

        //Player vs Player Button
        btnPvP.layoutXProperty().setValue(width*0.10);
        btnPvP.layoutYProperty().setValue(height*0.25);
        btnPvP.setPrefWidth(width*0.181);
        btnPvP.setPrefHeight(height*0.07);
        btnPvP.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.02));

        //Player vs Ki Button
        btnPvK.layoutXProperty().setValue(width*0.40);
        btnPvK.layoutYProperty().setValue(height*0.25);
        btnPvK.setPrefWidth(width*0.181);
        btnPvK.setPrefHeight(height*0.07);
        btnPvK.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.02));

        //Ki vs Ki Button
        btnKvK.layoutXProperty().setValue(width*0.70);
        btnKvK.layoutYProperty().setValue(height*0.25);
        btnKvK.setPrefWidth(width*0.181);
        btnKvK.setPrefHeight(height*0.07);
        btnKvK.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.02));


        //Spieler1 Textfeld
        txtfPlayer1.layoutXProperty().setValue(width*0.1);
        txtfPlayer1.layoutYProperty().setValue(height*0.1);
        txtfPlayer1.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Spieler2 Textfeld
        txtfPlayer2.layoutXProperty().setValue(width*0.6);
        txtfPlayer2.layoutYProperty().setValue(height*0.1);
        txtfPlayer2.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Netzwerk Checkbox
        cboxNetGame.layoutXProperty().setValue(width*0.1);
        cboxNetGame.layoutYProperty().setValue(height*0.2);
        cboxNetGame.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Easy Computer1 Checkbox
        rbEasy1.layoutXProperty().setValue(width*0.1);
        rbEasy1.layoutYProperty().setValue(height*0.3);
        rbEasy1.setToggleGroup(group1);
        rbEasy1.setSelected(true);
        rbEasy1.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Normal Computer1 Checkbox
        rbNormal1.layoutXProperty().setValue(width*0.2);
        rbNormal1.layoutYProperty().setValue(height*0.3);
        rbNormal1.setToggleGroup(group1);
        rbNormal1.setSelected(true);
        rbNormal1.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Hard Computer1 Checkbox
        rbHard1.layoutXProperty().setValue(width*0.3);
        rbHard1.layoutYProperty().setValue(height*0.3);
        rbHard1.setToggleGroup(group1);
        rbHard1.setSelected(true);
        rbHard1.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Easy Computer2 Checkbox
        rbEasy2.layoutXProperty().setValue(width*0.6);
        rbEasy2.layoutYProperty().setValue(height*0.3);
        rbEasy2.setToggleGroup(group2);
        rbEasy2.setSelected(true);
        rbEasy2.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Normal Computer2 Checkbox
        rbNormal2.layoutXProperty().setValue(width*0.7);
        rbNormal2.layoutYProperty().setValue(height*0.3);
        rbNormal2.setToggleGroup(group2);
        rbNormal2.setSelected(true);
        rbNormal2.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Hard Computer2 Checkbox
        rbHard2.layoutXProperty().setValue(width*0.8);
        rbHard2.layoutYProperty().setValue(height*0.3);
        rbHard2.setToggleGroup(group2);
        rbHard2.setSelected(true);
        rbHard2.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //10x10 RadioButton
        rb10x10.layoutXProperty().setValue(width*0.1);
        rb10x10.layoutYProperty().setValue(height*0.45);
        rb10x10.setToggleGroup(group3);
        rb10x10.setSelected(true);
        rb10x10.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //15x15 RadioButton
        rb15x15.layoutXProperty().setValue(width*0.3);
        rb15x15.layoutYProperty().setValue(height*0.45);
        rb15x15.setToggleGroup(group3);
        rb15x15.setSelected(true);
        rb15x15.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //20x20 RadioButton
        rb20x20.layoutXProperty().setValue(width*0.5);
        rb20x20.layoutYProperty().setValue(height*0.45);
        rb20x20.setToggleGroup(group3);
        rb20x20.setSelected(true);
        rb20x20.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Go Button
        btnGo.layoutXProperty().setValue(width*0.025);
        btnGo.layoutYProperty().setValue(height*0.65);
        btnGo.setPrefWidth(width*0.12);
        btnGo.setPrefHeight(height*0.05);
        btnGo.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.03));



        //Host Button
        btnHost.layoutXProperty().setValue(width*0.20);
        btnHost.layoutYProperty().setValue(height*0.25);
        btnHost.setPrefWidth(width*0.181);
        btnHost.setPrefHeight(height*0.07);
        btnHost.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.03));


        //Client Button
        btnClient.layoutXProperty().setValue(width*0.60);
        btnClient.layoutYProperty().setValue(height*0.25);
        btnClient.setPrefWidth(width*0.181);
        btnClient.setPrefHeight(height*0.07);
        btnClient.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.03));


        //IP Textfeld
        txtfIp.layoutXProperty().setValue(width*0.3);
        txtfIp.layoutYProperty().setValue(height*0.25);
        txtfIp.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Port Textfeld
        txtfPort.layoutXProperty().setValue(width*0.5);
        txtfPort.layoutYProperty().setValue(height*0.25);
        txtfPort.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Connect Button
        btnConnect.layoutXProperty().setValue(width*0.378);
        btnConnect.layoutYProperty().setValue(height*0.415);
        btnConnect.setPrefWidth(width*0.12);
        btnConnect.setPrefHeight(height*0.05);
        btnConnect.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.02));

        //Connected Text
        txtConnected.layoutXProperty().setValue(width*0.378);
        txtConnected.layoutYProperty().setValue(height*0.195);
        txtConnected.setStroke(Color.WHITE);
        txtConnected.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.03));


        //Continue Button
        btnContinue.layoutXProperty().setValue(width*0.378);
        btnContinue.layoutYProperty().setValue(height*0.305);
        btnContinue.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.02));


        // TryAgain Button
        btnTryAgain.layoutXProperty().setValue(width*0.378);
        btnTryAgain.layoutYProperty().setValue(height*0.305);
        btnTryAgain.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.02));

        //Undo Button
        btnUndo.layoutXProperty().setValue(width*0.20);
        btnUndo.layoutYProperty().setValue(height*0.025);
        btnUndo.setPrefWidth(width*0.16);
        btnUndo.setPrefHeight(height*0.07);
        btnUndo.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.02));


        //Redo Button
        btnRedo.layoutXProperty().setValue(width*0.40);
        btnRedo.layoutYProperty().setValue(height*0.025);
        btnRedo.setPrefWidth(width*0.16);
        btnRedo.setPrefHeight(height*0.07);
        btnRedo.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.02));


        //Save Button
        btnSave.layoutXProperty().setValue(width*0.60);
        btnSave.layoutYProperty().setValue(height*0.025);
        btnSave.setPrefWidth(width*0.16);
        btnSave.setPrefHeight(height*0.07);
        btnSave.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.02));


        //Load Button
        btnLoad.layoutXProperty().setValue(width*0.80);
        btnLoad.layoutYProperty().setValue(height*0.025);
        btnLoad.setPrefWidth(width*0.16);
        btnLoad.setPrefHeight(height*0.07);
        btnLoad.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.02));


        //Random Button
        btnRndm.layoutXProperty().setValue(width*0.025);
        btnRndm.layoutYProperty().setValue(height*0.55);
        btnRndm.setPrefWidth(width*0.12);
        btnRndm.setPrefHeight(height*0.05);
        btnRndm.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Drag and Drop
        dragBox.layoutXProperty().setValue(width*0.025);
        dragBox.layoutYProperty().setValue(height*0.18);
        dragBox.setWidth(width*0.20);
        dragBox.setHeight(height*0.35);
        dragBox.setStroke(Color.WHITE);


        ivSrc2.setImage(ship2);
        ivSrc2.setFitWidth(width*0.030);
        ivSrc2.setFitHeight(width*0.015);
        ivSrc2.setTranslateX(width*0.026);
        ivSrc2.setTranslateY(height*0.28);

        ivSrc3.setImage(ship3);
        ivSrc3.setFitWidth(width*0.045);
        ivSrc3.setFitHeight(width*0.015);
        ivSrc3.setTranslateX(width*0.026);
        ivSrc3.setTranslateY(height*0.38);

        ivSrc4.setImage(ship4);
        ivSrc4.setFitWidth(width*0.060);
        ivSrc4.setFitHeight(width*0.015);
        ivSrc4.setTranslateX(width*0.026);
        ivSrc4.setTranslateY(height*0.48);

        ivSrc5.setImage(ship5);
        ivSrc5.setFitWidth(width*0.075);
        ivSrc5.setFitHeight(width*0.015);
        ivSrc5.setTranslateX(width*0.026);
        ivSrc5.setTranslateY(height*0.18);


        //Lock Button
        btnLock.layoutXProperty().setValue(width*0.36);
        btnLock.layoutYProperty().setValue(height*0.7);
        btnLock.setPrefWidth(width*0.12);
        btnLock.setPrefHeight(height*0.05);
        btnLock.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


        //Spieler1 Punkte Text
        txtPoints1.layoutXProperty().setValue(width*0.2);
        txtPoints1.layoutYProperty().setValue(height*0.12);
        txtPoints1.setStroke(Color.WHITE);
        txtPoints1.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.016));


        //Spieler2 Punkte Text
        txtPoints2.layoutXProperty().setValue(width*0.4);
        txtPoints2.layoutYProperty().setValue(height*0.12);
        txtPoints2.setStroke(Color.WHITE);
        txtPoints2.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.016));


        //Spieler1 Combo Text
        txtCombo1.layoutXProperty().setValue(width*0.6);
        txtCombo1.layoutYProperty().setValue(height*0.12);
        txtCombo1.setStroke(Color.WHITE);
        txtCombo1.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.016));


        //Spieler2 Combo Text
        txtCombo2.layoutXProperty().setValue(width*0.8);
        txtCombo2.layoutYProperty().setValue(height*0.12);
        txtCombo2.setStroke(Color.WHITE);
        txtCombo2.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.016));


        //EndScreen Text
        txtEndScreen.layoutXProperty().setValue(width*0.4);
        txtEndScreen.layoutYProperty().setValue(height*0.15);
        txtEndScreen.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.05));
        txtEndScreen.setStroke(Color.WHITE);


        //PlayAgain Button
        btnPlayAgain.layoutXProperty().setValue(width*0.20);
        btnPlayAgain.layoutYProperty().setValue(height*0.4);
        btnPlayAgain.setPrefWidth(width*0.181);
        btnPlayAgain.setPrefHeight(height*0.07);
        btnPlayAgain.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.02));


        //Leave Button
        btnLeave.layoutXProperty().setValue(width*0.60);
        btnLeave.layoutYProperty().setValue(height*0.4);
        btnLeave.setPrefWidth(width*0.181);
        btnLeave.setPrefHeight(height*0.07);
        btnLeave.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.02));

        //setEndScreen();


        tbHighscore = new TableView();
        data = getInitialTableData();
        tbHighscore.setItems(data);


        //Highscore Tabelle
        tbHighscore.setPrefWidth(width);
        tbHighscore.setPrefHeight(height);

        //Positionsspalte
        TableColumn positionColumn = new TableColumn("Position");
        positionColumn.setMinWidth(width*0.05);
        positionColumn.setCellValueFactory(new PropertyValueFactory("position"));

        // Namens Spalte
        TableColumn nameColumn = new TableColumn("Playername");
        nameColumn.setMinWidth(width*0.3);
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));

        // Punkte Spalte
        TableColumn pointsColumn = new TableColumn("Points");
        pointsColumn.setMinWidth(width*0.24);
        pointsColumn.setCellValueFactory(new PropertyValueFactory("points"));

        // Datums Spalte
        TableColumn dateColumn = new TableColumn("Date");
        dateColumn.setMinWidth(width*0.15);
        dateColumn.setCellValueFactory(new PropertyValueFactory("date"));

        // Hinzuf�gen der Spalten
        tbHighscore.getColumns().addAll(positionColumn, nameColumn, pointsColumn, dateColumn);

        tbHighscore.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());

        //SavedGames
        tbSavedGames = new TableView();
        data2 = getInitialTableData();
        tbSavedGames.setItems(data2);


        //SavedGames Tabelle
        tbSavedGames.setPrefWidth(width);
        tbSavedGames.setPrefHeight(height);

        //Spielname
        TableColumn gamenameColumn = new TableColumn("Gamename");
        gamenameColumn.setMinWidth(width*0.2);
        gamenameColumn.setCellValueFactory(new PropertyValueFactory("gamename"));

        //Spieler1 Namens Spalte
        TableColumn name1Column = new TableColumn("Player 1");
        name1Column.setMinWidth(width*0.24);
        name1Column.setCellValueFactory(new PropertyValueFactory("name1"));

        //Spieler2 Namens Spalte
        TableColumn name2Column = new TableColumn("Player 2");
        name2Column.setMinWidth(width*0.24);
        name2Column.setCellValueFactory(new PropertyValueFactory("name2"));

        //Modus Spalte
        TableColumn modeColumn = new TableColumn("Mode");
        modeColumn.setMinWidth(width*0.15);
        modeColumn.setCellValueFactory(new PropertyValueFactory("mode"));

        // Datums Spalte
        TableColumn datesColumn = new TableColumn("Date");
        datesColumn.setMinWidth(width*0.15);
        datesColumn.setCellValueFactory(new PropertyValueFactory("date"));

        // Hinzuf�gen der Spalten
        tbSavedGames.getColumns().addAll(gamenameColumn, name1Column, name2Column, modeColumn, dateColumn);

        tbSavedGames.getSelectionModel().selectedIndexProperty().addListener(new RowSelectChangeListener());


        //Ende Button
        btnEGame.layoutXProperty().setValue(width*0.8);
        btnEGame.layoutYProperty().setValue(height*0.4);
        btnEGame.setPrefWidth(150);
        btnEGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.01));


        //Hinzuf�gen der Elemente zu den Panes
        header.getChildren().addAll(hlOverall, btnMenu);
        //body.getChildren().add(startbildschirm);
        //foot.getChildren().addAll(btnEGame);


        // Erstellen einer Scene
        Scene scene = new Scene(root, width, height);


        // Stage die angezeigt wird
        primaryStage.setTitle("Project: shipZ");
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(700);
        primaryStage.sizeToScene();
        scene.getStylesheets().add(newGUI.class.getResource("newGUICSS.css").toExternalForm());
        primaryStage.show();


        // ActionEvents
        /**
         * Event beim Bet�tigen des Men� Buttons
         */
        btnMenu.setOnMouseClicked(event -> {

            body.getChildren().clear();
            body.getChildren().addAll(mainMenu, btnPlay, btnHighscore, btnSettings);

            body.setOnMouseClicked(eventbody -> {


                body.getChildren().clear();
                body.getChildren().add(startbildschirm);

            });

        });


        /**
         * Event beim Bet�tigen des Play Buttons
         */
        btnPlay.setOnMouseClicked(event -> {

            body.getChildren().clear();
            body.getChildren().addAll(btnNGame, btnLGame);
            body.setOnMouseClicked(eventbody -> {

            });


        });


        /**
         * Event beim Bet�tigen des Highscore Buttons
         */
        btnHighscore.setOnMouseClicked(event -> {

            body.getChildren().clear();
            body.getChildren().add(tbHighscore);
            body.setOnMouseClicked(eventbody -> {

            });


        });


        /**
         * Event beim Bet�tigen des New Game Buttons
         */
        btnNGame.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.getChildren().addAll(btnPvP, btnPvK, btnKvK);
                body.setOnMouseClicked(eventbody -> {
                });


            }
        });

        btnLGame.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                fireGameEvent(LOAD_EVENT);
                body.getChildren().addAll(tbSavedGames);
                body.setOnMouseClicked(eventbody -> {
                });


            }
        });

/*
        tbSavedGames.setOnMousePressed(new EventHandler<MouseEvent>() {
            TableRow row;
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2 && (! row.isEmpty()) ) {

                    Node node = ((Node) event.getTarget()).getParent();
                    row = (TableRow) node.getParent();
                    System.out.println(row.getItem());

                }
                else
                    System.out.println("fail");
            }
        });
*/

        tbSavedGames.setRowFactory( tv -> {
            TableRow<SavedGames> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    String rowData = row.getItem().getGamename();
                    gamename = rowData;
                    body.getChildren().clear();
                    drawName(1);
                    body.getChildren().addAll(dragBox, ivSrc1, btnUndo, btnRedo, btnSave, btnLoad, txtPoints1, txtCombo1, txtPoints2, txtCombo2, btnRndm, btnLock);
                    fireGameEvent(LOAD_TABLE_EVENT);
                }
            });
            return row;
        });

        /**
         * Event beim Bet�tigen des Highscore Buttons
         */
        btnHighscore.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                fireGameEvent(HIGHSCORE_EVENT);


            }
        });


        /**
         * Event beim Bet�tigen des PvP Buttons
         */
        btnPvP.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.getChildren().addAll(txtfPlayer1, txtfPlayer2, cboxNetGame,rb10x10, rb15x15, rb20x20, btnGo);
                body.setOnMouseClicked(eventbody -> {
                });
                fireGameEvent(PVP_EVENT);

            }

        });


        /**
         * Event beim Bet�tigen des PvK Buttons
         */
        btnPvK.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.getChildren().addAll(txtfPlayer1, txtfPlayer2, cboxNetGame, rbEasy2, rbNormal2, rbHard2, rb10x10, rb15x15, rb20x20, btnGo);
                body.setOnMouseClicked(eventbody -> {
                });
                fireGameEvent(PVK_EVENT);

            }

        });


        /**
         * Event beim Bet�tigen des KvK Buttons
         */
        btnKvK.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.getChildren().addAll(txtfPlayer1, txtfPlayer2, cboxNetGame, rbEasy1, rbNormal1, rbHard1, rbEasy2, rbNormal2, rbHard2,rb10x10, rb15x15, rb20x20, btnGo);
                body.setOnMouseClicked(eventbody -> {
                });
                fireGameEvent(KVK_EVENT);

            }

        });

        btnUndo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                fireGameEvent(UNDO_EVENT);

            }
        });

        btnRedo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                fireGameEvent(REDO_EVENT);

            }
        });


        /**
         * Event beim Bet�tigen des Go Buttons
         */
        btnGo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.setOnMouseClicked(eventbody -> {
                });

                setPlayernames(txtfPlayer1.getText(), txtfPlayer2.getText());

                if (rbEasy1.isSelected())
                    ki1Mode = 1;
                else if (rbNormal1.isSelected())
                    ki1Mode = 2;
                else if (rbHard1.isSelected())
                    ki1Mode = 3;
                else
                    ki1Mode = 4;

                if (rbEasy2.isSelected())
                    ki2Mode = 1;
                else if (rbNormal2.isSelected())
                    ki2Mode = 2;
                else if (rbHard2.isSelected())
                    ki2Mode = 3;
                else
                    ki2Mode = 4;


                if (rb10x10.isSelected())
                    setFieldSize(10);
                else if (rb15x15.isSelected())
                    setFieldSize(15);
                else if (rb20x20.isSelected())
                    setFieldSize(20);



                if(!cboxNetGame.isSelected()) {
                    //drawName(1);
                    createField();
                    body.getChildren().addAll(dragBox, ivSrc1, ivSrc2, ivSrc3, ivSrc4, ivSrc5, btnUndo, btnRedo, btnSave, btnLoad, txtPoints1, txtCombo1, txtPoints2, txtCombo2, btnRndm, btnLock);
                }
                else {
                    body.getChildren().addAll(btnHost, btnClient);
                }

            }

        });




        /**
         * Event beim Bet�tigen des Random Buttons
         */
        btnHost.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.getChildren().addAll(txtfPort, btnConnect);
                isHost = true;

            }
        });

        /**
         * Event beim Bet�tigen des Connect Buttons
         */
        btnConnect.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                ip = txtfIp.getText();
                port = txtfPort.getText();
                fireGameEvent(CONNECT_EVENT);

                if(connected){
                    txtConnected.setText("Connection successful!");
                    body.getChildren().clear();
                    body.getChildren().addAll(txtConnected, btnContinue);
                } else {
                    txtConnected.setText("Connection failed!");
                    body.getChildren().clear();
                    body.getChildren().addAll(txtConnected, btnTryAgain);
                }

            }
        });

        /**
         * Event beim Bet�tigen des Random Buttons
         */
        btnClient.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.getChildren().addAll(txtfIp, txtfPort, btnConnect);
                isHost = false;

            }
        });


        /**
         * Event beim Bet�tigen des TryAgain Buttons
         */
        btnTryAgain.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if(isHost){
                    body.getChildren().clear();
                    body.getChildren().addAll(txtfPort, btnConnect);
                } else {
                    body.getChildren().clear();
                    body.getChildren().addAll(txtfIp, txtfPort, btnConnect);
                }

            }
        });


        /**
         * Event beim Bet�tigen des Continue Buttons
         */
        btnContinue.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                drawName(1);
                createField();
                body.getChildren().addAll(dragBox, ivSrc1, ivSrc2, ivSrc3, ivSrc4, ivSrc5, btnUndo, btnRedo, btnSave, btnLoad, txtPoints1, txtCombo1, txtPoints2, txtCombo2, btnRndm, btnLock);

            }
        });

        /**
         * Event beim Bet�tigen des Random Buttons
         */
        btnRndm.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                //Event zur zufalls Platzierung
                fireGameEvent(FILL_EVENT);


            }
        });

        btnLoad.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                boolean b = alertbox.displayLoad();
                if(b) {
                    fireGameEvent(LOAD_EVENT);
                }
            }
        });

        /**
         * Event beim Bet�tigen des Save Buttons
         */
        btnSave.setOnAction(e -> alertbox.displaySave());


        /**
         * Event beim Bet�tigen des Lock Buttons
         */
        btnLock.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().removeAll(btnRndm, btnLock, ivSrc1, ivSrc2, ivSrc3, ivSrc4, ivSrc5, dragBox);
                fireGameEvent(LOCK_EVENT);

            }
        });


        /**
         * Event zum anpassen der Scene size
         */

        double height = 0;
        double width = 0;

/*        scene.setOnMouseClicked(event -> {

//            height = scene.getHeight();
//            width = scene.getWidth();

            System.out.println(width);
            System.out.println(height);

            header.setPrefHeight(height*0.25);
            body.setPrefHeight(height*0.8);
            foot.setPrefHeight(height*0.05);

            hlOverall.layoutXProperty().setValue(width*0.03);
            hlOverall.layoutYProperty().setValue(height*0.14);
            //hlOverall.setFont(javafx.scene.text.Font.font("Rockwell", height*0.12));
            hlOverall.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.12));

            startbildschirm.setFitWidth(body.getWidth());
            startbildschirm.setFitHeight(body.getHeight());
            startbildschirm.setTranslateX(width*0.15);

            btnMenu.setTranslateX(width*0.9);
            btnMenu.setTranslateY(height*0.05);

            mainMenu.setFitWidth(width*0.239);
            mainMenu.setFitHeight(height*0.385);
            mainMenu.setTranslateX(width*0.35);
            mainMenu.setTranslateY(height*0.15);

            btnPlay.layoutXProperty().setValue(width*0.378);
            btnPlay.layoutYProperty().setValue(height*0.195);
            btnPlay.setPrefWidth(width*0.181);
            btnPlay.setPrefHeight(height*0.07);
            //btnPlay.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

            btnHighscore.layoutXProperty().setValue(width*0.378);
            btnHighscore.layoutYProperty().setValue(height*0.305);
            btnHighscore.setPrefWidth(width*0.181);
            btnHighscore.setPrefHeight(height*0.07);
            //btnHighscore.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

            btnSettings.layoutXProperty().setValue(width*0.378);
            btnSettings.layoutYProperty().setValue(height*0.415);
            btnSettings.setPrefWidth(width*0.181);
            btnSettings.setPrefHeight(height*0.07);
            //btnSettings.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

            btnNGame.layoutXProperty().setValue(width*0.20);
            btnNGame.layoutYProperty().setValue(height*0.25);
            btnNGame.setPrefWidth(width*0.181);
            btnNGame.setPrefHeight(height*0.07);
            //btnNGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

            btnLGame.layoutXProperty().setValue(width*0.60);
            btnLGame.layoutYProperty().setValue(height*0.25);
            btnLGame.setPrefWidth(width*0.181);
            btnLGame.setPrefHeight(height*0.07);
            //btnLGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


            btnPvP.layoutXProperty().setValue(width*0.10);
            btnPvP.layoutYProperty().setValue(height*0.25);
            btnPvP.setPrefWidth(width*0.181);
            btnPvP.setPrefHeight(height*0.07);

            btnPvK.layoutXProperty().setValue(width*0.40);
            btnPvK.layoutYProperty().setValue(height*0.25);
            btnPvK.setPrefWidth(width*0.181);
            btnPvK.setPrefHeight(height*0.07);;

            btnKvK.layoutXProperty().setValue(width*0.70);
            btnKvK.layoutYProperty().setValue(height*0.25);
            btnKvK.setPrefWidth(width*0.181);
            btnKvK.setPrefHeight(height*0.07);

            cboxNetGame.layoutXProperty().setValue(100);
            cboxNetGame.layoutYProperty().setValue(200);

            btnGo.layoutXProperty().setValue(width*0.025);
            btnGo.layoutYProperty().setValue(height*0.55);
            btnGo.setPrefWidth(width*0.12);
            btnGo.setPrefHeight(height*0.05);
            //btnGo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

            btnHost.layoutXProperty().setValue(width*0.20);
            btnHost.layoutYProperty().setValue(height*0.25);
            btnHost.setPrefWidth(width*0.181);
            btnHost.setPrefHeight(height*0.07);
            //btnHost.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

            btnClient.layoutXProperty().setValue(width*0.60);
            btnClient.layoutYProperty().setValue(height*0.25);
            btnClient.setPrefWidth(width*0.181);
            btnClient.setPrefHeight(height*0.07);
            //btnClient.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

            txtfIp.layoutXProperty().setValue(width*0.3);
            txtfIp.layoutYProperty().setValue(height*0.25);

            txtfPort.layoutXProperty().setValue(width*0.5);
            txtfPort.layoutYProperty().setValue(height*0.25);

            btnConnect.layoutXProperty().setValue(width*0.378);
            btnConnect.layoutYProperty().setValue(height*0.415);
            btnConnect.setPrefWidth(width*0.12);
            btnConnect.setPrefHeight(height*0.05);

            btnUndo.layoutXProperty().setValue(width*0.1);
            btnUndo.layoutYProperty().setValue(height*0.025);
            btnUndo.setPrefWidth(width*0.16);
            btnUndo.setPrefHeight(height*0.07);
            //btnUndo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

            btnRedo.layoutXProperty().setValue(width*0.3);
            btnRedo.layoutYProperty().setValue(height*0.025);
            btnRedo.setPrefWidth(width*0.16);
            btnRedo.setPrefHeight(height*0.07);
            //btnRedo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

            btnSave.layoutXProperty().setValue(width*0.5);
            btnSave.layoutYProperty().setValue(height*0.025);
            btnSave.setPrefWidth(width*0.16);
            btnSave.setPrefHeight(height*0.07);
            //btnSave.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

            btnLoad.layoutXProperty().setValue(width*0.7);
            btnLoad.layoutYProperty().setValue(height*0.025);
            btnLoad.setPrefWidth(width*0.16);
            btnLoad.setPrefHeight(height*0.07);
            //btnLoad.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

            btnRndm.layoutXProperty().setValue(width*0.025);
            btnRndm.layoutYProperty().setValue(height*0.55);
            btnRndm.setPrefWidth(width*0.12);
            btnRndm.setPrefHeight(height*0.05);
            //btnRndm.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

            dragBox.layoutXProperty().setValue(width*0.025);
            dragBox.layoutYProperty().setValue(height*0.18);
            dragBox.setWidth(200);
            dragBox.setHeight(350);
            dragBox.setStroke(Color.WHITE);

            ivSrc1.setTranslateX(width*0.026);
            ivSrc1.setTranslateY(height*0.18);

            btnEGame.layoutXProperty().setValue(width*0.8);
            btnEGame.layoutYProperty().setValue(height*0.4);
            btnEGame.setPrefWidth(150);
            btnEGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.01));

            btnLock.layoutXProperty().setValue(width*0.36);
            btnLock.layoutYProperty().setValue(height*0.7);
            btnLock.setPrefWidth(width*0.12);
            btnLock.setPrefHeight(height*0.05);
            //btnLock.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.01));

            txtPoints1.layoutXProperty().setValue(width*0.15);
            txtPoints1.layoutYProperty().setValue(height*0.12);

            txtPoints2.layoutXProperty().setValue(width*0.55);
            txtPoints2.layoutYProperty().setValue(height*0.12);

            txtCombo1.layoutXProperty().setValue(width*0.35);
            txtCombo1.layoutYProperty().setValue(height*0.12);

            txtCombo2.layoutXProperty().setValue(width*0.75);
            txtCombo2.layoutYProperty().setValue(height*0.12);

            tbHighscore.setPrefWidth(width);
            tbHighscore.setPrefHeight(height);

            positionColumn.setMinWidth(width*0.05);

            nameColumn.setMinWidth(width*0.2);

            pointsColumn.setMinWidth(width*0.14);

            comboColumn.setMinWidth(width*0.14);

            dateColumn.setMinWidth(width*0.1);

        });
*/

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//                System.out.println("Width: " + newSceneWidth);
                double width = (double) newSceneWidth;

                hlOverall.layoutXProperty().setValue(width*0.03);
                //hlOverall.setFont(javafx.scene.text.Font.font("Rockwell", height*0.12));
//                hlOverall.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.12));

                startbildschirm.setFitWidth(body.getWidth());
                startbildschirm.setFitHeight(body.getHeight());
                startbildschirm.setTranslateX(width*0.15);

                btnMenu.setTranslateX(width*0.9);

                mainMenu.setFitWidth(width*0.239);
                mainMenu.setTranslateX(width*0.35);

                btnPlay.layoutXProperty().setValue(width*0.378);
                btnPlay.setPrefWidth(width*0.181);
                //btnPlay.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnHighscore.layoutXProperty().setValue(width*0.378);
                btnHighscore.setPrefWidth(width*0.181);
                //btnHighscore.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnSettings.layoutXProperty().setValue(width*0.378);
                btnSettings.setPrefWidth(width*0.181);
                //btnSettings.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnNGame.layoutXProperty().setValue(width*0.20);
                btnNGame.setPrefWidth(width*0.181);
                //btnNGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnLGame.layoutXProperty().setValue(width*0.60);
                btnLGame.setPrefWidth(width*0.181);
                //btnLGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


                btnPvP.layoutXProperty().setValue(width*0.10);
                btnPvP.setPrefWidth(width*0.181);

                btnPvK.layoutXProperty().setValue(width*0.40);
                btnPvK.setPrefWidth(width*0.181);

                btnKvK.layoutXProperty().setValue(width*0.70);
                btnKvK.setPrefWidth(width*0.181);

                //Spieler1 Textfeld
                txtfPlayer1.layoutXProperty().setValue(width*0.1);

                //Spieler2 Textfeld
                txtfPlayer2.layoutXProperty().setValue(width*0.6);

                //Netzwerk Checkbox
                cboxNetGame.layoutXProperty().setValue(width*0.1);


                //Easy Computer1 Checkbox
                rbEasy1.layoutXProperty().setValue(width*0.1);


                //Normal Computer1 Checkbox
                rbNormal1.layoutXProperty().setValue(width*0.2);


                //Hard Computer1 Checkbox
                rbHard1.layoutXProperty().setValue(width*0.3);


                //Easy Computer2 Checkbox
                rbEasy2.layoutXProperty().setValue(width*0.6);


                //Normal Computer2 Checkbox
                rbNormal2.layoutXProperty().setValue(width*0.7);


                //Hard Computer2 Checkbox
                rbHard2.layoutXProperty().setValue(width*0.8);


                //10x10 RadioButton
                rb10x10.layoutXProperty().setValue(width*0.1);


                //15x15 RadioButton
                rb15x15.layoutXProperty().setValue(width*0.3);


                //20x20 RadioButton
                rb20x20.layoutXProperty().setValue(width*0.5);


                //Go Button
                btnGo.layoutXProperty().setValue(width*0.025);
                btnGo.setPrefWidth(width*0.12);

                btnHost.layoutXProperty().setValue(width*0.20);
                btnHost.setPrefWidth(width*0.181);
                //btnHost.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnClient.layoutXProperty().setValue(width*0.60);
                btnClient.setPrefWidth(width*0.181);
                //btnClient.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                txtfIp.layoutXProperty().setValue(width*0.3);

                txtfPort.layoutXProperty().setValue(width*0.5);

                btnConnect.layoutXProperty().setValue(width*0.378);
                btnConnect.setPrefWidth(width*0.12);

                btnUndo.layoutXProperty().setValue(width*0.20);
                btnUndo.setPrefWidth(width*0.16);
                //btnUndo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnRedo.layoutXProperty().setValue(width*0.40);
                btnRedo.setPrefWidth(width*0.16);
                //btnRedo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnSave.layoutXProperty().setValue(width*0.60);
                btnSave.setPrefWidth(width*0.16);
                //btnSave.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnLoad.layoutXProperty().setValue(width*0.80);
                btnLoad.setPrefWidth(width*0.16);
                //btnLoad.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnRndm.layoutXProperty().setValue(width*0.025);
                btnRndm.setPrefWidth(width*0.12);
                //btnRndm.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                dragBox.layoutXProperty().setValue(width*0.025);
                dragBox.setWidth(width*0.20);

                ivSrc1.setFitWidth(width*0.015);
                ivSrc1.setFitHeight(width*0.015);
                ivSrc1.setTranslateX(width*0.026);

                ivSrc2.setFitWidth(width*0.030);
                ivSrc2.setFitHeight(width*0.015);
                ivSrc2.setTranslateX(width*0.026);

                btnEGame.layoutXProperty().setValue(width*0.8);
                btnEGame.setPrefWidth(150);
//                btnEGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.01));

                btnLock.layoutXProperty().setValue(width*0.36);
                btnLock.setPrefWidth(width*0.12);
                //btnLock.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.01));

                txtPoints1.layoutXProperty().setValue(width*0.2);

                txtPoints2.layoutXProperty().setValue(width*0.4);

                txtCombo1.layoutXProperty().setValue(width*0.6);

                txtCombo2.layoutXProperty().setValue(width*0.8);

                tbHighscore.setPrefWidth(width);

                positionColumn.setMinWidth(width*0.05);

                nameColumn.setMinWidth(width*0.2);

                pointsColumn.setMinWidth(width*0.14);

                dateColumn.setMinWidth(width*0.1);

                for(int i = 0; i < field1.length; i++) {

                    for (int j = 0; j < field1[i].length; j++) {
                        int x = i;
                        int y = j;

                        //ImageView platzieren
                        field1[i][j].setFitWidth(width*0.015);
                        field1[i][j].setPreserveRatio(true);
                        field1[i][j].setTranslateX(j * width*0.015);
                        field1[i][j].setTranslateY(i * width*0.015);
                        field1[i][j].layoutXProperty().setValue(width*0.25);

                        //ImageView platzieren
                        field2[i][j].setFitWidth(width*0.015);
                        field2[i][j].setPreserveRatio(true);
                        field2[i][j].setTranslateX(j * width*0.015);
                        field2[i][j].setTranslateY(i * width*0.015);
                        field2[i][j].layoutXProperty().setValue(width*0.65);

                    }

                }
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
//                System.out.println("Height: " + newSceneHeight);

                double height = (double) newSceneHeight;

                header.setPrefHeight(height*0.25);
                body.setPrefHeight(height*0.8);
                foot.setPrefHeight(height*0.05);

                hlOverall.layoutYProperty().setValue(height*0.14);
                //hlOverall.setFont(javafx.scene.text.Font.font("Rockwell", height*0.12));
//                hlOverall.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.12));

                startbildschirm.setFitWidth(body.getWidth());
                startbildschirm.setFitHeight(body.getHeight());

                btnMenu.setTranslateY(height*0.05);

                mainMenu.setFitHeight(height*0.385);
                mainMenu.setTranslateY(height*0.15);

                btnPlay.layoutYProperty().setValue(height*0.195);
                btnPlay.setPrefHeight(height*0.07);
                //btnPlay.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnHighscore.layoutYProperty().setValue(height*0.305);
                btnHighscore.setPrefHeight(height*0.07);
                //btnHighscore.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnSettings.layoutYProperty().setValue(height*0.415);
                btnSettings.setPrefHeight(height*0.07);
                //btnSettings.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnNGame.layoutYProperty().setValue(height*0.25);
                btnNGame.setPrefHeight(height*0.07);
                //btnNGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnLGame.layoutYProperty().setValue(height*0.25);
                btnLGame.setPrefHeight(height*0.07);
                //btnLGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


                btnPvP.layoutYProperty().setValue(height*0.25);
                btnPvP.setPrefHeight(height*0.07);

                btnPvK.layoutYProperty().setValue(height*0.25);
                btnPvK.setPrefHeight(height*0.07);;

                btnKvK.layoutYProperty().setValue(height*0.25);
                btnKvK.setPrefHeight(height*0.07);

                //Spieler1 Textfeld
                txtfPlayer1.layoutYProperty().setValue(height*0.1);
                txtfPlayer1.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


                //Spieler2 Textfeld
                txtfPlayer2.layoutYProperty().setValue(height*0.1);
                txtfPlayer2.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


                //Netzwerk Checkbox
                cboxNetGame.layoutYProperty().setValue(height*0.2);
                cboxNetGame.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


                //Easy Computer1 Checkbox
                rbEasy1.layoutYProperty().setValue(height*0.3);
                rbEasy1.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


                //Normal Computer1 Checkbox
                rbNormal1.layoutYProperty().setValue(height*0.3);
                rbNormal1.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


                //Hard Computer1 Checkbox
                rbHard1.layoutYProperty().setValue(height*0.3);
                rbHard1.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


                //Easy Computer2 Checkbox
                rbEasy2.layoutYProperty().setValue(height*0.3);
                rbEasy2.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


                //Normal Computer2 Checkbox
                rbNormal2.layoutYProperty().setValue(height*0.3);
                rbNormal2.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


                //Hard Computer2 Checkbox
                rbHard2.layoutYProperty().setValue(height*0.3);
                rbHard2.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


                //10x10 RadioButton
                rb10x10.layoutYProperty().setValue(height*0.45);
                rb10x10.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


                //15x15 RadioButton
                rb15x15.layoutYProperty().setValue(height*0.45);
                rb15x15.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


                //20x20 RadioButton
                rb20x20.layoutYProperty().setValue(height*0.45);
                rb20x20.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.015));


                //Go Button
                btnGo.layoutYProperty().setValue(height*0.65);
                btnGo.setPrefHeight(height*0.05);
                btnGo.setFont(javafx.scene.text.Font.loadFont(newGUI.class.getResource("Tempesta.ttf").toExternalForm(), height*0.03));

                btnHost.layoutYProperty().setValue(height*0.25);
                btnHost.setPrefHeight(height*0.07);
                //btnHost.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnClient.layoutYProperty().setValue(height*0.25);
                btnClient.setPrefHeight(height*0.07);
                //btnClient.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                txtfIp.layoutYProperty().setValue(height*0.25);

                txtfPort.layoutYProperty().setValue(height*0.25);

                btnConnect.layoutYProperty().setValue(height*0.415);
                btnConnect.setPrefHeight(height*0.05);

                btnUndo.layoutYProperty().setValue(height*0.025);
                btnUndo.setPrefHeight(height*0.07);
                //btnUndo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnRedo.layoutYProperty().setValue(height*0.025);
                btnRedo.setPrefHeight(height*0.07);
                //btnRedo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnSave.layoutYProperty().setValue(height*0.025);
                btnSave.setPrefHeight(height*0.07);
                //btnSave.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnLoad.layoutYProperty().setValue(height*0.025);
                btnLoad.setPrefHeight(height*0.07);
                //btnLoad.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnRndm.layoutYProperty().setValue(height*0.55);
                btnRndm.setPrefHeight(height*0.05);
                //btnRndm.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                dragBox.layoutYProperty().setValue(height*0.18);
                dragBox.setHeight(height*0.35);

                ivSrc1.setTranslateY(height*0.18);

                ivSrc2.setTranslateY(height*0.28);

                btnEGame.layoutYProperty().setValue(height*0.4);
                btnEGame.setPrefWidth(150);
//              btnEGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.01));

                btnLock.layoutYProperty().setValue(height*0.7);
                btnLock.setPrefHeight(height*0.05);
                //btnLock.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.01));

                txtPoints1.layoutYProperty().setValue(height*0.12);

                txtPoints2.layoutYProperty().setValue(height*0.12);

                txtCombo1.layoutYProperty().setValue(height*0.12);

                txtCombo2.layoutYProperty().setValue(height*0.12);

                tbHighscore.setPrefHeight(height);

                for(int i = 0; i < field1.length; i++) {

                    for (int j = 0; j < field1[i].length; j++) {
                        int x = i;
                        int y = j;

                        //ImageView platzieren
                        field1[i][j].setPreserveRatio(true);
                        field1[i][j].layoutYProperty().setValue(height*0.18);

                        //ImageView platzieren
                        field2[i][j].setPreserveRatio(true);
                        field2[i][j].layoutYProperty().setValue(height*0.18);

                    }

                }


            }
        });


    }//Ende Constructor


    private class RowSelectChangeListener implements ChangeListener {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
/**
 int ix = newValue.intValue();

 if ((ix = data.size())) {

 return; // invalid data
 }

 Book book = data.get(ix);
 actionStatus.setText(book.toString());
 **/
        }
    }


}//Ende newGUI