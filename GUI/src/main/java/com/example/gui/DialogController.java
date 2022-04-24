package com.example.gui;

import com.ossnm.data.userManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class DialogController {


    @FXML
    private TextField addNewAccount;

    @FXML
    private TextField addNewUser;

    @FXML
    private TextField addNewPassword;

    @FXML
    private Button addPassword;

    @FXML
    protected void addPasswordOnClick() throws IOException {
        userManager.addNewPasswordToList(userManager.currentUser, addNewAccount.getText(), addNewUser.getText(), addNewPassword.getText(), userManager.getDB());
        Stage stage = (Stage) addPassword.getScene().getWindow();
        stage.close();

    }


}