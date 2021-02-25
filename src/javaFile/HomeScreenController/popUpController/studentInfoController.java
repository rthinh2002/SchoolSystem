package javaFile.HomeScreenController.popUpController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javaFile.HomeScreenController.studentInformationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import Database.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class studentInfoController {

    @FXML
    private Label nameLabel;

    @FXML
    private JFXTextField firstNameTxt;

    @FXML
    private JFXTextField lastNameTxt;

    @FXML
    private JFXTextField genderTxt;

    @FXML
    private JFXTextField dobTxt;

    @FXML
    private JFXTextField idTxt;

    @FXML
    private JFXTextField classTxt;

    @FXML
    private JFXButton saveButton;
    private DBConnection handler = new DBConnection();
    private Connection conn = handler.getConnection();
    private PreparedStatement pst;
    private int id;
    private AnchorPane homeAnchor;
    private String username;

    @FXML
    void returnButtonClicked(ActionEvent event) throws IOException {
        AnchorPane anchorPane = new AnchorPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFile/HomePageButtonScene/studentInformationScene.fxml"));
        anchorPane = loader.load();
        homeAnchor.getChildren().clear();
        homeAnchor.getChildren().setAll(anchorPane);
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

    @FXML
    void saveButtonClicked(ActionEvent event) {
        if(firstNameTxt.getText().isEmpty()){
            warning(firstNameTxt);
        }

        if(lastNameTxt.getText().isEmpty()){
            warning(lastNameTxt);
        }

        if(dobTxt.getText().isEmpty()){
            warning(dobTxt);
        }

        if(genderTxt.getText().isEmpty()){
            warning(genderTxt);
        }

        if(!genderTxt.getText().equals("FEMALE") && !genderTxt.getText().equals("MALE")){
            warning(genderTxt);
        }
        if(!firstNameTxt.getText().isEmpty() && !lastNameTxt.getText().isEmpty() && !dobTxt.getText().isEmpty() && !genderTxt.getText().isEmpty() && (genderTxt.getText().equals("FEMALE") || genderTxt.getText().equals("MALE"))) {
            String update = "UPDATE students SET first_name = ?, last_name = ?, gender = ?, DOB = ? WHERE student_id = "+ idTxt.getText();
            try{
                pst = conn.prepareStatement(update);
                pst.setString(1, firstNameTxt.getText());
                pst.setString(2, lastNameTxt.getText());
                pst.setString(3, genderTxt.getText());
                pst.setString(4, dobTxt.getText());
                pst.executeUpdate();


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Successfully update student's profile");
                alert.show();
            } catch (SQLException e){
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please check you input again");
                alert.setHeaderText(null);
                alert.show();
            }

            refresh(this.id);
        }
    }

    public void receiveStudentId(int id){
        this.id = id;
        refresh(id);
    }

    public void receiveAnchorPane(AnchorPane anchorPane){
        this.homeAnchor = anchorPane;
    }

    public void receiveUserName(String username){
        this.username = username;
    }

    public void warning(JFXTextField field){
        field.clear();
        field.setPromptText(field.getPromptText());
        field.setStyle("-fx-prompt-text-fill: #E72424");
    }

    public void refresh(int id){
        String query = "SELECT * FROM students WHERE student_id = " + id;
        try{
            ResultSet rs = conn.createStatement().executeQuery(query);
            if(rs.next()){
                nameLabel.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
                firstNameTxt.setText(rs.getString("first_name"));
                lastNameTxt.setText(rs.getString("last_name"));
                genderTxt.setText(rs.getString("gender"));
                dobTxt.setText(rs.getString("DOB"));
                idTxt.setText(rs.getString("student_id"));
                classTxt.setText(rs.getString("class_name"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}