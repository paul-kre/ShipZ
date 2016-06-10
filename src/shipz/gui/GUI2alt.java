package shipz.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 *
 * @author nnamf
 */
public class GUI2alt extends Application {
    
	//IV
	ImageView ivg = new ImageView();
    int fieldSize = 10;
    Tile[][] t1 = new Tile[fieldSize][fieldSize];
    Tile[][] t2 = new Tile[fieldSize][fieldSize];

    
    /**
     * Methode zum erschaffen 2er Spielfelder
     * @param game Anzeigefenster
     */
    public void createField (AnchorPane game) {
    	
        
        for(int i = 0; i < t1.length; i++){

            for(int j = 0; j < t1[i].length; j++){
                
                Tile tile = new Tile();
                t1[i][j] = tile;
                tile.setTranslateX(j * 35);
                tile.setTranslateY(i * 35);
                tile.layoutXProperty().setValue(100);
                tile.layoutYProperty().setValue(180);
                
                game.getChildren().add(tile);
                
            }
            
        }
        
        for(int i = 0; i < t2.length; i++){

            for(int j = 0; j < t2[i].length; j++){
                
                Tile tile = new Tile();
                t2[i][j] = tile;
                tile.setTranslateX(j * 35);
                tile.setTranslateY(i * 35);
                tile.layoutXProperty().setValue(550);
                tile.layoutYProperty().setValue(180);
                
                game.getChildren().add(tile);
                
            }
            
        }
        
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
    
    @Override
    public void start(Stage primaryStage) {
    	
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

        
        // Anpassen der Panes
        game.setId("ig");
        game.setPrefHeight(600);
        header.setPrefHeight(300);
       
        // Positionieren und anpassen der Controlelemente
        hlOverall.layoutXProperty().setValue(250);
        hlOverall.layoutYProperty().setValue(150);
        hlOverall.setFont(Font.font("", 120));
        
        hlSettings.layoutXProperty().setValue(1);
        hlSettings.layoutYProperty().setValue(1);
        hlSettings.setFont(Font.font("", 60));
        //hlSettings.setFill(Color.WHITE);
        
        hlGame.layoutXProperty().setValue(1);
        hlGame.layoutYProperty().setValue(1);
        hlGame.setFont(Font.font("", 60));
        
        hlHighscore.layoutXProperty().setValue(1);
        hlHighscore.layoutYProperty().setValue(1);
        hlHighscore.setFont(Font.font("", 60));
        
        btn0.layoutXProperty().setValue(30);
        btn0.layoutYProperty().setValue(30);
        
        btnNGame.layoutXProperty().setValue(30);
        btnNGame.layoutYProperty().setValue(50);
        btnNGame.setPrefWidth(150);
        
        btnLGame.layoutXProperty().setValue(30);
        btnLGame.layoutYProperty().setValue(100);
        btnLGame.setPrefWidth(150);
        
        btnHighscore.layoutXProperty().setValue(30);
        btnHighscore.layoutYProperty().setValue(150);
        btnHighscore.setPrefWidth(150);
        
        btnUndo.layoutXProperty().setValue(100);
        btnUndo.layoutYProperty().setValue(100);
        btnUndo.setPrefWidth(150);
        
        btnRedo.layoutXProperty().setValue(300);
        btnRedo.layoutYProperty().setValue(100);
        btnRedo.setPrefWidth(150);
        
        btnSave.layoutXProperty().setValue(550);
        btnSave.layoutYProperty().setValue(100);
        btnSave.setPrefWidth(150);
        
        btnExit.layoutXProperty().setValue(750);
        btnExit.layoutYProperty().setValue(100);
        btnExit.setPrefWidth(150);
        
        btnEGame.layoutXProperty().setValue(825);
        btnEGame.layoutYProperty().setValue(40);
        btnEGame.setPrefWidth(150);
        
        rbtnPvP.layoutXProperty().setValue(100);
        rbtnPvP.layoutYProperty().setValue(100);
        
        
        rbtnPvK.layoutXProperty().setValue(300);
        rbtnPvK.layoutYProperty().setValue(100);
        
        
        rbtnKvK.layoutXProperty().setValue(500);
        rbtnKvK.layoutYProperty().setValue(100);
        
        cboxNetGame.layoutXProperty().setValue(100);
        cboxNetGame.layoutYProperty().setValue(200);
        
        txtfP1.layoutXProperty().setValue(100);
        txtfP1.layoutYProperty().setValue(300);
        
        txtfP2.layoutXProperty().setValue(300);
        txtfP2.layoutYProperty().setValue(300);
        
        slFieldSize.layoutXProperty().setValue(100);
        slFieldSize.layoutYProperty().setValue(400);
        
        btnStart.layoutXProperty().setValue(825);
        btnStart.layoutYProperty().setValue(525);
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
        Scene scene = new Scene(root, 1000, 1000);
        
        // Stage die angezeigt wird 
        primaryStage.setTitle("Multiple Window");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(GUI2.class.getResource("GUICSS.css").toExternalForm());
        primaryStage.show();
        
        
     // ActionEvents
        btn0.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                new NewWindow();
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
		        new NewWindow();
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
            	game.getChildren().addAll(btnUndo, btnRedo, btnSave, btnExit, hlGame);
            	createField(game);
            	
            }
        });
		
		btnExit.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
            	game.getChildren().clear();
            	
            }
        });
		           

    } //Ende Start
    
    
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
            Image img = new Image(GUI2.class.getResource("BG.png").toExternalForm());
            
            
            
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
       
        private void ship(ImageView iv1) {
            Image img1 = new Image(GUI2.class.getResource("Raumschiff1.png").toExternalForm());
            iv1.setImage(img1);
            filled = 1;
        }
        
        private void explosion(ImageView iv1) {
            Image img2 = new Image(GUI2.class.getResource("Explosion.png").toExternalForm());
            iv1.setImage(img2);
            filled = 0;
        }
        
        private void water(ImageView iv1) {
            Image img3 = new Image(GUI2.class.getResource("Blackout.png").toExternalForm());
            iv1.setImage(img3);
            filled = 1;
        }
        
        private void delete(ImageView iv1) {
            iv1.setImage(null);
            filled = 0;
        }


    }
    

    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
