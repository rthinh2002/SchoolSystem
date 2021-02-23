package javaFile.HomeScreenController.popUpController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javaFile.Table.StudentTable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import Database.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class addNewStudentController {

    @FXML
    private JFXTextField firstNameTxt;

    @FXML
    private JFXTextField lastNameTxt;

    @FXML
    private JFXTextField dobTxt;

    @FXML
    private JFXButton addButton;

    @FXML
    private ChoiceBox<String> genderBox;
    private DBConnection handler;
    private Connection conn;
    private PreparedStatement pst;
    private int id;
    private String className;
    private TableView tableView;
    private TableColumn firstNameCol, lastNameCol, genderCol, dobCol;
    private ObservableList<StudentTable> list;

    @FXML
    void addButtonClicked(ActionEvent event) {
        if(firstNameTxt.getText().isEmpty()){
            warning(firstNameTxt);
        }

        if(lastNameTxt.getText().isEmpty()){
            warning(lastNameTxt);
        }

        if(dobTxt.getText().isEmpty()){
            warning(dobTxt);
        }

        if(genderBox.getItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please choose student's gender");
            alert.setHeaderText(null);
            alert.show();
        }

        if(!firstNameTxt.getText().isEmpty() && !lastNameTxt.getText().isEmpty() && !dobTxt.getText().isEmpty() && !genderBox.getItems().isEmpty()){
            handler = new DBConnection();
            conn = handler.getConnection();
            String query = "INSERT INTO students (first_name, last_name, gender, DOB, class_name, instructor_id) VALUES(?, ?, ?, ?, ? ,?)";
            try{
                pst = conn.prepareStatement(query);
                pst.setString(1, firstNameTxt.getText());
                pst.setString(2, lastNameTxt.getText());
                pst.setString(3, genderBox.getValue());
                pst.setString(4, dobTxt.getText());
                pst.setString(5, this.className);
                pst.setInt(6, this.id);
                pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Insert student successfully");
                alert.show();

                addButton.getScene().getWindow().hide();
            } catch (SQLException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Please check student's Date of Birth");
                alert.show();
            }
        }
        //reload the table
        loadTable();
    }

    //receive information from the other controller and set up the gender box
    public void setUp(int id, String className, TableView table, TableColumn firstName, TableColumn lastName, TableColumn gender, TableColumn dob, ObservableList<StudentTable> list){
        this.id = id;
        this.className = className;
        this.tableView = table;
        this.firstNameCol = firstName;
        this.lastNameCol = lastName;
        this.genderCol = gender;
        this.dobCol = dob;
        this.list = list;
        genderBox.getItems().addAll("MALE", "FEMALE");
    }

    //warning for the empty text field
    public void warning(JFXTextField field){
        field.setPromptText(field.getPromptText());
        field.setStyle("-fx-prompt-text-fill: #E72424");
    }

    //reload the table
    public void loadTable(){
        this.list.clear();
        String query = "SELECT * FROM students WHERE class_name = '" + this.className + "'";
        handler = new DBConnection();
        conn = handler.getConnection();
        try{
            ResultSet rs = conn.createStatement().executeQuery(query);

            while(rs.next()){
                this.list.add(new StudentTable(rs.getString("first_name"), rs.getString("last_name"), rs.getString("gender"), rs.getString("DOB")));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        this.firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        this.genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        this.dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
        this.tableView.setItems(list);
    }

}
