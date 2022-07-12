package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Student;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentFormController {
    public AnchorPane StudentContext;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public TableView tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colNic;
    public JFXTextField txtId;
    public JFXTextField txtNIC;
    public JFXTextField txtEmail;

    public void initialize(){


        colId.setCellValueFactory(new PropertyValueFactory("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory("studentName"));
        colAddress.setCellValueFactory(new PropertyValueFactory("email"));
        colContact.setCellValueFactory(new PropertyValueFactory("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colNic.setCellValueFactory(new PropertyValueFactory("nic"));

        try {
            loadAllCustomers();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


    private void loadAllCustomers() throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Student");
        ObservableList<Student> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Student(
                            result.getString("studentId"),
                            result.getString("studentName"),
                            result.getString("email"),
                            result.getString("contact"),
                            result.getString("address"),
                            result.getString("nic")

                    )
            );
        }
        tblStudent.setItems(obList);

    }

    public void addOnAction(ActionEvent actionEvent) {
        Student s = new Student(
                txtId.getText(), txtName.getText(), txtEmail.getText(),txtContact.getText(),txtAddress.getText(),txtNIC.getText());

        try {


            if (CrudUtil.execute("INSERT INTO Student VALUES (?,?,?,?,?,?)",s.getStudentId(),s.getStudentName(),s.getEmail(),s.getContact(),s.getAddress(),s.getNic())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved Student!..").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void newOnAction(ActionEvent actionEvent) {
    }

    public void deleteOnAction(ActionEvent actionEvent) {
    }
}
