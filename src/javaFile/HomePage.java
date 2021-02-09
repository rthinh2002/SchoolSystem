package javaFile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import Database.*;

public class HomePage implements Initializable {

    @FXML
    private Label timeLabel;

    @FXML
    private Label welcome;

    private String username = "";

    private DBConnection handler;
    private PreparedStatement preparedStatement;
    private Connection conn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        timeLabel.setText(formatter.format(now));
        System.out.println(this.username);
    }

    public void getMessage(String message){
        welcome.setText(message);
    }

}
