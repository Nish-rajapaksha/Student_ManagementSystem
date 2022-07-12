package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Student;
import util.CrudUtil;
import views.StudentTm;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentFormController {
    public AnchorPane StudentContext;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public TableView <StudentTm> tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colNic;
    public JFXTextField txtId;
    public JFXTextField txtNIC;
    public JFXTextField txtEmail;
    public JFXTextField txtSearch;
    public JFXButton btnAdd;

    public void initialize(){


        colId.setCellValueFactory(new PropertyValueFactory("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory("studentName"));
        colAddress.setCellValueFactory(new PropertyValueFactory("email"));
        colContact.setCellValueFactory(new PropertyValueFactory("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colNic.setCellValueFactory(new PropertyValueFactory("nic"));

        try {
            loadAllStudent();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        tblStudent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnAdd.setText(newValue != null ? "Update" : "Add");
            txtId.setEditable(false);
            if (newValue != null) {
                txtId.setText(newValue.getStudentId());
                txtName.setText(newValue.getStudentName());
                txtEmail.setText(newValue.getEmail());
                txtContact.setText(newValue.getContact());
                txtAddress.setText(newValue.getAddress());
                txtNIC.setText(newValue.getNic());
            }
        });



    }


    private void loadAllStudent() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT * FROM Student");
        ObservableList<StudentTm> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new StudentTm(
                            result.getString("student_id"),
                            result.getString("student_Name"),
                            result.getString("email"),
                            result.getString("contact"),
                            result.getString("address"),
                            result.getString("nic")
                    )
            );
        }
        tblStudent.setItems(obList);


        FilteredList<StudentTm> filterData = new FilteredList<StudentTm>(obList, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(Student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();


                if (Student.getStudentId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else return Student.getStudentName().toLowerCase().indexOf(lowerCaseFilter) != -1;

            });
        });

        SortedList<StudentTm> sortedData = new SortedList<>(filterData);

        sortedData.comparatorProperty().bind(tblStudent.comparatorProperty());

        tblStudent.setItems(sortedData);

    }

    public void addOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();
        String nic = txtNIC.getText();


        if (btnAdd.getText().equals("Add")) {
            Student s = new Student(
                    txtId.getText(), txtName.getText(), txtEmail.getText(), txtContact.getText(), txtAddress.getText(), txtNIC.getText());

            try {


                if (CrudUtil.execute("INSERT INTO Student VALUES (?,?,?,?,?,?)", s.getStudentId(), s.getStudentName(), s.getEmail(), s.getContact(), s.getAddress(), s.getNic())) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved Student!..").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }

        } else {
            try {
                CrudUtil.execute("UPDATE Student SET student_name=?,email=?,contact=?,address=?,nic=? where student_id=?", name, email, contact, address, nic, id);
                new Alert(Alert.AlertType.CONFIRMATION, "Updated Student!..").show();

            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            StudentTm selectedStudent = tblStudent.getSelectionModel().getSelectedItem();
            selectedStudent.setStudentName(name);
            selectedStudent.setEmail(email);
            selectedStudent.setContact(contact);
            selectedStudent.setAddress(address);
            selectedStudent.setNic(nic);

        }

        StudentTm selectedStudent = tblStudent.getSelectionModel().getSelectedItem();
        selectedStudent.setStudentName(name);
        selectedStudent.setEmail(email);
        selectedStudent.setContact(contact);
        selectedStudent.setAddress(address);
        selectedStudent.setNic(nic);


        try {
            loadAllStudent();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void newOnAction(ActionEvent actionEvent) {
        txtId.setEditable(true);
        btnAdd.setText("Save");
        txtId.clear();
        txtName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        txtNIC.clear();


    }

    public void deleteOnAction(ActionEvent actionEvent) {
        


    }
}
