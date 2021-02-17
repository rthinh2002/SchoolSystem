package javaFile.HomeScreenController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Database.*;
import javafx.stage.Stage;

public class informationSceneController {
    @FXML
    private JFXTextField userNameTextField;

    @FXML
    private JFXTextField genderTextField;

    @FXML
    private JFXTextField dobTextField;

    @FXML
    private JFXTextField roleField;

    @FXML
    private Label welcome;
    private DBConnection handler;
    private Connection conn;
    private PreparedStatement preparedStatement;


    //inner method receiving information from another controller
    public void receivingUserNameAndConnectToDatabase(String username){
        handler = new DBConnection();
        conn = handler.getConnection();
        String query = "SELECT * FROM schoolsystemlogininfo WHERE user_name = ?";
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

    public void getMessage(String message){
        welcome.setText("Welcome, "+message);
    }


}
