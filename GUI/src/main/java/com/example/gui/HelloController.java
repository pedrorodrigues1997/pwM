package com.example.gui;

import com.ossnm.data.User;
import com.ossnm.data.userManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Objects;

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
    private Pane Pane;

    @FXML
    protected void onRegisterButtonClick() throws IOException {
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
                ScrollPane newPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
                Pane.getChildren().setAll(newPane);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }else{
            MessageFailRegister.setText("Values not filled properly!");
        }


    }
}