/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.*;
import screensmanagers.*;
import mmorpg.*;

/**
 * FXML Controller class
 *
 * @author Jesse
 */
public class RegisterController extends ControlledScreen implements Initializable {
    ScreensFramework framework = new ScreensFramework();
    
    @FXML
    private TextField register_firstName;
    @FXML
    private TextField register_lastName;
    @FXML
    private TextField register_IBAN;
    @FXML
    private TextField register_userName;
    @FXML
    private PasswordField register_password;
    @FXML
    private PasswordField register_confirmedPassword;
    @FXML
    private Button register_register;
    @FXML
    private CheckBox register_acceptTC;
    @FXML
    private Button register_back;
    @FXML
    private ComboBox<Integer> serverList;
    
    List<Server> listOfServers = GameManager.gm.getServerList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        register_register.setDisable(true);
        for (Server s : listOfServers)
            serverList.getItems().add(s.getAddress());
    }    

    @FXML
    private void goToLogin(MouseEvent event) {
        ScreensFramework.controller.unloadScreen(ScreensFramework.REGISTER);
        ScreensFramework.setScreen(ScreensFramework.LOGIN);
    }

    @FXML
    private void register(ActionEvent event) {
        User checkedUserName = GameManager.gm.getUser(register_userName.getText());
        
        if (!register_password.getText().equals(register_confirmedPassword.getText())){
            HelperTools.showInfoAlert(Alert.AlertType.WARNING, "Error", null, "Your password is not the same as your confirmed password, please check again");
            register_password.clear();
            register_confirmedPassword.clear();
        }
                
        else if (checkedUserName != null){
            HelperTools.showInfoAlert(Alert.AlertType.WARNING, "Error", null, "User name already in use. Pick another one please.");
            register_userName.clear();
            register_password.clear();
            register_confirmedPassword.clear();
        }
        
        else {
            Integer address = serverList.getValue();
            Integer[] connections = GameManager.gm.getServerConnections(address);
            if (connections[0] > connections[1]){
                
                User u = new User(register_firstName.getText(), 
                                register_lastName.getText(), 
                                register_IBAN.getText(), 
                                register_userName.getText(), 
                                register_password.getText());


                GameManager.gm.persist(u);
                GameManager.gm.linkServerToUser(u, address);
                HelperTools.showInfoAlert(Alert.AlertType.INFORMATION, "Success", null, "Account registered, log in to use your account");
                goToLogin(null);
            }
            
            else {
                HelperTools.showInfoAlert(Alert.AlertType.WARNING, "Error", null, "This server is already full, select another one please");
            }
        }
    }

    @FXML
    private void enableRegister(ActionEvent event) {
        if (register_acceptTC.isSelected() && !register_firstName.getText().equals("") && !register_lastName.getText().equals("") && !register_IBAN.getText().equals("") && !register_userName.getText().equals("") && !register_password.getText().equals("") && !register_confirmedPassword.getText().equals("") && serverList.getValue() != null){
            register_register.setDisable(false);
        }
        
        else {
            register_register.setDisable(true);
        }
    }
    
}
