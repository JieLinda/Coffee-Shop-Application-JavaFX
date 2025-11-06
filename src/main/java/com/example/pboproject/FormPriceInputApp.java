package com.example.pboproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class FormPriceInputApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FormPriceInputApp.class.getResource("formPriceInput.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Product Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}