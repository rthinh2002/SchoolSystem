package javaFile.HomeScreenController;

import javaFile.Table.ClassesTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import Database.*;
import javaFile.HomeScreenController.popUpController.addingNewClassesBoxController;
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
                list.add(new ClassesTable(rs.getString("class_name"), rs.getString("categories"), rs.getString("availability"), Integer.parseInt(rs.getString("student_number"))));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        colClass.setCellValueFactory(new PropertyValueFactory<>("className"));
        colCategories.setCellValueFactory(new PropertyValueFactory<>("categories"));
        colNoOfStudents.setCellValueFactory(new PropertyValueFactory<>("noOfStudent"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));

        table.setItems(list);
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
        addingNewClassesBoxController.receiveTable(table, colClass, colCategories, colNoOfStudents, colAvailable);
    }
}


