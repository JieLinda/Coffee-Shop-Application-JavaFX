package com.example.pboproject.controllers;

import com.example.pboproject.beans.Menu;
import com.example.pboproject.beans.PaymentMethod;
import com.example.pboproject.beans.PaymentMethods;
import com.example.pboproject.dao.PaymentMethodDao;
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

public class PaymentTabController {
    private  ObservableList<PaymentMethod> PM;
    @FXML
    private TextField fieldbank;
    @FXML
    private ComboBox<PaymentMethods> combopaymentmethod;
    @FXML
    private TableView<PaymentMethod> tablePayment;
    public void initialize() throws SQLException {
        PM = FXCollections.emptyObservableList();
        tablePayment.getColumns().clear();
        //Inisialisasi kolom
        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(
                new PropertyValueFactory<PaymentMethod, Integer>("paymentId"));

        TableColumn nama = new TableColumn("Payment Method");
        nama.setMinWidth(150);
        nama.setCellValueFactory(
                new PropertyValueFactory<PaymentMethod, String>("paymentMethod"));
        TableColumn bank = new TableColumn("Bank");
        bank.setMinWidth(20);
        bank.setCellValueFactory(
                new PropertyValueFactory<Menu, String>("bank"));
        tablePayment.getColumns().addAll(idCol,nama,bank);
        combopaymentmethod.setItems(FXCollections.observableArrayList(PaymentMethods.values()));

        //isi data
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            PM = FXCollections.
                    observableList(PaymentMethodDao.getAll(con));
            tablePayment.setItems(PM);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.closeConnection(con);
        }

    }
    
    @FXML
    public void save() throws SQLException {
        PaymentMethod newInput = new PaymentMethod();
        newInput.setBank(fieldbank.getText());
        newInput.setPaymentMethod(String.valueOf(combopaymentmethod.getValue()));
        PaymentMethodDao.save(newInput);
        refresh();
    }

    @FXML
    public void edit() throws SQLException {
        PaymentMethod clickedMethod = tablePayment.getSelectionModel().getSelectedItem();
        clickedMethod.setPaymentMethod(String.valueOf(combopaymentmethod.getValue()));
        clickedMethod.setBank(fieldbank.getText());
        PaymentMethodDao.update(clickedMethod);
        refresh();
    }

    public void refresh() throws SQLException {
        //refresh tabel setelah save/edit/dll
        Connection con = ConnectionManager.getConnection();
        PM = FXCollections.observableList(PaymentMethodDao.getAll(con));
        tablePayment.setItems(PM);
    }

    @FXML
    public void delete() throws SQLException {
        PaymentMethod clickedMethod = tablePayment.getSelectionModel().getSelectedItem();
        PaymentMethodDao.delete(clickedMethod.getPaymentId());
        refresh();
    }

    @FXML
    public void showOnClick(){
        //ngisis emua field dll saat di klik
        PaymentMethod clickedMethod = tablePayment.getSelectionModel().getSelectedItem();
        fieldbank.setText(clickedMethod.getBank());
        combopaymentmethod.setValue(PaymentMethods.valueOf(clickedMethod.getPaymentMethod()));
    }

}
