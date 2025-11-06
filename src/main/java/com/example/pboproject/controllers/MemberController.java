package com.example.pboproject.controllers;

import com.example.pboproject.CashierApp;
import com.example.pboproject.beans.Membership;
import com.example.pboproject.dao.MemberDao;
import com.example.pboproject.utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class MemberController {
    @FXML
    private TableView<Membership> tablemember;
    @FXML
    private TextField fieldname;
    @FXML
    private TextField fieldphonenumber;
    private ObservableList<Membership> members;
    private int id;
    @FXML
    private Button btnback;
    @FXML
    public void initialize(){
        System.out.println("Initialize function");
        members = FXCollections.emptyObservableList();
        tablemember.getColumns().clear();
        //Inisialisasi kolom
        TableColumn nama = new TableColumn("Name");
        nama.setMinWidth(50);
        nama.setCellValueFactory(
                new PropertyValueFactory<Membership, String >("memberName"));

        TableColumn phone = new TableColumn("Phone No");
        phone.setMinWidth(150);
        phone.setCellValueFactory(
                new PropertyValueFactory<Membership, String>("phoneNoMember"));

        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(25);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Membership, Integer>("memberId"));
        tablemember.getColumns().addAll(idCol,nama,phone);

        //Inisialisasi data
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            members = FXCollections.
                    observableList(MemberDao.getAll(con));
            for (Membership member :  members) {
                System.out.println(member);
            }
            tablemember.setItems(members);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.closeConnection(con);
        }
    }

    @FXML
    public void edit2() throws SQLException {
        Membership clickedMember = tablemember.getSelectionModel().getSelectedItem();
        id = clickedMember.getMemberId();
        Membership updatedMember = new Membership();
        updatedMember.setMemberId(id);
        updatedMember.setMemberName(fieldname.getText());
        updatedMember.setPhoneNoMember(Integer.valueOf(fieldphonenumber.getText()));
        MemberDao.update(updatedMember);
        refresh();
        clear();
    }

    public void delete2() throws SQLException {
        Membership clickedMember = tablemember.getSelectionModel().getSelectedItem();
        id = clickedMember.getMemberId();
        MemberDao.delete(id);
        refresh();
        clear();
    }
    public void refresh(){
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            members = FXCollections.
                    observableList(MemberDao.getAll(con));
            for (Membership m: members) {
                System.out.println(m);
            }
            tablemember.setItems(members);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.closeConnection(con);
        }
    }


    @FXML
    public void save2() throws SQLException {
        Membership temp = new Membership();
        temp.setMemberName(fieldname.getText());
        temp.setPhoneNoMember(Integer.valueOf(fieldphonenumber.getText()));
        MemberDao.save(temp);
        refresh();
        clear();
    }

    @FXML
    public void close(){
            Stage stage = (Stage) btnback.getScene().getWindow();
            try {
                Parent root = CashierApp.loadScene("formTabel_order.fxml");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    @FXML
    public void onClickShow(){
        Membership member = tablemember.getSelectionModel().getSelectedItem();
        fieldphonenumber.setText(String.valueOf(member.getPhoneNoMember()));
        fieldname.setText(member.getMemberName());

    }

    private void clear(){
        fieldname.clear();
        fieldphonenumber.clear();
    }
}
