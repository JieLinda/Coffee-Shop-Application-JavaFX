package com.example.pboproject.controllers;

import com.example.pboproject.beans.Menu;
import com.example.pboproject.beans.MenuPrice;
import com.example.pboproject.beans.ProductType;
import com.example.pboproject.beans.Sizes;
import com.example.pboproject.dao.MenuDao;
import com.example.pboproject.dao.MenuPriceDao;
import com.example.pboproject.utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class FormPriceInputController {
    private static ObservableList<Menu> menus;
    private static ObservableList<MenuPrice> prices ;
    @FXML
    private TableView <Menu> menuView;
    @FXML private TableColumn<Menu,String> colProductID;
    @FXML private TableColumn<Menu,String> colProductType;
    @FXML private TableColumn<Menu,String> colProductName;
    @FXML private TextField fieldProductType;
    @FXML
    private  TableView<MenuPrice>sizePriceView;
    @FXML private TableColumn<MenuPrice, String> colPriceID;
    @FXML private TableColumn<MenuPrice, Sizes> colSize;
    @FXML private TableColumn<MenuPrice, String> colPrice;

    @FXML
    private ComboBox<Sizes> comboSize;
    @FXML
    private TextField priceInput;
    @FXML
    private TextField fieldProductID;
    private static String ProductID ;
    private String priceID ;

    @FXML
    private ComboBox<ProductType> comboType;


    @FXML
    private TextField fieldProduct;

    @FXML
    public void addProduct() throws SQLException {
        Menu menu = new Menu();
        menu.setProductId(fieldProductID.getText());
        menu.setProductType(String.valueOf(comboType.getValue()).toUpperCase().toUpperCase());
        menu.setProductName(fieldProduct.getText());
        MenuDao.save(menu);
        refreshMenu();
    }

    @FXML
    public void changeProduct() throws SQLException {
        Menu menu = new Menu();
        menu.setProductType(String.valueOf(comboType.getValue()).toUpperCase());
        menu.setProductName(fieldProduct.getText());
        Menu clickedMenu = menuView.getSelectionModel().getSelectedItem();
        menu.setProductId(clickedMenu.getProductId());
        MenuDao.update(menu);
        refreshMenu();
    }

    public void initialize() throws SQLException {
        System.out.println("Initialize function");
        menus = FXCollections.emptyObservableList();
        menuView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        colProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colProductType.setCellValueFactory(new PropertyValueFactory<>("productType"));
        comboSize.setItems(FXCollections.observableArrayList(Sizes.values()));
        comboType.setItems(FXCollections.observableArrayList(ProductType.values()));
        //Inisialisasi data
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            menus = FXCollections.observableList(MenuDao.getAll(con));
            System.out.println("Loading menus");
            menuView.setItems(menus);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.closeConnection(con);
        }

        colPriceID.setCellValueFactory(new PropertyValueFactory<>("sizePrizeId"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        refresh();
    }

    @FXML
    public void searchPrices() throws SQLException {
        //ngeluarin harga pas klik sebuah menu
        prices = FXCollections.emptyObservableList();
        Menu clickedMenu =  menuView.getSelectionModel().getSelectedItem();
        fieldProduct.setText(clickedMenu.getProductName());
        fieldProductID.setText(clickedMenu.getProductId());
        comboType.setValue(ProductType.valueOf( clickedMenu.getProductType()));
        sizePriceView.getColumns().clear();
        ProductID = clickedMenu.getProductId();
        //Inisialisasi kolom
        TableColumn idCol = new TableColumn("Size");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(
                new PropertyValueFactory<MenuPrice, Sizes>("size"));

        TableColumn nama = new TableColumn("Price");
        nama.setMinWidth(150);
        nama.setCellValueFactory(
                new PropertyValueFactory<MenuPrice , Integer>("price"));

        sizePriceView.getColumns().addAll(idCol,nama);
        refresh();
    }

    @FXML
    public void clickedPrice(){
        //ngisi semua field dan combo box menu saat di klik
        if (!sizePriceView.getItems().isEmpty()) {
            MenuPrice mp = sizePriceView.getSelectionModel().getSelectedItem();
            priceInput.setText(String.valueOf(mp.getPrice()));
            comboSize.setValue(mp.getSize());
        } else {
            System.out.println("sizePriceView is null");
        }
    }

    @FXML
    public void add2() throws SQLException {
        MenuPrice mp = new MenuPrice();
        mp.setPrice(Double.valueOf(priceInput.getText()));
        mp.setSize(comboSize.getValue());
        String sizeID ;
        System.out.println("Mempersiapkan price id utk product: "+ProductID);
        if (ProductID !=null||!ProductID.equalsIgnoreCase("")) {
            sizeID = ProductID;
            switch (comboSize.getValue()){
                case LARGE :
                    sizeID.concat("3");
                    break;
                case MEDIUM :
                    sizeID.concat("2");
                    break;
                case SMALL:
                    sizeID.concat("1");
            }
            System.out.println("Terbentuk price ID "+sizeID);
            mp.setSizePrizeId(sizeID);
        }
        Menu menu = menuView.getSelectionModel().getSelectedItem();
        MenuPriceDao.save(mp,menu);
        refresh();
    }

    @FXML
    public void change2() throws SQLException {
        MenuPrice clickedMenu = sizePriceView.getSelectionModel().getSelectedItem();
        clickedMenu.setSize(comboSize.getValue());
        clickedMenu.setPrice(Double.valueOf(priceInput.getText()));
        MenuPriceDao.update(clickedMenu);
        System.out.println("Save edit for price");
        refresh();
    }

    @FXML
    public void delete2() throws SQLException {
        MenuPrice clickedMenu = sizePriceView.getSelectionModel().getSelectedItem();
        String id = clickedMenu.getSizePrizeId();
        MenuPriceDao.delete(id);
        System.out.println("Delete price");
        refresh();
    }

    @FXML
    public void deleteProduct() throws SQLException {
        Menu clickedMenu = menuView.getSelectionModel().getSelectedItem();
        String id = clickedMenu.getProductId();
        MenuDao.delete(id);
        refresh();
        refreshMenu();
    }

    public void refresh(){
        //auto refresh tabel price saat edit/add/dll
        if (ProductID != null) {
            Connection con = null;
            try {
                con = ConnectionManager.getConnection();
                prices = FXCollections.observableArrayList(MenuPriceDao.getAll(con, ProductID));
                if (prices == null||prices.isEmpty()) System.out.println("prices still empty");
                System.out.println("Refresh prices list");
                sizePriceView.setItems(prices);
                if (!sizePriceView.getItems().isEmpty()) System.out.println("Prices've been Loaded");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                ConnectionManager.closeConnection(con);
            }
        } else {
            System.out.println("ProductID is null");
        }
    }

    public void refreshMenu(){
        //auto refresh tabel menu saat save/edit/dll
        Connection con = null;
        try {
            con = ConnectionManager.getConnection();
            menus = FXCollections.
                    observableList(MenuDao.getAll(con));
            menuView.setItems(menus);
            System.out.println("Refresh Menu list");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.closeConnection(con);
        }
    }
}
