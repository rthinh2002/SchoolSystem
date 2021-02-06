package javaFile;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class returnToLoginController {

    @FXML
    private JFXButton okayButton;

    private Stage window = new Stage();

    public void display() { //calling the login scene
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlFile/returnToLogin.fxml"));
            Scene scene = new Scene(root);
            window.initModality(Modality.APPLICATION_MODAL);
            window.setScene(scene);
            window.setResizable(false);
            window.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void okayButtonClicked(ActionEvent event){
        okayButton.getScene().getWindow().hide();
    } //close the stage

}