/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shipz.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author nnamf
 */
public class NewWindow extends Stage{
    
    //IV
    Stage primaryStage;
    
    // Constructor
    public NewWindow () {
        primaryStage = this;
        
        Button btn1 = new Button();
        
        btn1.setText("Schlieﬂen");
        
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                primaryStage.close();
                
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn1);
        
        
        Scene scene = new Scene(root, 1000, 1000);
        
        
        primaryStage.setTitle("Multiple Window");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    
}
