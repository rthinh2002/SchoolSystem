package javaFile;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import Database.*;
import javafx.stage.Stage;

public class HomePage implements Initializable {

    @FXML
    private Label timeLabel;

    @FXML
    private Label welcome;

    @FXML
    private JFXButton classesButton;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private JFXTextField userNameTextField;

    @FXML
    private JFXTextField genderTextField;

    @FXML
    private JFXTextField dobTextField;

    @FXML
    private JFXTextField roleField;

    private Button handlingLoginSceneButton;

    private DBConnection handler;
    private PreparedStatement preparedStatement;
    private Connection conn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        timeLabel.setText(formatter.format(now));
    }

    @FXML
    public void logoutButtonClicked(ActionEvent event){
        this.handlingLoginSceneButton.getScene().getWindow().hide();
        this.logoutButton.getScene().getWindow().hide();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFile/loginScene.fxml"));
            Parent root = loader.load();
            Stage window = new Stage();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void getMessage(String message){
        welcome.setText("Welcome, "+message);
    }

    public void receivingTheButton(Button handlingLoginSceneButton){
        this.handlingLoginSceneButton = handlingLoginSceneButton;
    }

    public void receivingUserNameAndConnectToDatabase(String username){
        handler = new DBConnection();
        conn = handler.getConnection();
        String query = "SELECT * FROM schoolsystemlogininfo WHERE user_name = ?";
        System.out.println(username);
        try{
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                String name = rs.getString("user_name");
                String gender = rs.getString("gender");
                String role = rs.getString("work_role");
                String dob = rs.getString("DOB");

                this.userNameTextField.setText(name);
                this.genderTextField.setText(gender);
                this.roleField.setText(role);
                this.dobTextField.setText(dob);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
