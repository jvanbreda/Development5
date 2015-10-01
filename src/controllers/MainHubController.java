/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mmorpg.GameManager;
import mmorpg.HelperTools;
import screensmanagers.ControlledScreen;
import screensmanagers.ScreensController;
import screensmanagers.ScreensFramework;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * FXML Controller class
 *
 * @author Jesse
 */
public class MainHubController extends ControlledScreen implements Initializable {

    ScreensController controller;
    ScreensFramework framework;

    @FXML
    private Text mainHub_userName;
    @FXML
    private Text mainHub_charName;
    @FXML
    private Text mainHub_charRace;
    @FXML
    private Text mainHub_charClass;
    @FXML
    private Text mainHub_charLevel;
    @FXML
    private Button mainHub_logout;
    @FXML
    private Text mainHub_balance;
    @FXML
    private ComboBox<String> mainHub_charList;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainHubController mhc = this;
        mainHub_charList.getItems().addAll(GameManager.gm.getCharacters());
               
        mainHub_userName.setText(GameManager.gm.loggedInUser.getUserName());
        mainHub_balance.setText(Integer.toString(GameManager.gm.getUserWallet()));
        mainHub_charList.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                mhc.showCharacterInfo(newValue);
            }
            
        });
        
    }

    @FXML
    private void addMoney(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("10");
        dialog.setTitle("Add money to Account");
        dialog.setContentText("Please enter your amount in euros:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            GameManager.gm.updateUserWallet(Integer.parseInt(result.get()));
        }
        mainHub_balance.setText(Integer.toString(GameManager.gm.getUserWallet()));
    }

    @FXML
    private void logout(ActionEvent event) {
        ScreensFramework.controller.unloadScreen(ScreensFramework.MAIN_HUB);
        ScreensFramework.setScreen(ScreensFramework.LOGIN);
    }

    @FXML
    private void buyMembership(ActionEvent event) {
        List<String> choices = new ArrayList<>();
        choices.add("1 month -- 5 euros");
        choices.add("2 months -- 8 euros");
        choices.add("3 months -- 10 euros");
        choices.add("12 months -- 35 euros");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("3 months -- 10 euros", choices);
        dialog.setTitle("Buy membership");
        dialog.setContentText("Choose the length of your membership:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            int months = Integer.parseInt(dialog.getSelectedItem().substring(0, 2).replace(" ", ""));
            int price = Integer.parseInt(dialog.getSelectedItem().substring(dialog.getSelectedItem().lastIndexOf("-"), dialog.getSelectedItem().indexOf("euros")).replace(" ", ""));
            
            System.out.println(price);
            System.out.println(GameManager.gm.getUserWallet());
            
            if (price*-1 > GameManager.gm.getUserWallet()){
                HelperTools.showInfoAlert(Alert.AlertType.WARNING, "Error", null, "You don't have enough money to buy this membership, add some money to your account!");

            }
            
            else {
                GameManager.gm.updateLastPayment(months);
                GameManager.gm.updateUserWallet(price);

                mainHub_balance.setText(Integer.toString(GameManager.gm.getUserWallet()));
            }
        }
    }

    @FXML
    private void makeNewCharacter(ActionEvent event) {
        Object[] characterSlots = GameManager.gm.getCharacterSlots();
        if ((int)characterSlots[0] > (long)characterSlots[1]){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainHubController.class.getResource(ScreensFramework.createCharFXML));
                AnchorPane pane = (AnchorPane) loader.load();

                Stage stage = new Stage();
                stage.setResizable(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(ScreensFramework.stage);
                Scene scene = new Scene(pane);
                stage.setScene(scene);

                NewCharPopUpController popUpController = loader.getController();

                stage.showAndWait();
                mainHub_charList.getItems().clear();
                mainHub_charList.getItems().addAll(GameManager.gm.getCharacters());


            } catch (IOException ioe){
                ioe.printStackTrace();            
            }
        }
        else {
            HelperTools.showInfoAlert(Alert.AlertType.WARNING, "Error", null, "You don't have any more slots available to make a new character, buy more slots to create a new one.");
        }
        
    }

    @FXML
    private void buySlots(ActionEvent event) {
        List<Integer> choices = new ArrayList<>();
        choices.add(1);
        choices.add(2);
        choices.add(3);
        choices.add(4);
        choices.add(5);

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, choices);
        dialog.setTitle("Buy character slots");
        dialog.setContentText("Choose the number of extra character slots you want to buy (1 euro each):");

        Optional<Integer> result = dialog.showAndWait();
        if (result.isPresent()) {
            int extraSlots = dialog.getSelectedItem();
            if (extraSlots > GameManager.gm.getUserWallet()){
                HelperTools.showInfoAlert(Alert.AlertType.WARNING, "Error", null, "You don't have enough money to buy this number of slots. Select less slots or add money to your account!");
            }
            
            else {
                GameManager.gm.updateCharacterSlots(extraSlots);
                GameManager.gm.updateUserWallet(-extraSlots);
                HelperTools.showInfoAlert(Alert.AlertType.INFORMATION, "Succes", null, "Transaction completed.");
                mainHub_balance.setText(Integer.toString(GameManager.gm.getUserWallet()));
            }
        
        }
    }
    
    public void showCharacterInfo(String selectedChar){
        models.Character character = GameManager.gm.getCharacterInfo(selectedChar);
        
        System.out.println(character.getName());
        
        String name = character.getName();
        String race = character.getRace();
        String classs = character.getClass1();
        Integer level = character.getLevel();

        mainHub_charName.setText(name);
        mainHub_charRace.setText(race);
        mainHub_charClass.setText(classs);
        mainHub_charLevel.setText(level.toString());
        
    }
}
