package com.example.pboproject.controllers;

import com.example.pboproject.beans.DiscountList;
import com.example.pboproject.beans.Menu;
import com.example.pboproject.beans.PaymentMethod;
import com.example.pboproject.beans.ProductType;
import com.example.pboproject.dao.DiscountDao;
import com.example.pboproject.dao.MenuDao;
import com.example.pboproject.dao.PaymentMethodDao;
import com.example.pboproject.utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DiscountTabController{
    @FXML
    private TextField fieldamountPct;
    @FXML
    private DatePicker datestart;
    @FXML
    private DatePicker dateend;
    @FXML
    private TableView<DiscountList> discs;
    @FXML
    private ComboBox<String> allTypes;
    @FXML
    private ComboBox<String> comboPayment;
    @FXML
    private ComboBox<String> combonameProduct;
    private ArrayList<Menu> allMenu;
    private ArrayList<String> allMenuIndexes;
    private ObservableList<DiscountList> listDisc;

    private ArrayList<String> methods;

    public void initialize() throws SQLException {
//        allTypes.setItems(FXCollections.observableArrayList(ProductType.values()));
        loadComboType();
        //setup combo box payment method
        Connection con = ConnectionManager.getConnection();
        //simpen semua payment method
        ObservableList<PaymentMethod> PMs;
        PMs = FXCollections.observableList(PaymentMethodDao.getAll(con));
        //array lit untuk hasil akhir (credit card - bca)
        methods = new ArrayList<>();
        //memasukkan nama metodenya (credit card) dan nama bank (bca)
        for (PaymentMethod pm:PMs){
            String temp = pm.getPaymentMethod()+" - "+pm.getBank();
            methods.add(temp);
        }
        comboPayment.setItems(FXCollections.observableArrayList(methods));

        //setup combo box required name
        allMenu = MenuDao.getAll(con);
        //simpan nama menu
        ArrayList<String> menuName = new ArrayList<>();
        //simpen id menu
        allMenuIndexes = new ArrayList<>();
        for (Menu menu:allMenu){
            //memasukkan nama menu dan id menu
            menuName.add(menu.getProductName());
            allMenuIndexes.add(menu.getProductId());
        }
        combonameProduct.setItems(FXCollections.observableArrayList(menuName));

        //setup table
        discs.getColumns().clear();
        TableColumn id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<DiscountList,Integer>("discId"));

        TableColumn pct = new TableColumn<>("Percentage");
        pct.setCellValueFactory(new PropertyValueFactory<DiscountList,Double>("discPercent"));

        TableColumn start = new TableColumn<>("Date Start");
        start.setCellValueFactory(new PropertyValueFactory<DiscountList,String>("dateStart"));

        TableColumn end = new TableColumn<>("Valid Until");
        end.setCellValueFactory(new PropertyValueFactory<DiscountList,String>("validUntilDate"));

        TableColumn payment = new TableColumn<>("Required Payment Method");
        payment.setCellValueFactory(new PropertyValueFactory<DiscountList,Integer >("requiredPaymentMethod"));

        TableColumn product = new TableColumn<>("Required Product");
        product.setCellValueFactory(new PropertyValueFactory<DiscountList,String>("requiredProduct"));

        TableColumn type = new TableColumn<>("Required Type");
        type.setCellValueFactory(new PropertyValueFactory<DiscountList,ProductType>("requiredType"));
        discs.getColumns().addAll(id,pct,start,end,payment,product,type);

        //inisialisasi data
            listDisc = FXCollections.observableList(DiscountDao.getAll(con));
        System.out.println(listDisc.size());
            discs.setItems(listDisc);
            ConnectionManager.closeConnection(con);
    }

    private void loadComboType() throws SQLException {
        ObservableList<String> types = null;
        Connection con = null;
        String id;
        try {
            con = ConnectionManager.getConnection();
            types = FXCollections.observableArrayList(DiscountDao.getAllColumn(con, "product_type", "menu_list", String.class));

        } catch (Exception e) {
            e.printStackTrace();
        }
        allTypes.setItems(types);
    }

    @FXML
    public void save() throws SQLException {
        DiscountList disc = new DiscountList();
        String[] pmethods = comboPayment.getValue().split(" - ");
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            if (pmethods[0].equalsIgnoreCase("cash")) {
                disc.setRequiredPaymentMethod(DiscountDao.getValueById(con, "payment_id", "payment_method", "payment_method", pmethods[0], Integer.class));
            }
            else if (pmethods[0].equalsIgnoreCase("ALL")){
                disc.setRequiredPaymentMethod(0);
            }
            else {
                disc.setRequiredPaymentMethod(DiscountDao.getValueByCombination(con, "payment_id", "payment_method", "payment_method", pmethods[0], "bank", pmethods[1], Integer.class));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        //simpan index terpilih combo box
        int idx = combonameProduct.getSelectionModel().getSelectedIndex();
        if (idx>=0){
            disc.setRequiredProduct(allMenuIndexes.get(idx));
        }
        //ambil menu_id dengan cara memilih index pada allMenuIndexes (arraylist isinya menu id) berdasarkan index nya combo box (idx) contoh : allMenuIndexes = {1,2,3}, idx = 0, outputnya 1
        disc.setValidUntilDate(String.valueOf(dateend.getValue()));
        disc.setDateStart(String.valueOf(datestart.getValue()));
        disc.setDiscPercent(Double.valueOf(fieldamountPct.getText()));
        String typeID = null;
        if(allTypes.getValue()!=null) {
            try {
                con = ConnectionManager.getConnection();
                if (!combonameProduct.getValue().isEmpty()||combonameProduct.getValue()!=null){
                    typeID = DiscountDao.getValueById(con, "product_id", "menu_list", "product_name", combonameProduct.getValue(), String.class);
                } else {
                    typeID = DiscountDao.getValueById(con, "product_id", "menu_list", "product_type", allTypes.getValue(), String.class);
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            typeID ="-1";
        }
        disc.setRequiredType(typeID);
        if(combonameProduct!=null) {
            try {
                con = ConnectionManager.getConnection();

                typeID = DiscountDao.getValueById(con, "product_id", "menu_list", "product_name", combonameProduct.getValue(), String.class);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            typeID ="-1";
        }
        if (combonameProduct.getValue()!=null) {
            disc.setRequiredProduct(typeID);
        }
        else {
            disc.setRequiredProduct("-1");
        }
        DiscountDao.save(disc);
        refresh();
        clear();
    }

    @FXML
    public void edit() throws SQLException {
        DiscountList clickedDisc = discs.getSelectionModel().getSelectedItem();
        DiscountList disc = new DiscountList();
        String[] pmethods = comboPayment.getValue().split(" - ");
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            if (pmethods[0].equalsIgnoreCase("cash")) {
                disc.setRequiredPaymentMethod(DiscountDao.getValueById(con, "payment_id", "payment_method", "payment_method", pmethods[0], Integer.class));
            }else if (pmethods[0].equalsIgnoreCase("ALL")){
                disc.setRequiredPaymentMethod(0);
            } else {
                disc.setRequiredPaymentMethod(DiscountDao.getValueByCombination(con, "payment_id", "payment_method", "payment_method", pmethods[0], "bank", pmethods[1], Integer.class));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        //simpan index terpilih combo box
        int idx = combonameProduct.getSelectionModel().getSelectedIndex();
        if (idx>=0){
            disc.setRequiredProduct(allMenuIndexes.get(idx));
        }
        //ambil menu_id dengan cara memilih index pada allMenuIndexes (arraylist isinya menu id) berdasarkan index nya combo box (idx) contoh : allMenuIndexes = {1,2,3}, idx = 0, outputnya 1
        disc.setValidUntilDate(String.valueOf(dateend.getValue()));
        disc.setDateStart(String.valueOf(datestart.getValue()));
        disc.setDiscPercent(Double.valueOf(fieldamountPct.getText()));
        String typeID = null;
        if(allTypes.getValue()!=null) {
            try {
                con = ConnectionManager.getConnection();
                if (combonameProduct.getValue()!=null){
                    typeID = DiscountDao.getValueById(con, "product_id", "menu_list", "product_name", combonameProduct.getValue(), String.class);
                } else {
                    typeID = DiscountDao.getValueById(con, "product_id", "menu_list", "product_type", allTypes.getValue(), String.class);
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }  else {
            typeID ="-1";
        }
        disc.setRequiredType(typeID);
        if(combonameProduct.getValue()!= null) {
            try {
                con = ConnectionManager.getConnection();
                typeID = DiscountDao.getValueById(con, "product_id", "menu_list", "product_name", combonameProduct.getValue(), String.class);

            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            typeID ="-1";
        }
        if (combonameProduct.getValue()!=null) {
            disc.setRequiredProduct(typeID);
        } else {
            disc.setRequiredProduct("-1");
        }
        disc.setDiscId(clickedDisc.getDiscId());
        DiscountDao.update(disc);
        refresh();
        clear();
    }

    @FXML
    public void delete() throws SQLException {
        DiscountList clickedDisc = discs.getSelectionModel().getSelectedItem();
        DiscountDao.delete(clickedDisc.getDiscId());
        refresh();
        clear();
    }

    private void refresh() throws SQLException {
        Connection con = ConnectionManager.getConnection();
        discs.setItems(FXCollections.observableArrayList(DiscountDao.getAll(con)));
    }
@FXML
    private void showOnClick() throws SQLException {
        //isi semua field, combo box , dll pas di klik
        DiscountList clickedDisc = discs.getSelectionModel().getSelectedItem();
        fieldamountPct.setText(String.valueOf(clickedDisc.getDiscPercent()));
        dateend.setValue(LocalDate.parse(clickedDisc.getValidUntilDate()));
        datestart.setValue(LocalDate.parse(clickedDisc.getDateStart()));
        combonameProduct.setValue(MenuDao.get(clickedDisc.getRequiredProduct()).getProductName());
        allTypes.setValue(clickedDisc.getRequiredType());
    }

    @FXML
    private void refreshPayment() throws SQLException {
        //refresh payment method apa saja yang ada ada setiap kali di klik
        Connection con = ConnectionManager.getConnection();
        ObservableList<PaymentMethod> PMs;
        PMs = FXCollections.observableList(PaymentMethodDao.getAll(con));
        ArrayList<String> methods = new ArrayList<>();
        for (PaymentMethod pm:PMs){
            String temp = pm.getPaymentMethod()+" - "+pm.getBank();
            methods.add(temp);
        }
        comboPayment.setItems(FXCollections.observableArrayList(methods));
    }

    @FXML
    private void refreshProduct() throws SQLException {
        //refresh product apa saja yang ada setiap kali di klik
        Connection con = ConnectionManager.getConnection();
        allMenu = MenuDao.getAll(con);
        ArrayList<String> menuName = new ArrayList<>();
        allMenuIndexes = new ArrayList<>();
        for (Menu menu:allMenu){
            menuName.add(menu.getProductName());
            allMenuIndexes.add(menu.getProductId());
        }
        combonameProduct.setItems(FXCollections.observableArrayList(menuName));
    }

    @FXML
    private void clear(){
        fieldamountPct.clear();
        combonameProduct.setValue("");
        comboPayment.setValue("");
        allTypes.setValue("");
        dateend.getEditor().clear();
        datestart.getEditor().clear();
    }
}
