package shipz.gui;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import shipz.util.GameEventSource;
//import shipz.util.GameEventSource;

import java.awt.*;
import java.awt.Font;
import java.awt.event.MouseEvent;

/**
 * Created by nnamf on 26.06.2016.
 */
public class newGUI extends GameEventSource {

    //IV

    // HinzufÃ¼gen einer VBox zum layouten
    VBox root = new VBox();

    //HinzufÃ¼gen von Panes
    AnchorPane header = new AnchorPane();
    AnchorPane body = new AnchorPane();
    AnchorPane foot = new AnchorPane();

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    //double width = screenSize.getWidth();
    //double height = screenSize.getHeight();
    double width = 1200;
    double height = 800;
    int fieldSize = 10;
    ImageView[][] field1 = new ImageView[fieldSize][fieldSize];
    ImageView[][] field2 = new ImageView[fieldSize][fieldSize];
    Image white = new Image(newGUI.class.getResource("White.png").toExternalForm());
    Image water = new Image(newGUI.class.getResource("BG.png").toExternalForm());
    Image ship = new Image(newGUI.class.getResource("Raumschiff1.png").toExternalForm());
    Image explosion = new Image(newGUI.class.getResource("Explosion.png").toExternalForm());
    int enableField = 0;

    //Koordinaten
    int xC;
    int yC;

    //IM

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
                Rectangle border = new Rectangle(35, 35);
                border.setFill(null);
                border.setStroke(Color.BLACK);

                //ImageView erstellen und hinzufügen
                ImageView oneField = new ImageView();
                Image oneFieldImg = new Image(newGUI.class.getResource("White.png").toExternalForm());
                oneField.setImage(oneFieldImg);
                field1[i][j] = oneField;

                //Rectangle platzieren
                border.setWidth(35);
                border.setTranslateX(j * 35);
                border.setTranslateY(i * 35);
                border.layoutXProperty().setValue(width*0.2);
                border.layoutYProperty().setValue(height*0.18);

                //ImageView platzieren
                oneField.setFitWidth(35);
                oneField.setPreserveRatio(true);
                oneField.setTranslateX(j * 35);
                oneField.setTranslateY(i * 35);
                oneField.layoutXProperty().setValue(width*0.2);
                oneField.layoutYProperty().setValue(height*0.18);

                body.getChildren().addAll(border, oneField);

