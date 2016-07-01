package shipz.gui;

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

import java.awt.*;
import java.awt.Font;
import java.awt.event.MouseEvent;

/**
 * Created by nnamf on 26.06.2016.
 */
public class newGUI {

    //IV
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();
    int fieldSize = 10;
    ImageView[][] field1 = new ImageView[fieldSize][fieldSize];
    ImageView[][] field2 = new ImageView[fieldSize][fieldSize];

    //Constructor
    public newGUI (Stage primaryStage) {

        // HinzufÃ¼gen einer VBox zum layouten
        VBox root = new VBox();

        //HinzufÃ¼gen von Panes
        AnchorPane header = new AnchorPane();
        AnchorPane body = new AnchorPane();
        AnchorPane foot = new AnchorPane();

        //Anpassen der Panes
        header.setPrefHeight(height*0.25);

        body.setPrefHeight(height*0.8);
        body.setId("body");
        double bodyWidth = body.getWidth();
        double bodyHeight =body.getHeight();

        foot.setPrefHeight(height*0.05);

        // HinzufÃ¼gen der Panes zur VBox
        root.getChildren().add(header);
        root.getChildren().add(body);
        root.getChildren().add(foot);

        //Elemente

        Text hlSettings = new Text("Settings");
        Text hlGame = new Text("Game");
        Text hlLGame = new Text("Load Game");
        Text hlHighscore = new Text("Highscore");
        TextField txtfP1 = new TextField("Player 1");
        TextField txtfP2 = new TextField("Player 2");
        Button btn0 = new Button("New Window");

        Button btnUndo = new Button("Undo");
        Button btnRedo = new Button("Redo");
        Button btnSave = new Button("Save");
        Button btnExit = new Button("Exit");
        Button btnStart = new Button("Start");
        Button btnEGame = new Button("End game");
        RadioButton rbtnPvP = new RadioButton("Player vs Player");
        RadioButton rbtnPvK = new RadioButton("Player vs KI");
        RadioButton rbtnKvK = new RadioButton("KI vs KI");
        CheckBox cboxNetGame = new CheckBox("Create a Networkgame");
        Slider slFieldSize = new Slider();
        TableView tbHighscore = new TableView();

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

        //Drag and Drop
        Rectangle dragBox = new Rectangle();
        ImageView ivSrc1 = new ImageView();
        ImageView ivT = new ImageView();
        Image img1 = new Image(newGUI.class.getResource("Raumschiff1.png").toExternalForm());
        Image img2 = new Image(newGUI.class.getResource("BG.png").toExternalForm());

        dragBox.layoutXProperty().setValue(width*0.025);
        dragBox.layoutYProperty().setValue(height*0.17);
        dragBox.setWidth(200);
        dragBox.setHeight(350);
        dragBox.setStroke(Color.WHITE);

        ivSrc1.setImage(img1);
        ivSrc1.setFitWidth(35);
        ivSrc1.setFitHeight(35);
        ivSrc1.setTranslateX(width*0.026);
        ivSrc1.setTranslateY(height*0.18);

        // Positionieren und anpassen der Controlelemente


        hlSettings.layoutXProperty().setValue(width*0.001);
        hlSettings.layoutYProperty().setValue(height*0.001);
        hlSettings.setFont(javafx.scene.text.Font.font("", height*0.06));
        //hlSettings.setFill(Color.WHITE);

        hlGame.layoutXProperty().setValue(1);
        hlGame.layoutYProperty().setValue(1);
        hlGame.setFont(javafx.scene.text.Font.font("", 60));

        hlHighscore.layoutXProperty().setValue(1);
        hlHighscore.layoutYProperty().setValue(1);
        hlHighscore.setFont(javafx.scene.text.Font.font("", 60));

        btn0.layoutXProperty().setValue(30);
        btn0.layoutYProperty().setValue(30);

        btnUndo.layoutXProperty().setValue(300);
        btnUndo.layoutYProperty().setValue(100);
        btnUndo.setPrefWidth(150);

        btnRedo.layoutXProperty().setValue(500);
        btnRedo.layoutYProperty().setValue(100);
        btnRedo.setPrefWidth(150);

        btnSave.layoutXProperty().setValue(750);
        btnSave.layoutYProperty().setValue(100);
        btnSave.setPrefWidth(150);

        btnExit.layoutXProperty().setValue(950);
        btnExit.layoutYProperty().setValue(100);
        btnExit.setPrefWidth(150);

        btnEGame.layoutXProperty().setValue(825);
        btnEGame.layoutYProperty().setValue(40);
        btnEGame.setPrefWidth(150);

        rbtnPvP.layoutXProperty().setValue(100);
        rbtnPvP.layoutYProperty().setValue(height*0.10);


        rbtnPvK.layoutXProperty().setValue(300);
        rbtnPvK.layoutYProperty().setValue(height*0.10);


        rbtnKvK.layoutXProperty().setValue(500);
        rbtnKvK.layoutYProperty().setValue(height*0.10);

        cboxNetGame.layoutXProperty().setValue(100);
        cboxNetGame.layoutYProperty().setValue(height*0.20);

        txtfP1.layoutXProperty().setValue(100);
        txtfP1.layoutYProperty().setValue(height*0.30);

        txtfP2.layoutXProperty().setValue(300);
        txtfP2.layoutYProperty().setValue(height*0.30);

        slFieldSize.layoutXProperty().setValue(100);
        slFieldSize.layoutYProperty().setValue(height*0.40);

        btnStart.layoutXProperty().setValue(width*0.8);
        btnStart.layoutYProperty().setValue(height*0.55);
        btnStart.setPrefWidth(150);
        btnStart.setPrefHeight(50);

        tbHighscore.layoutXProperty().setValue(100);
        tbHighscore.layoutYProperty().setValue(100);
        tbHighscore.setPrefWidth(480);

        //Positionsspalte
        TableColumn positionColumn = new TableColumn("Position");
        positionColumn.setMinWidth(20);
        positionColumn.setCellValueFactory(new PropertyValueFactory("position"));

        // Namens Spalte
        TableColumn nameColumn = new TableColumn("Playername");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));

        // Punkte Spalte
        TableColumn pointsColumn = new TableColumn("Points");
        pointsColumn.setMinWidth(200);
        pointsColumn.setCellValueFactory(new PropertyValueFactory("points"));

        // Hinzufügen der Spalten
        tbHighscore.getColumns().addAll(positionColumn, nameColumn, pointsColumn);


        //Hinzufügen der Elemente zu den Panes
        header.getChildren().addAll(hlOverall, btnMenu);
        body.getChildren().add(startbildschirm);
        foot.getChildren().addAll(btnEGame);

        // Erstellen einer Scene
        Scene scene = new Scene(root, width, height);

        // Stage die angezeigt wird
        primaryStage.setTitle("Project: shipZ");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        scene.getStylesheets().add(newGUI.class.getResource("newGUICSS.css").toExternalForm());
        primaryStage.show();

        // ActionEvents
        btnMenu.setOnMouseClicked(event -> {

            body.getChildren().clear();
            body.getChildren().addAll(mainMenu, btnPlay, btnHighscore, btnSettings);

            body.setOnMouseClicked(eventbody -> {

                body.getChildren().clear();
                body.getChildren().add(startbildschirm);

            });

        });

        btnPlay.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                body.getChildren().clear();
                body.getChildren().addAll(dragBox, ivSrc1);

                //Spielfeld erstellen
                for(int i = 0; i < field1.length; i++){

                    for(int j = 0; j < field1[i].length; j++){

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
                        border.layoutXProperty().setValue(width*0.3);
                        border.layoutYProperty().setValue(height*0.18);

                        //ImageView platzieren
                        oneField.setFitWidth(35);
                        oneField.setPreserveRatio(true);
                        oneField.setTranslateX(j * 35);
                        oneField.setTranslateY(i * 35);
                        oneField.layoutXProperty().setValue(width*0.3);
                        oneField.layoutYProperty().setValue(height*0.18);

                        body.getChildren().addAll(border, oneField);

                        //DropTargets

                        Image img1 = new Image(newGUI.class.getResource("Raumschiff1.png").toExternalForm());

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

                //Drag and Drop
                ivSrc1.setOnDragDetected(new EventHandler <javafx.scene.input.MouseEvent>() {
                    public void handle(javafx.scene.input.MouseEvent event) {
                        /* drag was detected, start drag-and-drop gesture*/
                        System.out.println("onDragDetected");

                        /* allow any transfer mode */
                        Dragboard db = ivSrc1.startDragAndDrop(TransferMode.MOVE);

                        /* put a string on dragboard */
                        ClipboardContent content = new ClipboardContent();
                        content.putImage(img1);
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

            }
        });


    }//Ende Constructor

}//Ende newGUI
