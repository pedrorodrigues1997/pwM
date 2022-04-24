package com.example.gui;

import com.ossnm.data.User;
import com.ossnm.data.userManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.UnknownHostException;

public class HelloController {
    @FXML
    private Label MessageFailRegister;

    @FXML
    private Button pressRegister;

    @FXML
    private PasswordField RegisterPassword;

    @FXML
    private TextField RegisterUsername;

    @FXML
    private TextField RegisterEmail;

    @FXML
    protected void onRegisterButtonClick() {
        String username = RegisterUsername.getText();
        String password = RegisterPassword.getText();
        String email = RegisterEmail.getText();

        if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty()){
            try {
               User newUser = userManager.createNewUser(username, password, email, userManager.getDB());
               if(newUser == null){
                   MessageFailRegister.setText("Password smaller than 8 characters!");
               }

               //skip login and load the new main page


            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }else{
            MessageFailRegister.setText("Values not filled properly!");
        }


    }
}