                //auf click
                field1[i][j].setOnMouseClicked(event -> {

                    if(event.getButton() == MouseButton.PRIMARY) {
                        if(enableField==1) {
                            //Event zum übergeben der Koordinaten
                            //Auf Rückmeldung
                            setCoordinates(y, x);
                            fireGameEvent(GUI_SHOOT_EVENT);
                        }
                    }
                    else if(event.getButton() == MouseButton.SECONDARY) {
                        oneField.setImage(oneFieldImg);
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
                Rectangle border = new Rectangle(35, 35);
                border.setFill(null);
                border.setStroke(Color.BLACK);

                //ImageView erstellen und hinzufügen
                ImageView oneField = new ImageView();
                Image oneFieldImg = new Image(newGUI.class.getResource("White.png").toExternalForm());
                oneField.setImage(oneFieldImg);
                field2[i][j] = oneField;

                //Rectangle platzieren
                border.setWidth(35);
                border.setTranslateX(j * 35);
                border.setTranslateY(i * 35);
                border.layoutXProperty().setValue(width*0.5);
                border.layoutYProperty().setValue(height*0.18);

                //ImageView platzieren
                oneField.setFitWidth(35);
                oneField.setPreserveRatio(true);
                oneField.setTranslateX(j * 35);
                oneField.setTranslateY(i * 35);
                oneField.layoutXProperty().setValue(width*0.5);
                oneField.layoutYProperty().setValue(height*0.18);

                body.getChildren().addAll(border, oneField);

                //auf click
                field2[i][j].setOnMouseClicked(event -> {

                    if(event.getButton() == MouseButton.PRIMARY) {
                        if(enableField==2) {
                            //Event zum übergeben der Koordinaten
                            //Auf Rückmeldung
                            setCoordinates(y, x);
                            fireGameEvent(GUI_SHOOT_EVENT);
                        }
                    }
                    else if(event.getButton() == MouseButton.SECONDARY) {
                        oneField.setImage(oneFieldImg);
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
     * Methode zum Ändern des Zustandes der GUI (enable/disable)
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

        Text playername = new Text("Player "+player+" it´s your turn!");
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

    //Constructor
    public newGUI (Stage primaryStage) {


        //Anpassen der Panes
        header.setPrefHeight(height*0.25);

        body.setPrefHeight(height*0.8);
        body.setId("body");

        foot.setPrefHeight(height*0.05);


        // HinzufÃ¼gen der Panes zur VBox
        root.getChildren().add(header);
        root.getChildren().add(body);
        root.getChildren().add(foot);


        //Hauptüberschrift
        Text hlOverall = new Text("Project: shipZ");
        hlOverall.layoutXProperty().setValue(width*0.03);
        hlOverall.layoutYProperty().setValue(height*0.14);
        //hlOverall.setFont(javafx.scene.text.Font.font("Rockwell", height*0.12));
        hlOverall.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.12));


        //Startbildschirm
        ImageView startbildschirm = new ImageView();
        Image startbild = new Image(newGUI.class.getResource("Startbildschirm.png").toExternalForm());
        startbildschirm.setImage(startbild);
        startbildschirm.setFitWidth(1000);
        startbildschirm.setFitHeight(750);
        startbildschirm.setTranslateX(width*0.15);


        //Explosion
        ImageView explosionStart = new ImageView();
        Image explosion = new Image(newGUI.class.getResource("Explosion.png").toExternalForm());
        explosionStart.setImage(explosion);


        //Menübutton
        ImageView btnMenu = new ImageView();
        Image menu = new Image(newGUI.class.getResource("btnMenu.png").toExternalForm());
        btnMenu.setImage(menu);
        btnMenu.setFitWidth(100);
        btnMenu.setFitHeight(100);
        btnMenu.setTranslateX(width*0.9);
        btnMenu.setTranslateY(height*0.05);


        //Hauptmenü
        ImageView mainMenu = new ImageView();
        Image imgMainMenu = new Image(newGUI.class.getResource("Menu.png").toExternalForm());
        mainMenu.setImage(imgMainMenu);
        mainMenu.setFitWidth(400);
        mainMenu.setFitHeight(400);
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

        //Netzwerk Checkbox
        CheckBox cboxNetGame = new CheckBox("Networkgame");
        cboxNetGame.layoutXProperty().setValue(100);
        cboxNetGame.layoutYProperty().setValue(200);


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
        Image img1 = new Image(GUI.class.getResource("Raumschiff1.png").toExternalForm());
        Image img2 = new Image(GUI.class.getResource("BG.png").toExternalForm());

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


        //Ende Button
        Button btnEGame = new Button("End game");
        btnEGame.layoutXProperty().setValue(width*0.8);
        btnEGame.layoutYProperty().setValue(height*0.4);
        btnEGame.setPrefWidth(150);
        btnEGame.setFont(javafx.scene.text.Font.loadFont("file:/C:/Users/nnamf/Downloads/videophreak/VIDEOPHREAK.ttf", height*0.01));


        //Hinzufügen der Elemente zu den Panes
        header.getChildren().addAll(hlOverall, btnMenu);
        //body.getChildren().add(startbildschirm);
        //foot.getChildren().addAll(btnEGame);


        // Erstellen einer Scene
        Scene scene = new Scene(root, width, height);


        // Stage die angezeigt wird
        primaryStage.setTitle("Project: shipZ");
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.sizeToScene();
        scene.getStylesheets().add(GUI.class.getResource("newGUICSS.css").toExternalForm());
        primaryStage.show();


        // ActionEvents
        /**
         * Event beim Betätigen des Menü Buttons
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
         * Event beim Betätigen des Play Buttons
         */
        btnPlay.setOnMouseClicked(event -> {

            body.getChildren().clear();
            body.getChildren().addAll(btnNGame, btnLGame);


        });

        /**
         * Event beim Betätigen des New Game Buttons
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
         * Event beim Betätigen des PvP Buttons
         */
        btnPvP.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.getChildren().addAll(cboxNetGame, btnGo);
                body.setOnMouseClicked(eventbody -> {
                });
                fireGameEvent(PVP_EVENT);
            }

        });

        /**
         * Event beim Betätigen des PvK Buttons
         */
        btnPvK.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.getChildren().addAll(cboxNetGame, btnGo);
                body.setOnMouseClicked(eventbody -> {
                });
                fireGameEvent(PVK_EVENT);
            }

        });


        /**
         * Event beim Betätigen des KvK Buttons
         */
        btnKvK.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.getChildren().addAll(cboxNetGame, btnGo);
                body.setOnMouseClicked(eventbody -> {
                });
                fireGameEvent(KVK_EVENT);
            }

        });


        /**
         * Event beim Betätigen des Go Buttons
         */
        btnGo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.setOnMouseClicked(eventbody -> {
                });

                if(cboxNetGame.isSelected()==false) {
                    drawName(1, body);
                    createField(body, ivSrc1);
                    body.getChildren().addAll(dragBox, ivSrc1, btnUndo, btnRedo, btnSave, btnLoad, btnRndm);
                }
                else {
                    body.getChildren().addAll(btnHost, btnClient);
                }

            }

        });


        /**
         * Event beim Betätigen des Random Buttons
         */
        btnClient.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.getChildren().addAll(txtfIp, txtfPort, btnConnect);

            }
        });


        /**
         * Event beim Betätigen des Random Buttons
         */
        btnRndm.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                //Event zur zufalls Platzierung
                fireGameEvent(FILL_EVENT);


            }
        });


        /**
         * Event beim Betätigen des Save Buttons
         */
        btnSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                //Event zur zufalls Platzierung
                fireGameEvent(SAVE_EVENT);


            }
        });

        /**
         * Event beim Betätigen des Undo Buttons
         */
        btnUndo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                //Event zur zufalls Platzierung
                fireGameEvent(UNDO_EVENT);


            }
        });

        /**
         * Event beim Betätigen des Redo Buttons
         */
        btnRedo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                //Event zur zufalls Platzierung
                fireGameEvent(REDO_EVENT);


            }
        });

        /**
         * Event zum anpassen der Scene size
         */
        /*
        scene.s(event -> {
 
            height = scene.getHeight();
            width = scene.getWidth();
 
            System.out.println(width);
            System.out.println(height);
 
            header.setPrefHeight(height*0.25);
            body.setPrefHeight(height*0.8);
            foot.setPrefHeight(height*0.05);
            btnMenu.setTranslateX(width*0.9);
            btnMenu.setTranslateY(height*0.05);
 
        });
        */


    }//Ende Constructor

}//Ende newGUI