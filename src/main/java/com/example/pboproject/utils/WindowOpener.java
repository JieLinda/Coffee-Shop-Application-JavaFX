package com.example.pboproject.utils;

import com.example.pboproject.FormPriceInputApp;
import com.example.pboproject.LoginApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowOpener {
    public static void openFormPriceInput() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApp.class.getResource("formPriceInput.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Price Input");
        stage.setScene(scene);
        stage.show();
    }
    public static void openMember() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(FormPriceInputApp.class.getResource("formTableMember.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Member Manager");
        stage.setScene(scene);
        stage.show();
    }
    public static void openMaster() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApp.class.getResource("MainMaster.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Master Control");
        stage.setScene(scene);
        stage.show();
    }
    public static void openCashier() throws IOException{
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApp.class.getResource("formTabel_order.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cashier");
        stage.setScene(scene);
        stage.show();
    }
}
