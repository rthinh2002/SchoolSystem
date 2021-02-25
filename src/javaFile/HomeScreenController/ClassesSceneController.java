package javaFile.HomeScreenController;

import javaFile.Table.ClassesTable;
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
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import Database.*;
import javaFile.HomeScreenController.popUpController.*;
import javafx.stage.Stage;

public class ClassesSceneController{
    @FXML
    private TableView<ClassesTable> table;

    @FXML
    private TableColumn<ClassesTable, String> colClass;

    @FXML
    private TableColumn<ClassesTable, String> colCategories;

    @FXML
    private TableColumn<ClassesTable, Integer> colNoOfStudents;

    @FXML
    private TableColumn<ClassesTable, String> colAvailable;

    @FXML
    private TableColumn<ClassesTable, Integer> idCol;

    private DBConnection handler;
    private Connection conn;
    private int id;
    ObservableList<ClassesTable> list = FXCollections.observableArrayList();

    public void receivingInstructorId(int id){ //receive the id and start to connect the database
        handler = new DBConnection();
        conn = handler.getConnection();
        this.id = id;
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM classes WHERE instructor_id = "+ id);

            while (rs.next()){ //add the infor to the Table View
                this.list.add(new ClassesTable(rs.getString("class_name"), rs.getString("categories"), rs.getString("availability"),
                        Integer.parseInt(rs.getString("student_number")), Integer.parseInt(rs.getString("class_id"))));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        this.colClass.setCellValueFactory(new PropertyValueFactory<>("className"));
        this.colCategories.setCellValueFactory(new PropertyValueFactory<>("categories"));
        this.colNoOfStudents.setCellValueFactory(new PropertyValueFactory<>("noOfStudent"));
        this.colAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));
        this.idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.table.setItems(list);
    }

    @FXML
    public void addClassesClicked(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFile/HomePageButtonScene/popUpScene/addingNewClassesBox.fxml"));
        Stage window = new Stage();
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.show();
        } catch (IOException e){
            e.printStackTrace();
        }

        addingNewClassesBoxController addingNewClassesBoxController = loader.getController();
        addingNewClassesBoxController.receiveInstructorId(this.id);
        addingNewClassesBoxController.receiveList(list);
        addingNewClassesBoxController.receiveTable(table, colClass, colCategories, colNoOfStudents, colAvailable, idCol);
    }

    @FXML
    void dropButtonClicked(ActionEvent event) { //dropping class
        ClassesTable classesTable = table.getSelectionModel().getSelectedItem(); //get the selected item
        if(classesTable == null){ //if no item was selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("You didn't select any class!");
            alert.show();
        } else {
            handler = new DBConnection();
            conn = handler.getConnection();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Drop "+ classesTable.getClassName() + " will delete everything about this class, are you sure?");

            Optional<ButtonType> result = alert.showAndWait(); //handling the alert button
            if(!result.isPresent()){ //alert box being exited
                System.out.println("Alert box exited");
            } else if(result.get() == ButtonType.OK){ //if the user confirm to drop the class
                String query = "DELETE FROM classes WHERE class_id = " + classesTable.getId();
                try {
                    conn.createStatement().executeUpdate(query);
                } catch (SQLException e){
                    e.printStackTrace();
                }
                this.list.clear();
                receivingInstructorId(this.id);
            } else if(result.get() == ButtonType.CANCEL){ //the user change his/her mind hehe
                System.out.println("Cancel dropping the class");
            }
        }
    }

}


