package javaFile;
import Database.*;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private DBConnection handler;
    private Connection connection;
    private PreparedStatement preparedStatement;

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

        handler = new DBConnection();

    }

    @FXML
    public void loginAction(ActionEvent e){ //perform when user click the login button
        loadingImage.setVisible(true);
        PauseTransition pause = new PauseTransition();
        pause.setDuration(Duration.seconds(2));
        pause.setOnFinished(ev -> {
            loadingImage.setVisible(false);
        });
        pause.play();

        //Connect to database and retrieve information
        connection = handler.getConnection();
        String query = "SELECT * FROM schoolsystemlogininfo WHERE user_name = ? AND pass_word = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username.getText());
            preparedStatement.setString(2, password.getText());
            ResultSet rs = preparedStatement.executeQuery();

            int count = 0;

            while(rs.next()){
                count++;
            }

            if(count == 1){
                System.out.println("Login successfully");
            } else {
                System.out.println("Invalid username and password");
            }
        } catch (SQLException ev){
            ev.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ev2){
                ev2.printStackTrace();
            }
        }
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
