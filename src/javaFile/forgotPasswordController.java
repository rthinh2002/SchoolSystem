package javaFile;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import Database.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import java.util.Stack;

public class forgotPasswordController implements Initializable {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private FontAwesomeIcon passwordIcon;

    @FXML
    private JFXButton checkButton;

    @FXML
    private JFXButton saveButton;

    @FXML
    private ImageView loadingImage;

    @FXML
    private JFXButton returnToLoginButton;

    private DBConnection handler;
    private Connection conn;
    private PreparedStatement preparedStatement;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordField.setVisible(false);
        passwordIcon.setVisible(false);
        saveButton.setVisible(false);
        returnToLoginButton.setVisible(false);
        loadingImage.setVisible(false);
        handler = new DBConnection();
    }

    public void handlingMissingTextField(JFXTextField field){
        field.clear();
        field.setPromptText("Unrecognized Information");
        field.setStyle("-fx-prompt-text-fill: #E72424");
    }

    public void handlingMissingTextField(JFXPasswordField field){
        field.clear();
        field.setPromptText("Unrecognized Information");
        field.setStyle("-fx-prompt-text-fill: #E72424");
    }

    public void clearingField(){
        passwordField.clear();
        usernameField.clear();
    }

    @FXML
    public void checkButtonClicked(ActionEvent event){
        if(usernameField.getText().isEmpty()) {
            handlingMissingTextField(usernameField);
        } else {
            String query = "SELECT * FROM schoolsystemlogininfo WHERE user_name = ?";
            conn = handler.getConnection();

            try{
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, usernameField.getText());
                ResultSet rs = preparedStatement.executeQuery();

                if(rs.next()){
                    System.out.println("Correct username");
                    passwordIcon.setVisible(true);
                    passwordField.setVisible(true);
                    saveButton.setVisible(true);
                    checkButton.setVisible(false);
                } else {
                    handlingMissingTextField(usernameField);
                    System.out.println("Incorrect username");
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void saveButtonClicked(ActionEvent event){
        loadingImage.setVisible(true);
        PauseTransition pause = new PauseTransition();
        pause.setDuration(Duration.seconds(1));
        pause.setOnFinished(e -> {
            if(usernameField.getText().isEmpty()){
                handlingMissingTextField(usernameField);
            } else if(passwordField.getText().isEmpty()){
                handlingMissingTextField(passwordField);
            } else {
                String query = "SELECT * FROM schoolsystemlogininfo WHERE user_name = ?";
                conn = handler.getConnection();

                try{
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, usernameField.getText());
                    ResultSet rs  = preparedStatement.executeQuery();

                    if(rs.next()){
                        preparedStatement = conn.prepareStatement("UPDATE schoolsystemlogininfo SET pass_word = ? WHERE user_name = ?");
                        preparedStatement.setString(1, passwordField.getText());
                        preparedStatement.setString(2, usernameField.getText());
                        preparedStatement.executeUpdate();

                        Alert alertBox = new Alert(Alert.AlertType.INFORMATION);
                        alertBox.setHeaderText(null);
                        alertBox.setContentText("Successfully change your password!");
                        alertBox.show();

                        clearingField();
                        returnToLoginButton.setVisible(true);
                    } else {
                        handlingMissingTextField(usernameField);
                        handlingMissingTextField(passwordField);
                    }
                }catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
            loadingImage.setVisible(false);
        });
        pause.play();
    }

    @FXML
    public void returnToLoginClicked(ActionEvent event) throws IOException{
        saveButton.getScene().getWindow().hide();

        Stage loginStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxmlFile/loginScene.fxml"));
        Scene scene = new Scene(root);
        loginStage.setScene(scene);
        loginStage.setResizable(false);
        loginStage.show();
    }
}