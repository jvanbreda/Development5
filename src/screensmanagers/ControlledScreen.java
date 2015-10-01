/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screensmanagers;
/**
 *
 * @author Jesse
 */
public class ControlledScreen {
    
    ScreensController controller;
    
    public void setScreenParent(ScreensController screensPage){
        controller = screensPage;
    }
    
}
