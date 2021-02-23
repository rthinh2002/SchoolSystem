package javaFile.HomeScreenController.popUpController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.sun.webkit.network.data.Handler;
import javaFile.Table.ClassesTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import Database.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.sql.*;

public class addingNewClassesBoxController {
    @FXML
    private JFXTextField classNameTxt;

    @FXML
    private JFXTextField categoriesTxt;

    @FXML
    private JFXTextField numberofStudentTxt;

    @FXML
    private JFXTextField availabilityTxt;

    @FXML
    private JFXButton addButton;

    private DBConnection handler;
    private Connection conn;
    private PreparedStatement pst;
    private int instructorId = 0;
    private ObservableList<ClassesTable> list;
    private TableView tableView;
    private TableColumn colClass;
    private TableColumn colCategories;
    private TableColumn colNoOfStudents;
    private TableColumn colAvailable;

    @FXML
    void addButtonClicked(ActionEvent event) {
        handler = new DBConnection();
        conn = handler.getConnection();
        String query = "INSERT INTO classes (class_name, categories, student_number, availability, instructor_id) VALUES(?, ?, ?, ?, ?)";

        //check when the field is empty and validate the field
        if(classNameTxt.getText().isEmpty()){
            warning(classNameTxt);
        }

        if(categoriesTxt.getText().isEmpty()){
            warning(categoriesTxt);
        }

        if(numberofStudentTxt.getText().isEmpty()){
            warning(numberofStudentTxt);
        } else if(!numberofStudentTxt.getText().isEmpty()){
            try{
                int result = Integer.parseInt(numberofStudentTxt.getText());
            } catch (NumberFormatException ev){
                warning(numberofStudentTxt);
            }
        }

        if(availabilityTxt.getText().isEmpty()){
            availabilityTxt.clear();
            warning(availabilityTxt);
        }

        //insert into database
        if(!classNameTxt.getText().isEmpty() && !categoriesTxt.getText().isEmpty() && !numberofStudentTxt.getText().isEmpty() && !availabilityTxt.getText().isEmpty() && (availabilityTxt.getText().equals("Y") || availabilityTxt.getText().equals("N"))){
            try {
                pst = conn.prepareStatement(query);
                pst.setString(1, classNameTxt.getText());
                pst.setString(2, categoriesTxt.getText());
                pst.setInt(3, Integer.parseInt(numberofStudentTxt.getText()));
                pst.setString(4, availabilityTxt.getText());
                pst.setInt(5, this.instructorId);
                pst.executeUpdate();
            } catch (SQLException e){
                e.printStackTrace();
            }
            addButton.getScene().getWindow().hide();
            Alert alertBox = new Alert(Alert.AlertType.INFORMATION);
            alertBox.setHeaderText(null);
            alertBox.setContentText("Insert new class successfully");
            alertBox.showAndWait();
            refreshTable();
        } else if(!availabilityTxt.getText().equals("Y") || !availabilityTxt.getText().equals("N")){
            availabilityTxt.clear();
            warning(availabilityTxt);
        }
    }

    public void warning(JFXTextField field){
        field.setPromptText(field.getPromptText());
        field.setStyle("-fx-prompt-text-fill: #E72424");
    }

    public void receiveInstructorId(int id){
        this.instructorId = id;
    }

    public void receiveTable(TableView tableView, TableColumn colClass, TableColumn colCategories, TableColumn colNoOfStudents, TableColumn colAvailable){
        this.tableView = tableView;
        this.colClass = colClass;
        this.colCategories = colCategories;
        this.colNoOfStudents = colNoOfStudents;
        this.colAvailable = colAvailable;
    }

    public void refreshTable(){ //refresh the table
        handler = new DBConnection();
        conn = handler.getConnection();
        this.list.clear();
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM classes WHERE instructor_id = "+ this.instructorId);

            while (rs.next()){ //add the infor to the Table View
                list.add(new ClassesTable(rs.getString("class_name"), rs.getString("categories"), rs.getString("availability"), Integer.parseInt(rs.getString("student_number"))));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        this.colClass.setCellValueFactory(new PropertyValueFactory<>("className"));
        this.colCategories.setCellValueFactory(new PropertyValueFactory<>("categories"));
        this.colNoOfStudents.setCellValueFactory(new PropertyValueFactory<>("noOfStudent"));
        this.colAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));

        this.tableView.setItems(this.list);
    }

    public void receiveList(ObservableList<ClassesTable> list){
        this.list = list;
    }
}
