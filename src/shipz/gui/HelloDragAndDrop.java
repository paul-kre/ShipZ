import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Demonstrates a drag-and-drop feature.
 */
public class HelloDragAndDrop extends Application {

    @Override public void start(Stage stage) {
        stage.setTitle("Hello Drag And Drop");

        Group root = new Group();
        Scene scene = new Scene(root, 400, 200);
        scene.setFill(Color.LIGHTGREEN);

        final Text source = new Text(50, 100, "DRAG ME");
        source.setScaleX(2.0);
        source.setScaleY(2.0);
        
        //Image Source
        ImageView iv = new ImageView();
        Image img1 = new Image(GUI.class.getResource("Raumschiff1.png").toExternalForm());
        iv.setImage(img1);
        iv.setFitWidth(35);
        iv.setFitHeight(35);
        iv.setTranslateX(35);
        iv.setTranslateY(35);
        

        final Text target = new Text(250, 100, "DROP HERE");
        target.setScaleX(2.0);
        target.setScaleY(2.0);
        
        //Image Target
        ImageView ivT = new ImageView();
        Image img2 = new Image(GUI.class.getResource("BG.png").toExternalForm());
        ivT.setImage(img2);
        ivT.setFitWidth(35);
        ivT.setFitHeight(35);
        ivT.setTranslateX(235);
        ivT.setTranslateY(35);
        

        iv.setOnDragDetected(new EventHandler <MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start drag-and-drop gesture*/
                System.out.println("onDragDetected");
                
                /* allow any transfer mode */
                Dragboard db = iv.startDragAndDrop(TransferMode.MOVE);
                
                /* put a string on dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putImage(img1);
                db.setContent(content);
                
                event.consume();
            }
        });

        ivT.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                System.out.println("onDragOver");
                
                /* accept it only if it is  not dragged from the same node 
                 * and if it has a string data */
                if (event.getGestureSource() != ivT &&
                        event.getDragboard().hasImage()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                
                event.consume();
            }
        });

        ivT.setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                System.out.println("onDragEntered");
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != ivT &&
                        event.getDragboard().hasImage()) {
                    ivT.setImage(img1);;
                }
                
                event.consume();
            }
        });

        ivT.setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                ivT.setImage(img1);;
                
                event.consume();
            }
        });
        
        ivT.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    ivT.setImage(db.getImage());
                    success = true;
                }
                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);
                
                event.consume();
            }
        });

        iv.setOnDragDone(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture ended */
                System.out.println("onDragDone");
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    iv.setImage(img2);
                }
                
                event.consume();
            }
        });

        root.getChildren().add(source);
        root.getChildren().add(iv);
        root.getChildren().add(target);
        root.getChildren().add(ivT);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
