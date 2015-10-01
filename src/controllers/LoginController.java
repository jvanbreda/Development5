/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import mmorpg.GameManager;
import mmorpg.HelperTools;
import screensmanagers.*;

/**
 * FXML Controller class
 *
 * @author Jesse
 */
public class LoginController extends ControlledScreen implements Initializable {
    ScreensFramework framework = new ScreensFramework();
    
    @FXML
    private Text login_title;
    @FXML
    private TextField login_userName;
    @FXML
    private PasswordField login_password;
    @FXML
    private Button login_loginButton;
    @FXML
    private Text login_signUp;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    @FXML
    private void goToRegister(MouseEvent event) {
        ScreensFramework.controller.unloadScreen(ScreensFramework.LOGIN);
        framework.setScreen(ScreensFramework.REGISTER);
    }

    @FXML
    private void login(ActionEvent event) {
        if (GameManager.gm.validateLogin(login_userName.getText(), login_password.getText())){
            GameManager.gm.loggedInUser = GameManager.gm.getUser(login_userName.getText());
            ScreensFramework.controller.unloadScreen(ScreensFramework.LOGIN);
            framework.setScreen(ScreensFramework.MAIN_HUB);
            
        }
        
        else {
            HelperTools.showInfoAlert(Alert.AlertType.WARNING, "Error", null, "This user name/password combination is not known in our database, please try again.");
        }
    }

    @FXML
    private void generateData(ActionEvent event) {
        GameManager.gm.generateData();
    }

    
    
}
