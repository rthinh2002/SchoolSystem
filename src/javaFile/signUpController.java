package javaFile;
import Database.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class signUpController implements Initializable {

    @FXML
    private JFXComboBox<String> genderChoiceBox;

    @FXML
    public Button signUpButton;

    @FXML
    private ImageView loadingImage;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXTextField role;

    //variable to connect to database
    private Connection connection;
    private DBConnection handler;
    private PreparedStatement preparedStatement;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadingImage.setVisible(false);
        genderChoiceBox.getItems().addAll("MALE", "FEMALE", "DON'T WANT TO SAY");
        genderChoiceBox.setStyle("-fx-font: 20px \"Arial\";");
        genderChoiceBox.setValue("Male");

        username.setStyle("-fx-prompt-text-fill: #C5C3C3");
        password.setStyle("-fx-prompt-text-fill: #C5C3C3");
        role.setStyle("-fx-prompt-text-fill: #C5C3C3");

        handler = new DBConnection();
    }

    @FXML
    public void signUpAction(ActionEvent e) throws IOException{ //handling when user click sign up button
        returnToLoginController returnToLogin = new returnToLoginController();

        loadingImage.setVisible(true);

        PauseTransition pause = new PauseTransition();
        pause.setDuration(Duration.seconds(2));
        pause.setOnFinished(ev ->{
            System.out.println("Sign up successfully");
            loadingImage.setVisible(false);
        });
        pause.play();


        if(username.getText().isEmpty()){ //check if user enter the username
            username.setPromptText("Username missing");
            username.setStyle("-fx-prompt-text-fill: #E72424");
        }
        if(password.getText().isEmpty()){ //check if user enter the password
            password.setPromptText("Password missing");
            password.setStyle("-fx-prompt-text-fill: #E72424");
        }

        if(role.getText().isEmpty()){ //check if user enter their role
            role.setPromptText("Role missing");
            role.setStyle("-fx-prompt-text-fill: #E72424");
        }


        if(!username.getText().isEmpty() && !password.getText().isEmpty() && !genderChoiceBox.getValue().isEmpty() && !role.getText().isEmpty()) { //user correctly enter all the field
            //sending to database
            String insert = "INSERT INTO schoolsystemlogininfo(user_name, pass_word, gender, work_role)"
                    +" VALUES(?, ?, ?, ?)"; //query for mySql
            connection = handler.getConnection(); //getting the connection to the sql server

            //create preparedStatement to insert into the query
            try {
                preparedStatement = connection.prepareStatement(insert);
            } catch (SQLException el){
                el.printStackTrace();
            }

            try { //getting the text and insert into the query
                preparedStatement.setString(1, username.getText());
                preparedStatement.setString(2, password.getText());
                preparedStatement.setString(3, genderChoiceBox.getValue());
                preparedStatement.setString(4, role.getText());

                preparedStatement.executeUpdate(); //update the query
            } catch (SQLException el){
                el.printStackTrace();
            }
            displayAlertScene(); //display to message that signup successfully
        }

    }

    @FXML
    public void loginAction(ActionEvent e) throws IOException { //handling when user click login button
        closeProgram();

        Stage loginStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxmlFile/loginScene.fxml"));
        Scene scene = new Scene(root);
        loginStage.setScene(scene);
        loginStage.setResizable(false);
        loginStage.show();
    }

    public void closeProgram(){
        signUpButton.getScene().getWindow().hide();
    }//close the program

    public void displayAlertScene() { //calling the login scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFile/returnToLogin.fxml"));
            Stage window = new Stage();
            Parent root = loader.load();

            //passing the button
            returnToLoginController returnToLoginController = loader.getController();
            returnToLoginController.passButton(signUpButton);


            Scene scene = new Scene(root);
            window.initModality(Modality.APPLICATION_MODAL); //this must be handle before process any further
            window.setScene(scene);
            window.setResizable(false);
            window.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
