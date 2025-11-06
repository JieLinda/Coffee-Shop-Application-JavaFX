package com.example.pboproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CashierApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CashierApp.class.getResource("formTabel_order.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Transaction");
        stage.setScene(scene);
        stage.show();
    }

    public static Parent loadScene(String name) throws IOException {
        return FXMLLoader.load(CashierApp.class.getResource(name));
    }
    public static void main(String[] args) {
        launch();
    }
}