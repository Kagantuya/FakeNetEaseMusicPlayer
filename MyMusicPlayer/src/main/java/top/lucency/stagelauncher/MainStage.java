package top.lucency.stagelauncher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import top.lucency.controller.MainController;
import top.lucency.utils.StageUtil;

/**
 * @author 86181
 */
public class MainStage extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/statics/stages/main.fxml"));
        Parent root = fxmlLoader.load();
        
        MainController controller = fxmlLoader.getController();
        controller.init(primaryStage, controller);
        
        StageUtil.diyStage(primaryStage, root, false, controller.rootPane.getPrefWidth(), controller.rootPane.getPrefHeight());
        primaryStage.show();
    }
    
}
