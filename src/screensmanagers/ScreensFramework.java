/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screensmanagers;

import java.util.HashMap;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Jesse
 */
public class ScreensFramework extends Application {
    
    public static HashMap<String, String> screens = new HashMap<>();
    public static ScreensController controller = new ScreensController();
    public static Stage stage;
    
    public static String LOGIN = "login";
    public static String loginScreenFXML = "/fxml/Login.fxml";
    public static String REGISTER = "register";
    public static String registerScreenFXML = "/fxml/Register.fxml";
    public static String MAIN_HUB = "mainHub";
    public static String mainHubFXML = "/fxml/MainHub.fxml";
    public static String CREATE_CHAR = "createChar";
    public static String createCharFXML = "/fxml/newCharPopUp.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        
        screens.put(LOGIN, loginScreenFXML);
        screens.put(REGISTER, registerScreenFXML);
        screens.put(MAIN_HUB, mainHubFXML);
        screens.put(CREATE_CHAR, createCharFXML);
        
        controller.loadScreen(LOGIN, screens.get(LOGIN));
        controller.setScreen(LOGIN);
        
        Scene scene = new Scene(controller);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void setScreen(String screen){
        if(screens.get(screen) != null){
            for (String key : screens.keySet()){
                controller.unloadScreen(key);
            }
        if (controller.getScreen(screen) == null)
            controller.loadScreen(screen, screens.get(screen));
        
        controller.setScreen(screen);
            
        }
    }
    
    public static void setWindowSize(){
        stage.sizeToScene();
    }
    
    public static void main(String[] args){
        launch(args);
    }
    
}
