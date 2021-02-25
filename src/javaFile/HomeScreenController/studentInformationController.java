package javaFile.HomeScreenController;

import Database.DBConnection;
import com.jfoenix.controls.JFXListView;
import javaFile.Table.StudentTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import Database.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javaFile.HomeScreenController.popUpController.studentInfoController;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class studentInformationController {

    @FXML
    private JFXListView<String> listView;

    @FXML
    private TableView<StudentTable> tableView;

    @FXML
    private TableColumn<StudentTable, String> firstNameCol;

    @FXML
    private TableColumn<StudentTable, String> lastNameCol;

    @FXML
    private TableColumn<StudentTable, Integer> idCol;
    private DBConnection handler;
    private Connection conn;
    private int id;
    private ObservableList<StudentTable> list = FXCollections.observableArrayList();
    private AnchorPane homeAnchor;
    private String username;

    @FXML
    void editButtonClicked(ActionEvent event) throws IOException {
        StudentTable studentTable = tableView.getSelectionModel().getSelectedItem();
        if(listView.getSelectionModel().getSelectedItems() == null){ //the user didn't select any class
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please pick a class first!");
            alert.show();
        } else if(studentTable == null){ //the user didn't select any student to perform action
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You didn't pick any student");
            alert.setHeaderText(null);
            alert.show();
        } else { //everything is good to go
            AnchorPane anchorPane = new AnchorPane();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFile/HomePageButtonScene/popUpScene/studentInfo.fxml"));
            anchorPane = loader.load();
            homeAnchor.getChildren().clear();
            homeAnchor.getChildren().setAll(anchorPane);

            studentInfoController studentInfoController = loader.getController();
            studentInfoController.receiveStudentId(studentTable.getId());
            studentInfoController.receiveAnchorPane(this.homeAnchor);
            studentInfoController.receiveUserName(this.username);
        }
    }

    @FXML
    void viewButtonClicked(ActionEvent event) { //view button clicked
        String item = listView.getSelectionModel().getSelectedItem();
        if(item == null){ //check if the user select or not
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("You didn't select any class!");
            alert.show();
        } else {
            loadTable(item);
        }
    }

    public void receiveId(int id){ //receive the id from the controller and set up the list view
        this.id = id;
        handler = new DBConnection();
        conn = handler.getConnection();
        String query = "SELECT * FROM classes WHERE instructor_id = " + id;
        try{
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                this.listView.getItems().add(rs.getString("class_name")); //set up the list view
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void loadTable(String item){ //load table view method
        this.list.clear();
        handler = new DBConnection();
        conn = handler.getConnection();
        String query = "SELECT * FROM students WHERE class_name = '" + item + "'";
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
        this.idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.tableView.setItems(list);
    }

    public void receiveAnchorPane(AnchorPane pane){
        this.homeAnchor = pane;
    }

    public void receiveUserName(String username){
        this.username = username;
    }

}
