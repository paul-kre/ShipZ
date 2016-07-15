package shipz.gui;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
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
    ImageView[][] field1 = new ImageView[fieldSize][fieldSize];
    ImageView[][] field2 = new ImageView[fieldSize][fieldSize];

    //Images
    Image white = new Image(newGUI.class.getResource("White.png").toExternalForm());
    Image water = new Image(newGUI.class.getResource("shipZ_spielfeld.png").toExternalForm());
    Image ship = new Image(newGUI.class.getResource("shipZ_1ershipIntact.png").toExternalForm());
    Image explosion = new Image(newGUI.class.getResource("shipZ_explosion2.gif").toExternalForm());
    Image explosion2 = new Image(newGUI.class.getResource("shipZ_explosion3.gif").toExternalForm());
    Image explosion3 = new Image(newGUI.class.getResource("shipZ_explosion4.gif").toExternalForm());

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


    TableView tbHighscore;
    private ObservableList data;
    ArrayList list = new ArrayList();



    //IM
    public void setPlayernames (String player1, String player2) {

        playername1 = player1;
        playername2 = player2;

    }


    public String getPlayername1 () {

        return playername1;

    }

    public String getPlayername2 () {

        return playername2;

    }

    public int getKi1Mode () {

        return ki1Mode;

    }

    public int getKi2Mode () {

        return ki2Mode;

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
     * @param body  Pane zur Anzeige
     * @param ivSrc1 Drag and Drop ImageView
     */
    public void createField (AnchorPane body, ImageView ivSrc1) {
        //Spielfeld erstellen
        for(int i = 0; i < field1.length; i++){

            for(int j = 0; j < field1[i].length; j++){
                int x = i;
                int y = j;

                //Rectangle zur Feldbegrenzung erstellen
                Rectangle border = new Rectangle(30, 30);
                border.setFill(null);
                border.setStroke(Color.BLACK);

                //ImageView erstellen und hinzuf�gen
                ImageView oneField = new ImageView();
                Image oneFieldImg = new Image(newGUI.class.getResource("White.png").toExternalForm());
                oneField.setImage(oneFieldImg);
                field1[i][j] = oneField;

                //Rectangle platzieren
                border.setWidth(30);
                border.setTranslateX(j * 30);
                border.setTranslateY(i * 30);
                border.layoutXProperty().setValue(width*0.3);
                border.layoutYProperty().setValue(height*0.18);

                //ImageView platzieren
                oneField.setFitWidth(30);
                oneField.setPreserveRatio(true);
                oneField.setTranslateX(j * 30);
                oneField.setTranslateY(i * 30);
                oneField.layoutXProperty().setValue(width*0.3);
                oneField.layoutYProperty().setValue(height*0.18);

                body.getChildren().addAll(border, oneField);

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


                    int randomNumber = random.nextInt(2);

                        if (randomNumber == 0){

                            oneField.setImage(explosion);
                        } else if ( randomNumber == 1){
                            oneField.setImage(explosion2);
                        } else {
                            oneField.setImage(explosion3);
                        }

                    }
                });

                //DropTargets

                field1[i][j].setOnDragOver(new EventHandler <DragEvent>() {public void handle(DragEvent event) {
                        /* data is dragged over the target */
                    System.out.println("onDragOver");

                        /* accept it only if it is  not dragged from the same node
                         * and if it has a string data */
                    if (event.getGestureSource() != oneField) {
                            /* allow for both copying and moving, whatever user chooses */
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
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
                        if (db.hasImage()) {
                            oneField.setImage(db.getImage());
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

                //Rectangle zur Feldbegrenzung erstellen
                Rectangle border = new Rectangle(30, 30);
                border.setFill(null);
                border.setStroke(Color.BLACK);

                //ImageView erstellen und hinzuf�gen
                ImageView oneField = new ImageView();
                Image oneFieldImg = new Image(newGUI.class.getResource("White.png").toExternalForm());
                oneField.setImage(oneFieldImg);
                field2[i][j] = oneField;

                //Rectangle platzieren
                border.setWidth(30);
                border.setTranslateX(j * 30);
                border.setTranslateY(i * 30);
                border.layoutXProperty().setValue(width*0.65);
                border.layoutYProperty().setValue(height*0.18);

                //ImageView platzieren
                oneField.setFitWidth(30);
                oneField.setPreserveRatio(true);
                oneField.setTranslateX(j * 30);
                oneField.setTranslateY(i * 30);
                oneField.layoutXProperty().setValue(width*0.65);
                oneField.layoutYProperty().setValue(height*0.18);

                body.getChildren().addAll(border, oneField);

                //auf click
                field2[i][j].setOnMouseClicked(event -> {

                    if(event.getButton() == MouseButton.PRIMARY) {
                        if(enableField==2) {
                            //Event zum �bergeben der Koordinaten
                            //Auf R�ckmeldung
                            setCoordinates(y, x);
                            fireGameEvent(GUI_SHOOT_EVENT);
                            System.out.print("test");
                        }
                    }
                    else if(event.getButton() == MouseButton.SECONDARY) {
                        oneField.setImage(water);
                    }
                });

                //DropTargets

                field2[i][j].setOnDragOver(new EventHandler <DragEvent>() {public void handle(DragEvent event) {
                        /* data is dragged over the target */
                    System.out.println("onDragOver");

                        /* accept it only if it is  not dragged from the same node
                         * and if it has a string data */
                    if (event.getGestureSource() != oneField) {
                            /* allow for both copying and moving, whatever user chooses */
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
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
                        if (db.hasImage()) {
                            oneField.setImage(db.getImage());
                            success = true;
                        }
                        /* let the source know whether the string was successfully
                         * transferred and used */
                        event.setDropCompleted(success);

                        event.consume();
                    }
                });

                // Drag and Drop
                field2[i][j].setOnDragDetected(new EventHandler <javafx.scene.input.MouseEvent>() {
                    public void handle(javafx.scene.input.MouseEvent event) {
                        /* drag was detected, start drag-and-drop gesture*/
                        System.out.println("onDragDetected");

                        /* allow any transfer mode */
                        Dragboard db = ivSrc1.startDragAndDrop(TransferMode.MOVE);

                        /* put a string on dragboard */
                        ClipboardContent content = new ClipboardContent();
                        content.putImage(ship);
                        db.setContent(content);

                        event.consume();
                    }
                });

                field2[i][j].setOnDragDone(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* the drag-and-drop gesture ended */
                        System.out.println("onDragDone");
                        /* if the data was successfully moved, clear it */
                        if (event.getTransferMode() == TransferMode.MOVE) {
                            oneField.setImage(oneFieldImg);
                        }

                        event.consume();
                    }
                });

            }

        }

        //Dragzone
        ivSrc1.setOnDragDetected(new EventHandler <javafx.scene.input.MouseEvent>() {
            public void handle(javafx.scene.input.MouseEvent event) {
                        /* drag was detected, start drag-and-drop gesture*/
                System.out.println("onDragDetected");

                        /* allow any transfer mode */
                Dragboard db = ivSrc1.startDragAndDrop(TransferMode.MOVE);

                        /* put a string on dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putImage(ship);
                db.setContent(content);

                event.consume();
            }
        });

        ivSrc1.setOnDragDone(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                        /* the drag-and-drop gesture ended */
                System.out.println("onDragDone");
                        /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {

                }

                event.consume();
            }
        });
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
                    fireGameEvent(FINISHED_ROUND);
                    break;
                case 1:
                    field1[x][y].setImage(ship);
                    break;
                case 2:
                    field1[x][y].setImage(explosion);
                    fireGameEvent(FINISHED_ROUND);
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
                    fireGameEvent(FINISHED_ROUND);
                    break;
                case 1:
                    field2[x][y].setImage(ship);
                    break;
                case 2:
                    field2[x][y].setImage(explosion);
                    fireGameEvent(FINISHED_ROUND);
                    break;
                default:
                    field2[x][y].setImage(white);
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
     * @param body      Pane
     */
    public void drawName (int player, AnchorPane body) {

        Text playername = new Text("Player "+player+" it�s your turn!");
        playername.layoutXProperty().setValue(width*0.3);
        playername.layoutYProperty().setValue(height*0.65);
        playername.setStroke(Color.WHITE);

        FadeTransition ft = new FadeTransition(Duration.millis(3000), playername);
        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        ft.play();

        body.getChildren().add(playername);


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


        list.add(new Highscore("1", "Paul", "2000", "30.09.09"));
        list.add(new Highscore("2", "Flo", "1000", "31.09.09"));
        list.add(new Highscore("3", "Sonnenschein", "900", "32.09.09"));
        list.add(new Highscore("4", "Max", "700", "33.09.09"));
        list.add(new Highscore("5", "Arthur", "500", "34.09.09"));

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
        Text hlOverall = new Text("Project: shipZ");
        hlOverall.layoutXProperty().setValue(width*0.03);
        hlOverall.layoutYProperty().setValue(height*0.14);
        //hlOverall.setFont(javafx.scene.text.Font.font("Rockwell", height*0.12));
        hlOverall.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.12));


        //Startbildschirm
        ImageView startbildschirm = new ImageView();
        Image startbild = new Image(newGUI.class.getResource("Startbildschirm.png").toExternalForm());
        startbildschirm.setImage(startbild);
        startbildschirm.setFitWidth(body.getWidth());
        startbildschirm.setFitHeight(body.getHeight());
        startbildschirm.setTranslateX(width*0.15);



        //Explosion
        ImageView explosionStart = new ImageView();
        Image explosion = new Image(newGUI.class.getResource("Explosion.png").toExternalForm());
        explosionStart.setImage(explosion);


        //Men�button
        ImageView btnMenu = new ImageView();
        Image menu = new Image(newGUI.class.getResource("btnMenu.png").toExternalForm());
        btnMenu.setImage(menu);
        btnMenu.setFitWidth(100);
        btnMenu.setFitHeight(100);
        btnMenu.setTranslateX(width*0.9);
        btnMenu.setTranslateY(height*0.05);


        //Hauptmen�
        ImageView mainMenu = new ImageView();
        Image imgMainMenu = new Image(newGUI.class.getResource("Menu.png").toExternalForm());
        mainMenu.setImage(imgMainMenu);
        mainMenu.setFitWidth(width*0.239);
        mainMenu.setFitHeight(height*0.385);
        mainMenu.setTranslateX(width*0.35);
        mainMenu.setTranslateY(height*0.15);


        //Play Button
        Button btnPlay = new Button("Play");
        btnPlay.layoutXProperty().setValue(width*0.378);
        btnPlay.layoutYProperty().setValue(height*0.195);
        btnPlay.setPrefWidth(width*0.181);
        btnPlay.setPrefHeight(height*0.07);
        //btnPlay.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


        //Highscore Button
        Button btnHighscore = new Button("Highscore");
        btnHighscore.layoutXProperty().setValue(width*0.378);
        btnHighscore.layoutYProperty().setValue(height*0.305);
        btnHighscore.setPrefWidth(width*0.181);
        btnHighscore.setPrefHeight(height*0.07);
        //btnHighscore.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


        //Settings Button
        Button btnSettings = new Button("Settings");
        btnSettings.layoutXProperty().setValue(width*0.378);
        btnSettings.layoutYProperty().setValue(height*0.415);
        btnSettings.setPrefWidth(width*0.181);
        btnSettings.setPrefHeight(height*0.07);
        //btnSettings.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


        //New Game Button
        Button btnNGame = new Button("New Game");
        btnNGame.layoutXProperty().setValue(width*0.20);
        btnNGame.layoutYProperty().setValue(height*0.25);
        btnNGame.setPrefWidth(width*0.181);
        btnNGame.setPrefHeight(height*0.07);
        //btnNGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


        //Load Game Button
        Button btnLGame = new Button("Load Game");
        btnLGame.layoutXProperty().setValue(width*0.60);
        btnLGame.layoutYProperty().setValue(height*0.25);
        btnLGame.setPrefWidth(width*0.181);
        btnLGame.setPrefHeight(height*0.07);
        //btnLGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

        //Player vs Player Button
        Button btnPvP = new Button("Player vs Player");
        btnPvP.layoutXProperty().setValue(width*0.10);
        btnPvP.layoutYProperty().setValue(height*0.25);
        btnPvP.setPrefWidth(width*0.181);
        btnPvP.setPrefHeight(height*0.07);

        //Player vs Ki Button
        Button btnPvK = new Button("Player vs Ki");
        btnPvK.layoutXProperty().setValue(width*0.40);
        btnPvK.layoutYProperty().setValue(height*0.25);
        btnPvK.setPrefWidth(width*0.181);
        btnPvK.setPrefHeight(height*0.07);;

        //Ki vs Ki Button
        Button btnKvK = new Button("Ki vs Ki");
        btnKvK.layoutXProperty().setValue(width*0.70);
        btnKvK.layoutYProperty().setValue(height*0.25);
        btnKvK.setPrefWidth(width*0.181);
        btnKvK.setPrefHeight(height*0.07);


        //Spieler1 Textfeld
        TextField txtfPlayer1 = new TextField("Player 1");
        txtfPlayer1.layoutXProperty().setValue(width*0.1);
        txtfPlayer1.layoutYProperty().setValue(height*0.1);


        //Spieler2 Textfeld
        TextField txtfPlayer2 = new TextField("Player 2");
        txtfPlayer2.layoutXProperty().setValue(width*0.6);
        txtfPlayer2.layoutYProperty().setValue(height*0.1);


        //Netzwerk Checkbox
        CheckBox cboxNetGame = new CheckBox("Networkgame");
        cboxNetGame.layoutXProperty().setValue(width*0.1);
        cboxNetGame.layoutYProperty().setValue(height*0.2);


        //Togglegruppe
        final ToggleGroup group1 = new ToggleGroup();


        //Easy Computer2 Checkbox
        RadioButton rbEasy1 = new RadioButton("Easy");
        rbEasy1.layoutXProperty().setValue(width*0.1);
        rbEasy1.layoutYProperty().setValue(height*0.4);
        rbEasy1.setToggleGroup(group1);
        rbEasy1.setSelected(true);


        //Normal Computer2 Checkbox
        RadioButton rbNormal1 = new RadioButton("Normal");
        rbNormal1.layoutXProperty().setValue(width*0.2);
        rbNormal1.layoutYProperty().setValue(height*0.4);
        rbNormal1.setToggleGroup(group1);
        rbNormal1.setSelected(true);


        //Hard Computer2 Checkbox
        RadioButton rbHard1 = new RadioButton("Hard");
        rbHard1.layoutXProperty().setValue(width*0.3);
        rbHard1.layoutYProperty().setValue(height*0.4);
        rbHard1.setToggleGroup(group1);
        rbHard1.setSelected(true);


        //Togglegruppe
        final ToggleGroup group2 = new ToggleGroup();


        //Easy Computer2 Checkbox
        RadioButton rbEasy2 = new RadioButton("Easy");
        rbEasy2.layoutXProperty().setValue(width*0.6);
        rbEasy2.layoutYProperty().setValue(height*0.4);
        rbEasy2.setToggleGroup(group2);
        rbEasy2.setSelected(true);


        //Normal Computer2 Checkbox
        RadioButton rbNormal2 = new RadioButton("Normal");
        rbNormal2.layoutXProperty().setValue(width*0.7);
        rbNormal2.layoutYProperty().setValue(height*0.4);
        rbNormal2.setToggleGroup(group2);
        rbNormal2.setSelected(true);


        //Hard Computer2 Checkbox
        RadioButton rbHard2 = new RadioButton("Hard");
        rbHard2.layoutXProperty().setValue(width*0.8);
        rbHard2.layoutYProperty().setValue(height*0.4);
        rbHard2.setToggleGroup(group2);
        rbHard2.setSelected(true);


        //Go Button
        Button btnGo = new Button("Go!");
        btnGo.layoutXProperty().setValue(width*0.025);
        btnGo.layoutYProperty().setValue(height*0.55);
        btnGo.setPrefWidth(width*0.12);
        btnGo.setPrefHeight(height*0.05);
        //btnGo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


        //Host Button
        Button btnHost = new Button("Host");
        btnHost.layoutXProperty().setValue(width*0.20);
        btnHost.layoutYProperty().setValue(height*0.25);
        btnHost.setPrefWidth(width*0.181);
        btnHost.setPrefHeight(height*0.07);
        //btnHost.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


        //Client Button
        Button btnClient = new Button("Client");
        btnClient.layoutXProperty().setValue(width*0.60);
        btnClient.layoutYProperty().setValue(height*0.25);
        btnClient.setPrefWidth(width*0.181);
        btnClient.setPrefHeight(height*0.07);
        //btnClient.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


        //IP Textfeld
        TextField txtfIp = new TextField("IP");
        txtfIp.layoutXProperty().setValue(width*0.3);
        txtfIp.layoutYProperty().setValue(height*0.25);


        //Port Textfeld
        TextField txtfPort = new TextField("Port");
        txtfPort.layoutXProperty().setValue(width*0.5);
        txtfPort.layoutYProperty().setValue(height*0.25);


        //Connect Button
        Button btnConnect = new Button("Connect");
        btnConnect.layoutXProperty().setValue(width*0.378);
        btnConnect.layoutYProperty().setValue(height*0.415);
        btnConnect.setPrefWidth(width*0.12);
        btnConnect.setPrefHeight(height*0.05);


        //Undo Button
        Button btnUndo = new Button("Undo");
        btnUndo.layoutXProperty().setValue(width*0.1);
        btnUndo.layoutYProperty().setValue(height*0.025);
        btnUndo.setPrefWidth(width*0.16);
        btnUndo.setPrefHeight(height*0.07);
        //btnUndo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


        //Redo Button
        Button btnRedo = new Button("Redo");
        btnRedo.layoutXProperty().setValue(width*0.3);
        btnRedo.layoutYProperty().setValue(height*0.025);
        btnRedo.setPrefWidth(width*0.16);
        btnRedo.setPrefHeight(height*0.07);
        //btnRedo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


        //Save Button
        Button btnSave = new Button("Save");
        btnSave.layoutXProperty().setValue(width*0.5);
        btnSave.layoutYProperty().setValue(height*0.025);
        btnSave.setPrefWidth(width*0.16);
        btnSave.setPrefHeight(height*0.07);
        //btnSave.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


        //Load Button
        Button btnLoad = new Button("Load");
        btnLoad.layoutXProperty().setValue(width*0.7);
        btnLoad.layoutYProperty().setValue(height*0.025);
        btnLoad.setPrefWidth(width*0.16);
        btnLoad.setPrefHeight(height*0.07);
        //btnLoad.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


        //Random Button
        Button btnRndm = new Button("Random");
        btnRndm.layoutXProperty().setValue(width*0.025);
        btnRndm.layoutYProperty().setValue(height*0.55);
        btnRndm.setPrefWidth(width*0.12);
        btnRndm.setPrefHeight(height*0.05);
        //btnRndm.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));


        //Drag and Drop
        Rectangle dragBox = new Rectangle();
        ImageView ivSrc1 = new ImageView();
        ImageView ivT = new ImageView();
        Image img1 = new Image(newGUI.class.getResource("shipZ_1ershipIntact.png").toExternalForm());
        Image img2 = new Image(newGUI.class.getResource("shipZ_spielfeld.png").toExternalForm());

        dragBox.layoutXProperty().setValue(width*0.025);
        dragBox.layoutYProperty().setValue(height*0.18);
        dragBox.setWidth(200);
        dragBox.setHeight(350);
        dragBox.setStroke(Color.WHITE);

        ivSrc1.setImage(img1);
        ivSrc1.setFitWidth(35);
        ivSrc1.setFitHeight(35);
        ivSrc1.setTranslateX(width*0.026);
        ivSrc1.setTranslateY(height*0.18);


        //Lock Button
        Button btnLock = new Button("Lock");
        btnLock.layoutXProperty().setValue(width*0.36);
        btnLock.layoutYProperty().setValue(height*0.7);
        btnLock.setPrefWidth(width*0.12);
        btnLock.setPrefHeight(height*0.05);
        //btnLock.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.01));


        //Spieler1 Punkte Text
        txtPoints1.layoutXProperty().setValue(width*0.15);
        txtPoints1.layoutYProperty().setValue(height*0.12);
        txtPoints1.setStroke(Color.WHITE);


        //Spieler2 Punkte Text
        txtPoints2.layoutXProperty().setValue(width*0.55);
        txtPoints2.layoutYProperty().setValue(height*0.12);
        txtPoints2.setStroke(Color.WHITE);


        //Spieler1 Combo Text
        txtCombo1.layoutXProperty().setValue(width*0.35);
        txtCombo1.layoutYProperty().setValue(height*0.12);
        txtCombo1.setStroke(Color.WHITE);


        //Spieler2 Combo Text
        txtCombo2.layoutXProperty().setValue(width*0.75);
        txtCombo2.layoutYProperty().setValue(height*0.12);
        txtCombo2.setStroke(Color.WHITE);


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

        //test
        //setNewRow("hi");


        //Ende Button
        Button btnEGame = new Button("End game");
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


        /**
         * Event beim Bet�tigen des PvP Buttons
         */
        btnPvP.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.getChildren().addAll(txtfPlayer1, txtfPlayer2, cboxNetGame, btnGo);
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
                body.getChildren().addAll(txtfPlayer1, txtfPlayer2, cboxNetGame, rbEasy2, rbNormal2, rbHard2, btnGo);
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
                body.getChildren().addAll(txtfPlayer1, txtfPlayer2, cboxNetGame, rbEasy1, rbNormal1, rbHard1, rbEasy2, rbNormal2, rbHard2, btnGo);
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

                if(cboxNetGame.isSelected()==false) {

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


                    drawName(1, body);
                    createField(body, ivSrc1);
                    body.getChildren().addAll(dragBox, ivSrc1, btnUndo, btnRedo, btnSave, btnLoad, txtPoints1, txtCombo1, txtPoints2, txtCombo2, btnRndm, btnLock);
                }
                else {

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

                fireGameEvent(LOAD_EVENT);

            }
        });

        /**
         * Event beim Bet�tigen des Save Buttons
         */
        btnSave.setOnAction(e -> alertbox.display("Save", "Enter a filename!"));

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

                cboxNetGame.layoutXProperty().setValue(100);
                cboxNetGame.layoutYProperty().setValue(200);

                btnGo.layoutXProperty().setValue(width*0.025);
                btnGo.setPrefWidth(width*0.12);
                //btnGo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

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

                btnUndo.layoutXProperty().setValue(width*0.1);
                btnUndo.setPrefWidth(width*0.16);
                //btnUndo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnRedo.layoutXProperty().setValue(width*0.3);
                btnRedo.setPrefWidth(width*0.16);
                //btnRedo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnSave.layoutXProperty().setValue(width*0.5);
                btnSave.setPrefWidth(width*0.16);
                //btnSave.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnLoad.layoutXProperty().setValue(width*0.7);
                btnLoad.setPrefWidth(width*0.16);
                //btnLoad.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                btnRndm.layoutXProperty().setValue(width*0.025);
                btnRndm.setPrefWidth(width*0.12);
                //btnRndm.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

                dragBox.layoutXProperty().setValue(width*0.025);
                dragBox.setWidth(200);
                dragBox.setHeight(350);
                dragBox.setStroke(Color.WHITE);

                ivSrc1.setTranslateX(width*0.026);

                btnEGame.layoutXProperty().setValue(width*0.8);
                btnEGame.setPrefWidth(150);
//                btnEGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.01));

                btnLock.layoutXProperty().setValue(width*0.36);
                btnLock.setPrefWidth(width*0.12);
                //btnLock.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.01));

                txtPoints1.layoutXProperty().setValue(width*0.15);

                txtPoints2.layoutXProperty().setValue(width*0.55);

                txtCombo1.layoutXProperty().setValue(width*0.35);

                txtCombo2.layoutXProperty().setValue(width*0.75);

                tbHighscore.setPrefWidth(width);

                positionColumn.setMinWidth(width*0.05);

                nameColumn.setMinWidth(width*0.2);

                pointsColumn.setMinWidth(width*0.14);

                dateColumn.setMinWidth(width*0.1);
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

                cboxNetGame.layoutXProperty().setValue(100);
                cboxNetGame.layoutYProperty().setValue(200);

                btnGo.layoutYProperty().setValue(height*0.55);
                btnGo.setPrefHeight(height*0.05);
                //btnGo.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.03));

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
                dragBox.setWidth(200);
                dragBox.setHeight(350);
                dragBox.setStroke(Color.WHITE);

                ivSrc1.setTranslateY(height*0.18);

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


