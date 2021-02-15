package javaFile;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomePage implements Initializable {

    @FXML
    private Pane horizontalMenu;

    @FXML
    private Label welcome;

    @FXML
    private Label labelMenu;

    @FXML
    private Label timeLabel;

    @FXML
    private JFXButton classesButton;

    @FXML
    private JFXTextField userNameTextField;

    @FXML
    private JFXTextField genderTextField;

    @FXML
    private JFXTextField dobTextField;

    @FXML
    private JFXTextField roleField;

    @FXML
    private VBox overflowContainer;

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

    public void getMessage(String message){
        welcome.setText("Welcome, "+message);
    }

    public void receivingTheButton(Button handlingLoginSceneButton){
        this.handlingLoginSceneButton = handlingLoginSceneButton;
    }

    @FXML
    public void logOutButtonClicked(ActionEvent event){
        try {
            classesButton.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlFile/loginScene.fxml"));
            Stage window = new Stage();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

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
}
