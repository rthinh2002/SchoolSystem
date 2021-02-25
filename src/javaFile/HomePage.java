package javaFile;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.scene.layout.AnchorPane;
import javaFile.HomeScreenController.*;
import javafx.stage.Stage;
import Database.*;

public class HomePage implements Initializable {

    @FXML
    private AnchorPane homeAnchor;

    @FXML
    private Label timeLabel;

    @FXML
    private JFXButton classesButton;

    private DBConnection handler;
    private Connection conn;

    private String username = "";
    private String message = "";
    private int id = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        timeLabel.setText(formatter.format(now));
        handler = new DBConnection();
    }

    @FXML
    public void classesButtonClicked(ActionEvent event) throws IOException{ //classesButton being clicked and search the database to send the instructor id
        AnchorPane anchor = new AnchorPane();
        FXMLLoader loader = setScene(anchor, "/fxmlFile/HomePageButtonScene/ClassesScene.fxml");
        ClassesSceneController classes = loader.getController();
        conn = handler.getConnection();
        try{
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM schoolsystemlogininfo WHERE user_name = '" + this.username + "'");
            if(rs.next()){
                this.id = rs.getInt("id");
            }
            classes.receivingInstructorId(this.id);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void profileButtonClicked(ActionEvent event) throws IOException{ //profile button being clicked, reload the information page
        createMainPage(this.message, this.username);
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

    @FXML
    void managementClicked(ActionEvent event) throws IOException{
        AnchorPane anchorPane = new AnchorPane();
        FXMLLoader loader = setScene(anchorPane, "/fxmlFile/HomePageButtonScene/StudentManaging.fxml");
        handler = new DBConnection();
        conn = handler.getConnection();
        try{
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM schoolsystemlogininfo WHERE user_name = '" + this.username + "'");
            if(rs.next()){
                this.id = rs.getInt("id");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        StudentManagingController studentManagingController = loader.getController();
        studentManagingController.receiveId(this.id);
    }

    @FXML
    void studentRecordClicked(ActionEvent event) throws IOException{
        AnchorPane anchorPane = new AnchorPane();
        FXMLLoader loader = setScene(anchorPane, "/fxmlFile/HomePageButtonScene/studentInformationScene.fxml");
        handler = new DBConnection();
        conn = handler.getConnection();
        try{
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM schoolsystemlogininfo WHERE user_name = '" + this.username + "'");
            if(rs.next()){
                this.id = rs.getInt("id");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        studentInformationController studentInformationController = loader.getController();
        studentInformationController.receiveId(this.id);
        studentInformationController.receiveAnchorPane(this.homeAnchor);
        studentInformationController.receiveUserName(this.username);
    }

    private FXMLLoader setScene(AnchorPane anchor, String url) throws IOException{ //changing scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        anchor = loader.load();
        homeAnchor.getChildren().clear();
        homeAnchor.getChildren().setAll(anchor);
        return loader;
    }

    public void createMainPage(String message, String username) throws IOException{
        AnchorPane anchor = new AnchorPane();
        FXMLLoader loader = setScene(anchor, "/fxmlFile/HomePageButtonScene/InformationScene.fxml");

        this.username = username;
        this.message = message;

        informationSceneController infor = loader.getController();
        infor.getMessage(message);
        infor.receivingUserNameAndConnectToDatabase(username);
    }




}
