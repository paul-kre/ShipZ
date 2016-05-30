package shipz.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import shipz.Game;

/**
 *
 * @author nnamf
 */
public class GUI2 extends Application {
    
	//IV
	ImageView ivg = new ImageView();
    int fieldSize = 10;
    Tile[][] t1 = new Tile[fieldSize][fieldSize];
    Tile[][] t2 = new Tile[fieldSize][fieldSize];

    Game g = new Game(10, 10);

    
    
    public void drawShip(int x, int y, int nr){
	    if(nr == 1) {
	        t1[x][y].ship(t1[x][y].iv1);
	    }
	    else{
	        t2[x][y].ship(t2[x][y].iv1);
	    }
	}
    
    public void drawExplosion(int x, int y, int nr){
	    if(nr == 1) {
	        t1[x][y].explosion(t1[x][y].iv1);
	    }
	    else{
	        t2[x][y].explosion(t2[x][y].iv1);
	    }
	}
    
    public void drawWater(int x, int y, int nr){
	    if(nr == 1) {
	        t1[x][y].water(t1[x][y].iv1);
	    }
	    else{
	        t2[x][y].water(t2[x][y].iv1);
	    }
	}
    
    public void drawDelete(int x, int y, int nr){
	    if(nr == 1) {
	        t1[x][y].delete(t1[x][y].iv1);
	    }
	    else{
	        t2[x][y].delete(t2[x][y].iv1);
	    }
	}
    
    public void switchScreens(){
    	
    	
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        // Controlelemente
        Text txt1 = new Text("Project shipZ");
        Button btn0 = new Button("New Window");
        Button btn1 = new Button("New Game");
        Button btn2 = new Button("Load Game");
        Button btn3 = new Button("Highscore");
        Button btn4 = new Button("Undo");
        Button btn5 = new Button("Redo");
        Button btn6 = new Button("Save");
        Button btn7 = new Button("Exit");
        

        // ActionEvents
        btn0.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                new NewWindow();
            }
        });
        
        
        // Hinzufügen einer VBox zum layouten
        VBox root = new VBox();
        
        //Hinzufügen von Panes
        AnchorPane header = new AnchorPane();
        AnchorPane settings = new AnchorPane();
        AnchorPane game = new AnchorPane();
        
        // Anpassen der Panes
        game.setId("ig");
        game.setPrefHeight(700);
        header.setPrefHeight(220);
       
        // Positionieren und anpassen der Controlelemente
        btn0.layoutXProperty().setValue(30);
        btn0.layoutYProperty().setValue(30);
        
        btn1.layoutXProperty().setValue(30);
        btn1.layoutYProperty().setValue(50);
        btn1.setPrefWidth(150);
        
        btn2.layoutXProperty().setValue(30);
        btn2.layoutYProperty().setValue(100);
        btn2.setPrefWidth(150);
        
        btn3.layoutXProperty().setValue(30);
        btn3.layoutYProperty().setValue(150);
        btn3.setPrefWidth(150);
        
        txt1.layoutXProperty().setValue(250);
        txt1.layoutYProperty().setValue(150);
        txt1.setFont(Font.font("", 120));
        
        btn4.layoutXProperty().setValue(100);
        btn4.layoutYProperty().setValue(100);
        btn4.setPrefWidth(150);
        
        btn5.layoutXProperty().setValue(300);
        btn5.layoutYProperty().setValue(100);
        btn5.setPrefWidth(150);
        
        btn6.layoutXProperty().setValue(550);
        btn6.layoutYProperty().setValue(100);
        btn6.setPrefWidth(150);
        
        btn7.layoutXProperty().setValue(750);
        btn7.layoutYProperty().setValue(100);
        btn7.setPrefWidth(150);

        
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
        
        
        // Hinzufügen der Panes zur VBox
        root.getChildren().add(header);
        root.getChildren().add(game);
        
        
        // Hinzufügen der Controlelemente
        header.getChildren().addAll(btn1, btn2, btn3, txt1);
        game.getChildren().addAll(btn4, btn5, btn6, btn7);
        settings.getChildren().add(btn0);
        
        
        // Erstellen einer Scene
        Scene scene = new Scene(root, 1000, 1000);
        
        // Stage die angezeigt wird 
        primaryStage.setTitle("Multiple Window");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(GUI2.class.getResource("GUICSS.css").toExternalForm());
        primaryStage.show();

        //"Main"
        g.shipList = g.createShipList("5443332");
        System.out.println(g.shipList);
        g.player1active = true;
        g.placeShips(1);
        g.player1active = false;
        g.placeShips(2);
        g.displaySingleBoard(g.board1);
        g.displaySingleBoard(g.board2);
        g.gui = this;
        g.drawShipOnGUI();
        //drawShip(1, 1, 1);
        
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
            Image img = new Image("file:\\F:\\NetBeansProjects\\GUI\\src\\gui\\BG.png");
            
            
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
            Image img1 = new Image("file:\\F:\\NetBeansProjects\\GUI\\src\\gui\\Raumschiff1.png");
            iv1.setImage(img1);
            filled = 1;
        }
        
        private void explosion(ImageView iv1) {
            Image img2 = new Image("file:\\F:\\NetBeansProjects\\GUI\\src\\gui\\Explosion.png");
            iv1.setImage(img2);
            filled = 0;
        }
        
        private void water(ImageView iv1) {
            Image img3 = new Image("file:\\F:\\NetBeansProjects\\GUI\\src\\gui\\Blackout.png");
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
