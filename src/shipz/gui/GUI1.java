/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

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

/**
 *
 * @author nnamf
 */
public class GUI1 extends Application {
    
    
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

        
        for(int i = 0; i < 10; i++){

            for(int j = 0; j < 10; j++){
                
                Tile tile = new Tile();
                tile.setTranslateX(j * 35);
                tile.setTranslateY(i * 35);
                tile.layoutXProperty().setValue(100);
                tile.layoutYProperty().setValue(180);
                
                game.getChildren().add(tile);
                
            }
            
        }
        
        for(int i = 0; i < 10; i++){

            for(int j = 0; j < 10; j++){
                
                Tile tile = new Tile();
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
        game.getChildren().addAll(btn4, btn5, btn6);
        settings.getChildren().add(btn0);
        
        
        // Erstellen einer Scene
        Scene scene = new Scene(root, 1000, 1000);
        
        // Stage die angezeigt wird 
        primaryStage.setTitle("Multiple Window");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(GUI.class.getResource("GUICSS.css").toExternalForm());
        primaryStage.show();
        
    }
    
    
    /**
     *
     * @author nnamf
     */
    public class Tile extends StackPane {
    
        //IV
        private Text text = new Text();
        public int filled = 0;
        
        //Constructor
        public Tile () {
            Rectangle border = new Rectangle(35, 35);
            border.setFill(null);
            border.setStroke(Color.BLACK);
            Image img = new Image("file:\\F:\\NetBeansProjects\\GUI\\src\\gui\\BG.png");
            
            
            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);
            
            
            ImageView iv = new ImageView();
            ImageView iv1 = new ImageView();
            
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
                        delete(iv1);
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
        
        private void water(ImageView iv1) {
            Image img2 = new Image("file:\\F:\\NetBeansProjects\\GUI\\src\\gui\\Raumschiff2.png");
            iv1.setImage(img2);
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
