package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.student;
import view.tm.studentTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentController {
    public TextField txtId;
    public TextField txtNIC;
    public TextField txtAddress;
    public TextField txtEmail;
    public TextField txtName;
    public TextField txtContact;
    public TableView<studentTM> tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colNic;

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));

        loadAllStudents();

        tblStudent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtId.setText(newValue.getId());
                txtName.setText(newValue.getName());
                txtEmail.setText(newValue.getEmail());
                txtContact.setText(newValue.getContact());
                txtAddress.setText(newValue.getAddress());
                txtNIC.setText(newValue.getNic());
            }
        });
    }

    private void loadAllStudents() {
        ArrayList<student> students = new ArrayList<>();
        ObservableList<studentTM> obList = FXCollections.observableArrayList();
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student");
            ResultSet rst = preparedStatement.executeQuery();

            while (rst.next()) {
                students.add(new student(
                        rst.getString(1),
                        rst.getString(2),
                        rst.getString(3),
                        rst.getString(4),
                        rst.getString(5),
                        rst.getString(6))
                );
            }
            for (student stu : students) {
                obList.add(new studentTM(stu.getId(), stu.getName(), stu.getEmail(), stu.getContact(), stu.getAddress(), stu.getNic()));
            }
            tblStudent.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Student WHERE student_id=?");
        stm.setString(1, txtId.getText());
        if (stm.executeUpdate() > 0) {
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted Student.").show();
            loadAllStudents();

        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
            loadAllStudents();

        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        student s1 = new student(
                txtId.getText(), txtName.getText(), txtEmail.getText(), txtContact.getText(), txtAddress.getText(), txtNIC.getText()
        );
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Student SET student_name=?, email=?, contact=?, address=?, nic=? WHERE student_id=?");
        stm.setString(1, s1.getName());
        stm.setString(2, s1.getEmail());
        stm.setString(3, s1.getContact());
        stm.setString(4, s1.getAddress());
        stm.setString(5, s1.getNic());
        stm.setString(6, s1.getId());

        if (stm.executeUpdate() > 0) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated Student.").show();
            loadAllStudents();
            
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }

    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        student s1 = new student(
                txtId.getText(), txtName.getText(), txtEmail.getText(), txtContact.getText(), txtAddress.getText(), txtNIC.getText()
        );
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Student VALUES (?,?,?,?,?,?)");
        stm.setString(1, s1.getId());
        stm.setString(2, s1.getName());
        stm.setString(3, s1.getEmail());
        stm.setString(4, s1.getContact());
        stm.setString(5, s1.getAddress());
        stm.setString(6, s1.getNic());
        if (stm.executeUpdate() > 0) {
            new Alert(Alert.AlertType.CONFIRMATION, "Saved..!").show();
            loadAllStudents();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
            loadAllStudents();
        }
    }
}
