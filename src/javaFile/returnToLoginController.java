package javaFile;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class returnToLoginController {

    @FXML
    private JFXButton noButton;

    private Button handlingButton;

    @FXML
    public void noButtonClicked(ActionEvent event){
        noButton.getScene().getWindow().hide();
    } //close the stage

    @FXML
    public void yesButtonClicked(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFile/loginScene.fxml"));
            Parent root = loader.load();

            Stage window = new Stage();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.show();

            noButton.getScene().getWindow().hide();
            this.handlingButton.getScene().getWindow().hide();


        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //inner method
    public void passButton(Button button){ //receive the button from signUpController controller
        this.handlingButton = button;
    }

}