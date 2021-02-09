package javaFile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlFile/loginScene.fxml"));
            primaryStage.setTitle("School System");
            primaryStage.setScene(new Scene(root, 972, 717));
            primaryStage.show();
            primaryStage.setResizable(false);
        } catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
