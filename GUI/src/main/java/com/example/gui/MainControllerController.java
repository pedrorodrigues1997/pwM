package com.example.gui;

import com.ossnm.data.User;
import com.ossnm.data.passwordHelp;
import com.ossnm.data.userManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class MainControllerController {


    @FXML
    private TableColumn<Object, Object> col1;

    @FXML
    private TableColumn<Object, Object> col2;

    @FXML
    private TableColumn<Object, Object> col3;

    @FXML
    private Button addPassword;
    @FXML
    private Label MessageFailLogin;
    @FXML
    private TableView tableView;

    @FXML
    protected void addPassword() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addNewPassword.fxml")));
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Add new Password");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void populateTable() {
        ObservableList<HashMap<String, Pair<String, String>>> data = FXCollections.observableArrayList();
        User user = userManager.currentUser;
        if (user == null) {
            //Fatal Error abort
        }

        data.add(user.getPasswordList());

        col1.setCellValueFactory(new PropertyValueFactory<>("AccountName"));
        col2.setCellValueFactory(new PropertyValueFactory<>("UserName"));
        col3.setCellValueFactory(new PropertyValueFactory<>("password"));
        ObservableList<passwordHelp> passwordHelps = FXCollections.observableArrayList();
        user.getPasswordList().forEach((account, pair) -> {

            passwordHelps.add(new passwordHelp(account, pair.getKey(), pair.getValue()));


        });
        tableView.setItems(passwordHelps);

    }

}