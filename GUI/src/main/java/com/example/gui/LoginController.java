package com.example.gui;

import com.ossnm.data.User;
import com.ossnm.data.userManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Objects;

public class LoginController {


    @FXML
    private PasswordField RegisterPassword;

    @FXML
    private TextField RegisterUsername;
    @FXML
    private Label MessageFailLogin;
    @FXML
    private Pane Pane;

    @FXML
    protected void onLoginButtonClick() throws UnknownHostException {
       User user = userManager.authenticateUser(RegisterUsername.getText(), RegisterPassword.getText(), userManager.getDB());
       if(user == null){
           MessageFailLogin.setText("Wrong Credentials/User not found");
       }else{
           //Load new FXML of main window where all the password are shown
       }

    }
    @FXML
    protected void onRegisterButtonRedirect() throws IOException {
        Pane newPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        Pane.getChildren().setAll(newPane);


    }
}