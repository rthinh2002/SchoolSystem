package javaFile.HomeScreenController;


import com.jfoenix.controls.JFXListView;
import javaFile.Table.StudentTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import Database.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javaFile.HomeScreenController.popUpController.addNewStudentController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StudentManagingController {
    @FXML
    private TableView<StudentTable> table;

    @FXML
    private TableColumn<StudentTable, String> lastNameCol;

    @FXML
    private TableColumn<StudentTable, String> firstNameCol;

    @FXML
    private TableColumn<StudentTable, String> genderCol;

    @FXML
    private TableColumn<StudentTable, String> dobCol;

    @FXML
    private TableColumn<StudentTable, Integer> idCol;

    @FXML
    private JFXListView<String> listView;
    ObservableList<StudentTable> list = FXCollections.observableArrayList();
    private int id;
    private DBConnection handler;
    private Connection conn;

    @FXML
    void addButtonClicked(ActionEvent event) throws IOException {
        String className = listView.getSelectionModel().getSelectedItem();
        if(className == null){ //report error when the user didn't choose the class in the list view
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a class before you add student");
            alert.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFile/HomePageButtonScene/popUpScene/addNewStudent.fxml"));
            Parent root = loader.load();
            Stage window = new Stage();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.show();

            addNewStudentController addNewStudentController = loader.getController();
            addNewStudentController.setUp(this.id, className, table, firstNameCol, lastNameCol, genderCol, dobCol, idCol, list);
        }

    }

    @FXML
    void deleteButtonClicked(ActionEvent event) {
        StudentTable studentTable = table.getSelectionModel().getSelectedItem();
        if(listView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please select a class before you delete students");
            alert.show();
        } else if(studentTable == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You didn't pick any student");
            alert.setHeaderText(null);
            alert.show();
        } else {
            handler = new DBConnection();
            conn = handler.getConnection();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Delete student "+ studentTable.getFirstName() + " " + studentTable.getLastName() + " from "+ listView.getSelectionModel().getSelectedItem() + " " +
                    "will remove everything about this student, are you sure?");
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();

            if(!result.isPresent()){ //the alert box being exited
                System.out.println("Alert box exited");
            } else if(result.get() == ButtonType.OK){
                String query = "DELETE FROM students WHERE student_id = " + studentTable.getId();
                try{
                    conn.createStatement().executeUpdate(query);
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            loadTable();
        }
    }

    @FXML
    void searchButtonClicked(ActionEvent event) {
        String item = listView.getSelectionModel().getSelectedItem();
        if(item == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("You didn't select any class!");
            alert.show();
        } else {
            loadTable();
        }
    }

    public void receiveId(int id){ //take the id from other controller
        this.id = id;
        handler = new DBConnection();
        conn = handler.getConnection();
        String query = "SELECT * FROM classes WHERE instructor_id = " + id;
        try{
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                listView.getItems().add(rs.getString("class_name"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void loadTable(){ //load the table
        this.list.clear();
        String query = "SELECT * FROM students WHERE class_name = '" + listView.getSelectionModel().getSelectedItem() + "'";
        handler = new DBConnection();
        conn = handler.getConnection();
        try{
            ResultSet rs = conn.createStatement().executeQuery(query);

            while(rs.next()){
                this.list.add(new StudentTable(rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("gender"), rs.getString("DOB"), rs.getInt("student_id")));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        this.firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        this.lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        this.genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        this.dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
        this.idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.table.setItems(list);
    }

}
