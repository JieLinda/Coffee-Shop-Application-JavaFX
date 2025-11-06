module com.example.pboproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.ooxml;
    requires layout;
    requires kernel;
    requires io;
    opens com.example.pboproject.beans to javafx.base;


    opens com.example.pboproject to javafx.fxml;
    opens com.example.pboproject.controllers to javafx.fxml;
    exports com.example.pboproject;
    exports com.example.pboproject.controllers;
}