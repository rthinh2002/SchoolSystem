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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.scene.layout.AnchorPane;
import javaFile.HomeScreenController.*;
import javafx.stage.Stage;

public class HomePage implements Initializable {

    @FXML
    private AnchorPane homeAnchor;

    @FXML
    private Label timeLabel;

    @FXML
    private JFXButton classesButton;


    private String username = "";
    private String message = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        timeLabel.setText(formatter.format(now));
    }

    @FXML
    public void classesButtonClicked(ActionEvent event) throws IOException{
        AnchorPane anchor = new AnchorPane();
        FXMLLoader loader = setScene(anchor, "/fxmlFile/HomePageButtonScene/ClassesScene.fxml");
    }

    @FXML
    public void profileButtonClicked(ActionEvent event) throws IOException{
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
