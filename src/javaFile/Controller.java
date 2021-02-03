package javaFile;
import Database.DBConnection;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    @FXML
    private Button forgotPassButton;

    @FXML
    private ImageView logoImage;

    @FXML
    private ImageView loadingImage;

    private Scene signUpScene;

    @FXML
    void initialize() {
        assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'sample.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'sample.fxml'.";
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert signUpButton != null : "fx:id=\"signUpButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert forgotPassButton != null : "fx:id=\"forgotPassButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert logoImage != null : "fx:id=\"logoImage\" was not injected: check your FXML file 'sample.fxml'.";
        assert loadingImage != null : "fx:id=\"loadingImage\" was not injected: check your FXML file 'sample.fxml'.";

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadingImage.setVisible(false);
        username.setStyle("-fx-prompt-text-fill: #C5C3C3");

        password.setStyle("-fx-prompt-text-fill: #C5C3C3");

    }

    @FXML
    public void loginAction(ActionEvent e){ //perform when user click the login button
        loadingImage.setVisible(true);
        PauseTransition pause = new PauseTransition();
        pause.setDuration(Duration.seconds(2));
        pause.setOnFinished(ev -> {
            System.out.println("Login successfully");
            loadingImage.setVisible(false);
        });
        //prepare to connect to database to retrieve the information
        DBConnection connect = new DBConnection();
        Connection connection = connect.getConnection();
        pause.play();
    }

    @FXML
    public void signUpAction(ActionEvent e) throws IOException { //handling when user click the sign up button
        loginButton.getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource("/fxmlFile/signUpScene.fxml"));
        Stage signUp = new Stage();
        signUpScene = new Scene(root);
        signUp.setScene(signUpScene);
        signUp.show();
        signUp.setResizable(false);
    }

}
