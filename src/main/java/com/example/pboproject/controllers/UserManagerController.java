package com.example.pboproject.controllers;

import com.example.pboproject.beans.Username;
import com.example.pboproject.dao.LoginDao;
import com.example.pboproject.utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class UserManagerController {
    @FXML
    private TextField fieldUser;
    @FXML
    private TextField fieldPass;
    @FXML
    private TableView<Username> tableUser;
    @FXML
    private ComboBox<String> comboType;
    private ObservableList<Username> users;
    public void initialize(){
        users = FXCollections.emptyObservableList();
        tableUser.getColumns().clear();
        //Inisialisasi kolom
        TableColumn idCol = new TableColumn("Username");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Username, String >("user"));

        TableColumn passCol = new TableColumn("Password");
        passCol.setMinWidth(150);
        passCol.setCellValueFactory(
                new PropertyValueFactory<Username, String>("pass"));
        TableColumn tipe = new TableColumn("Type");
        tipe.setMinWidth(150);
        tipe.setCellValueFactory(
                new PropertyValueFactory<Username, String>("type"));
        tableUser.getColumns().addAll(idCol,passCol,tipe);

        //Inisialisasi data
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            users = FXCollections.
                    observableList(LoginDao.getAll(con));
            tableUser.setItems(users);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.closeConnection(con);
        }
        comboType.setItems(FXCollections.observableArrayList("MASTER","CASHIER"));
    }
    @FXML
    private void add() throws SQLException {
        Username user = new Username();
        user.setPass(fieldPass.getText());
        user.setUser(fieldUser.getText());
        user.setType(comboType.getValue());
        LoginDao.save(user);
        refresh();
        clear();
    }

    @FXML
    private void change() throws SQLException {
        Username user = new Username();
        Username selectedAtTable = tableUser.getSelectionModel().getSelectedItem();
        user.setPass(fieldPass.getText());
        user.setUser(fieldUser.getText());
        user.setType(comboType.getValue());
        LoginDao.update(user,selectedAtTable);
        refresh();
        clear();
    }

    @FXML
    private void delete() throws SQLException {
        Username user = tableUser.getSelectionModel().getSelectedItem();
        LoginDao.delete(user);
        refresh();
    }

    @FXML
    private void showOnClick(){
        Username user = tableUser.getSelectionModel().getSelectedItem();
        fieldPass.setText(user.getPass());
        fieldUser.setText(user.getUser());
        comboType.setValue(user.getType());
    }

    private void refresh() throws SQLException {
        Connection con = ConnectionManager.getConnection();
        tableUser.setItems(FXCollections.observableArrayList(LoginDao.getAll(con)));
    }

    private void clear(){
        fieldPass.setText("");
        fieldUser.setText("");
        comboType.setValue("");
    }
}
