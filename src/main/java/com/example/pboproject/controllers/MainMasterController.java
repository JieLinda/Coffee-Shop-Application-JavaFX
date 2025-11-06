package com.example.pboproject.controllers;

import com.example.pboproject.beans.*;
import com.example.pboproject.dao.MenuDao;
import com.example.pboproject.dao.MenuPriceDao;
import com.example.pboproject.utils.ConnectionManager;
import com.example.pboproject.utils.WindowOpener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainMasterController {
    @FXML
    private TableView<MenuAndPrice> allMenu;

    public void initialize() throws SQLException {
        System.out.println("Initialize function");
        allMenu.getColumns().clear();
        //Inisialisasi kolom
        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(
                new PropertyValueFactory<MenuAndPrice, Integer>("productId"));

        TableColumn nama = new TableColumn("Product Name");
        nama.setMinWidth(150);
        nama.setCellValueFactory(
                new PropertyValueFactory<MenuAndPrice, String>("productName"));

        TableColumn tipe = new TableColumn("Type");
        tipe.setMinWidth(25);
        tipe.setCellValueFactory(
                new PropertyValueFactory<MenuAndPrice, ProductType>("productType"));

        TableColumn priceId = new TableColumn("Price ID");
        priceId.setMinWidth(25);
        priceId.setCellValueFactory(
                new PropertyValueFactory<MenuAndPrice, Integer>("sizePriceId"));

        TableColumn size = new TableColumn("Size");
        size.setMinWidth(25);
        size.setCellValueFactory(
                new PropertyValueFactory<MenuAndPrice, Sizes>("size"));

        TableColumn price = new TableColumn("Price");
        price.setMinWidth(150);
        price.setCellValueFactory(
                new PropertyValueFactory<Menu, Integer>("price"));

       allMenu.getColumns().addAll(idCol,nama,tipe,priceId,size,price);

        //Inisialisasi data
        Connection con = ConnectionManager.getConnection();
        ObservableList<MenuAndPrice> finalMAPs;
        ArrayList<MenuAndPrice> MAPs = new ArrayList<>();
        ObservableList<Menu> menus;
        menus = FXCollections.observableList(MenuDao.getAll(con));
        for (Menu menu : menus) {
            ObservableList<MenuPrice> MPs;
            MPs = FXCollections.observableList(MenuPriceDao.getAll(con,menu.getProductId()));
            for (MenuPrice mp:MPs){
                MenuAndPrice map = new MenuAndPrice();
                map.setPrice(mp.getPrice());
                map.setSize(mp.getSize());
                map.setProductId(menu.getProductId());
                map.setSizePriceId(mp.getSizePrizeId());
                map.setProductType(menu.getProductType());
                map.setProductName(menu.getProductName());
                MAPs.add(map);
            }
        }
        finalMAPs = FXCollections.observableList(MAPs);
        ConnectionManager.closeConnection(con);
        allMenu.setItems(finalMAPs);
    }

    @FXML
    public void input() throws IOException {
        //buka formPriceInput
        WindowOpener.openFormPriceInput();
    }

    @FXML
    public void refresh() throws SQLException {
        Connection con = ConnectionManager.getConnection();
        ObservableList<MenuAndPrice> finalMAPs;
        ArrayList<MenuAndPrice> MAPs = new ArrayList<>();
        ObservableList<Menu> menus;
        menus = FXCollections.observableList(MenuDao.getAll(con));
        for (Menu menu : menus) {
            ObservableList<MenuPrice> MPs;
            MPs = FXCollections.observableList(MenuPriceDao.getAll(con,menu.getProductId()));
            for (MenuPrice mp:MPs){
                MenuAndPrice map = new MenuAndPrice();
                map.setPrice(mp.getPrice());
                map.setSize(mp.getSize());
                map.setProductId(menu.getProductId());
                map.setSizePriceId(mp.getSizePrizeId());
                map.setProductType(menu.getProductType());
                map.setProductName(menu.getProductName());
                MAPs.add(map);
            }
        }
        finalMAPs = FXCollections.observableList(MAPs);
        ConnectionManager.closeConnection(con);
        allMenu.setItems(finalMAPs);
    }
}
