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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mmorpg.GameManager;
import mmorpg.HelperTools;
import models.User;
import screensmanagers.ScreensFramework;

/**
 * FXML Controller class
 *
 * @author Jesse
 */
public class NewCharPopUpController implements Initializable {

    private Stage stage;
    private boolean okClicked;
    @FXML
    private TextField char_name;
    @FXML
    private ComboBox<String> char_race;
    @FXML
    private ComboBox<String> char_class;
    @FXML
    private Button char_cancel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        char_race.getItems().add("White");
        char_race.getItems().add("Black");
        char_race.getItems().add("Asian");
        char_race.getItems().add("Hispanic");
        
        char_class.getItems().add("Warrior");
        char_class.getItems().add("Wizard");
        char_class.getItems().add("Archer");
    }  
    
    public void setDialogStage(Stage dialogStage){
        this.stage = dialogStage;
    }

    @FXML
    private void createNewChar(ActionEvent event) {
        String name = char_name.getText();
        String race = char_race.getValue();
        String classs = char_class.getValue();
        
        models.Character c = new models.Character(name, classs, race);
        
        GameManager.gm.persist(c);
        GameManager.gm.linkCharToUser(c);
        HelperTools.showInfoAlert(Alert.AlertType.INFORMATION, "Success", null, "Character successfully made!");
        closePopUp(null);
    }    

    @FXML
    private void closePopUp(ActionEvent event) {
        Stage stage = (Stage) char_cancel.getScene().getWindow();
        stage.close();
    }
}
