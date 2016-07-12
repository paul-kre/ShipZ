package shipz.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;

import static com.sun.java.accessibility.util.AWTEventMonitor.addComponentListener;
import static java.awt.SystemColor.window;

/**
 * Created by nnamf on 13.06.2016.
 */

public class GUI {

    //IV
    ImageView ivg = new ImageView();
    int fieldSize = 10;
    Tile[][] t1 = new Tile[fieldSize][fieldSize];
    Tile[][] t2 = new Tile[fieldSize][fieldSize];
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();

    //Constructor
    public GUI(Stage primaryStage) {

        // Hinzufügen einer VBox zum layouten
        VBox root = new VBox();

        //Hinzufügen von Panes
        AnchorPane header = new AnchorPane();
        AnchorPane game = new AnchorPane();
        AnchorPane foot = new AnchorPane();

        // Controlelemente
        Text hlOverall = new Text("Project shipZ");
        Text hlSettings = new Text("Settings");
        Text hlGame = new Text("Game");
        Text hlLGame = new Text("Load Game");
        Text hlHighscore = new Text("Highscore");
        TextField txtfP1 = new TextField("Player 1");
        TextField txtfP2 = new TextField("Player 2");
        Button btn0 = new Button("New Window");
        Button btnNGame = new Button("New Game");
        Button btnLGame = new Button("Load Game");
        Button btnHighscore = new Button("Highscore");
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
        Rectangle dragBox = new Rectangle();
        ImageView ivSrc1 = new ImageView();
        ImageView ivT = new ImageView();
        Image img1 = new Image(GUI.class.getResource("Raumschiff1.png").toExternalForm());
        Image img2 = new Image(GUI.class.getResource("BG.png").toExternalForm());


        // Anpassen der Panes
        game.setId("ig");
        game.setPrefHeight(height*0.8);
        header.setPrefHeight(height*0.25);
        foot.setPrefHeight(height*0.05);

        // Positionieren und anpassen der Controlelemente
        hlOverall.layoutXProperty().setValue(width*0.14);
        hlOverall.layoutYProperty().setValue(height*0.14);
        hlOverall.setFont(Font.font("", height*0.12));

        hlSettings.layoutXProperty().setValue(width*0.001);
        hlSettings.layoutYProperty().setValue(height*0.001);
        hlSettings.setFont(Font.font("", height*0.06));
        //hlSettings.setFill(Color.WHITE);

        hlGame.layoutXProperty().setValue(1);
        hlGame.layoutYProperty().setValue(1);
        hlGame.setFont(Font.font("", 60));

        hlHighscore.layoutXProperty().setValue(1);
        hlHighscore.layoutYProperty().setValue(1);
        hlHighscore.setFont(Font.font("", 60));

        btn0.layoutXProperty().setValue(30);
        btn0.layoutYProperty().setValue(30);

        btnNGame.layoutXProperty().setValue(width*0.02);
        btnNGame.layoutYProperty().setValue(height*0.04);
        btnNGame.setPrefWidth(width*0.09);
        btnNGame.setPrefHeight(height*0.02);

        btnLGame.layoutXProperty().setValue(width*0.02);
        btnLGame.layoutYProperty().setValue(height*0.09);
        btnLGame.setPrefWidth(width*0.09);
        btnLGame.setPrefHeight(height*0.02);

        btnHighscore.layoutXProperty().setValue(width*0.02);
        btnHighscore.layoutYProperty().setValue(height*0.14);
        btnHighscore.setPrefWidth(width*0.09);
        btnHighscore.setPrefHeight(height*0.02);

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

        // Hinzuf�gen der Spalten
        tbHighscore.getColumns().addAll(positionColumn, nameColumn, pointsColumn);


        // Hinzufügen der Panes zur VBox
        root.getChildren().add(header);
        root.getChildren().add(game);
        root.getChildren().add(foot);


        // Hinzufügen der Controlelemente
        header.getChildren().addAll(btnNGame, btnLGame, btnHighscore, hlOverall);
        foot.getChildren().addAll(btnEGame);


        // Erstellen einer Scene
        Scene scene = new Scene(root, width, height);

        // Stage die angezeigt wird
        primaryStage.setTitle("Multiple Window");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        scene.getStylesheets().add(GUI.class.getResource("GUICSS.css").toExternalForm());
        primaryStage.show();



        // ActionEvents
        btn0.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //new NewWindow();
            }
        });

        btnNGame.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                game.getChildren().clear();
                game.getChildren().addAll(rbtnPvP, rbtnPvK, rbtnKvK, cboxNetGame, hlSettings, txtfP1, txtfP2, slFieldSize, btnStart);

            }
        });

        btnLGame.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                AlertBox.display("Network Connection", "Choose:");

            }
        });

        btnHighscore.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                game.getChildren().clear();
                game.getChildren().addAll(hlHighscore, tbHighscore);

            }
        });

        btnStart.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                game.getChildren().clear();
                game.getChildren().addAll(dragBox, ivSrc1, btnUndo, btnRedo, btnSave, btnExit, hlGame);
                createField(game);
                for(int i = 0; i < t1.length; i++){

                    for(int j = 0; j < t1[i].length; j++){

                        Tile tile = new Tile();
                        t1[i][j] = tile;
                        tile.setTranslateX(j * 35);
                        tile.setTranslateY(i * 35);
                        tile.layoutXProperty().setValue(300);
                        tile.layoutYProperty().setValue(180);
                        ImageView ivTar;
                        ivTar = tile.getImageView();
                        tile.background(t1[i][j].iv1);

                        game.getChildren().add(tile);

                    }

                }

                for(int i = 0; i < t2.length; i++){

                    for(int j = 0; j < t2[i].length; j++){

                        Tile tile = new Tile();
                        t2[i][j] = tile;
                        tile.setTranslateX(j * 35);
                        tile.setTranslateY(i * 35);
                        tile.layoutXProperty().setValue(750);
                        tile.layoutYProperty().setValue(180);
                        tile.background(t1[i][j].iv1);

                        game.getChildren().add(tile);

                    }

                }


                //Drag and Drop
                ivSrc1.setOnDragDetected(new EventHandler <MouseEvent>() {
                    public void handle(MouseEvent event) {
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

                t1[1][1].iv1.setOnDragOver(new EventHandler <DragEvent>() {public void handle(DragEvent event) {
                        /* data is dragged over the target */
                        System.out.println("onDragOver");

                        /* accept it only if it is  not dragged from the same node
                         * and if it has a string data */
                        if (event.getGestureSource() != t1[1][1].iv1) {
                            /* allow for both copying and moving, whatever user chooses */
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        }

                        event.consume();
                    }
                });

                t1[1][1].iv1.setOnDragEntered(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* the drag-and-drop gesture entered the target */
                        System.out.println("onDragEntered");
                        /* show to the user that it is an actual gesture target */
                        if (event.getGestureSource() != t1[1][1].iv1) {
                            t1[1][1].ship(t1[1][1].iv1);;
                        }

                        event.consume();
                    }
                });

                t1[1][1].iv1.setOnDragExited(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* mouse moved away, remove the graphical cues */
                        t1[1][1].ship(t1[1][1].iv1);;

                        event.consume();
                    }
                });

                t1[1][1].iv1.setOnDragDropped(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* data dropped */
                        System.out.println("onDragDropped");
                        /* if there is a string data on dragboard, read it and use it */
                        Dragboard db = event.getDragboard();
                        boolean success = false;
                        if (db.hasImage()) {
                            t1[1][1].iv1.setImage(db.getImage());
                            success = true;
                        }
                        /* let the source know whether the string was successfully
                         * transferred and used */
                        event.setDropCompleted(success);

                        event.consume();
                    }
                });

                ivSrc1.setOnDragDone(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        /* the drag-and-drop gesture ended */
                        System.out.println("onDragDone");
                        /* if the data was successfully moved, clear it */
                        if (event.getTransferMode() == TransferMode.MOVE) {
                            ivSrc1.setImage(img2);
                        }

                        event.consume();
                    }
                });

            }
        });

        btnExit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                game.getChildren().clear();

            }
        });



    } //Ende Constructor


    /**
     * Methode zum erschaffen 2er Spielfelder
     * @param game Anzeigefenster
     */
    public void createField (AnchorPane game) {




    } // Ende createField


    /**
     * Methode zum zeichnen eines Schiffes an die angegebene Position
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param nr Spielfeldnummer
     */
    public void drawShip(int x, int y, int nr){
        if(nr == 1) {
            t1[x][y].ship(t1[x][y].iv1);
        }
        else{
            t2[x][y].ship(t2[x][y].iv1);
        }
    }

    /**
     * Methode zum zeichnen einer Explosion an die angegebene Position
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param nr Spielfeldnummer
     */
    public void drawExplosion(int x, int y, int nr){
        if(nr == 1) {
            t1[x][y].explosion(t1[x][y].iv1);
        }
        else{
            t2[x][y].explosion(t2[x][y].iv1);
        }
    }

    /**
     * Methode zum zeichnen eines Wasserfeldes an die angegebene Position
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param nr Spielfeldnummer
     */
    public void drawWater(int x, int y, int nr){
        if(nr == 1) {
            t1[x][y].water(t1[x][y].iv1);
        }
        else{
            t2[x][y].water(t2[x][y].iv1);
        }
    }

    /**
     * Methode zum l�schen eines Images an der angegebene Position
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param nr Spielfeldnummer
     */
    public void drawDelete(int x, int y, int nr){
        if(nr == 1) {
            t1[x][y].delete(t1[x][y].iv1);
        }
        else{
            t2[x][y].delete(t2[x][y].iv1);
        }
    }



    /**
     * Methode zum Aufrufen des richtigen Bildschirms
     */
    public void switchScreens(){


    }



    /**
     *
     * @author nnamf
     */
    public class Tile extends StackPane {

        //IV
        private Text text = new Text();
        public int filled = 0;
        private ImageView iv;
        private ImageView iv1;

        //Constructor
        public Tile () {
            Rectangle border = new Rectangle(35, 35);
            border.setFill(null);
            border.setStroke(Color.BLACK);
            Image img = new Image(GUI.class.getResource("BG.png").toExternalForm());



            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);


            iv = new ImageView();
            iv1 = new ImageView();


            iv.setImage(img);
            iv.setFitWidth(35);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            iv.setCache(true);
            iv1.setFitWidth(35);
            iv1.setPreserveRatio(true);
            iv1.setSmooth(true);
            iv1.setCache(true);
            getChildren().add(iv);
            getChildren().add(iv1);


            setOnMouseClicked(event -> {

                if(event.getButton() == MouseButton.PRIMARY) {
                    if(filled == 0){
                        ship(iv1);
                    }
                    else
                        explosion(iv1);
                }
                else if(event.getButton() == MouseButton.SECONDARY) {
                    if(filled == 0){
                        water(iv1);
                    }
                    else
                        delete(iv1);
                }
            });
        }

        private ImageView getImageView(){
            return iv1;
        }

        private void ship(ImageView iv1) {
            Image img1 = new Image(GUI.class.getResource("Raumschiff1.png").toExternalForm());
            iv1.setImage(img1);
            filled = 1;
        }

        private void explosion(ImageView iv1) {
            Image img2 = new Image(GUI.class.getResource("Explosion.png").toExternalForm());
            iv1.setImage(img2);
            filled = 0;
        }

        private void water(ImageView iv1) {
            Image img3 = new Image(GUI.class.getResource("Blackout.png").toExternalForm());
            iv1.setImage(img3);
            filled = 1;
        }

        private void delete(ImageView iv1) {
            iv1.setImage(null);
            filled = 0;
        }

        private void background(ImageView iv1) {
            Image img = new Image(GUI.class.getResource("BG.png").toExternalForm());
            iv1.setImage(img);
        }


    }




    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {



    }


}
