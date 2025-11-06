package com.example.pboproject.controllers;

import com.example.pboproject.beans.Username;
import com.example.pboproject.dao.LoginDao;
import com.example.pboproject.utils.WindowOpener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private TextField user;
    @FXML
    private TextField pw;

    @FXML
    private void open() throws IOException, SQLException {
        Username login = new Username();
        login.setUser(user.getText());
        login.setPass(pw.getText());
        if (login.getPass().equals(LoginDao.get(login).getPass())) {
            if (LoginDao.get(login).getType().equals("MASTER")) WindowOpener.openMaster();
            else WindowOpener.openCashier();
        }
    }
}
