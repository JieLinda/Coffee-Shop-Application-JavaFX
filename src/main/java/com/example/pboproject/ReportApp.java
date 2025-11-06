package com.example.pboproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportApp extends Application{
        @Override
        public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(ReportApp.class.getResource("Reports.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Reports");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }

}
