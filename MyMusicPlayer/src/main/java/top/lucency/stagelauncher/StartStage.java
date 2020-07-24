package top.lucency.stagelauncher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import top.lucency.controller.StartController;
import top.lucency.utils.StageUtil;

/**
 * @author 86181
 */
public class StartStage extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/statics/stages/start.fxml"));
        Parent root = fxmlLoader.load();
        
        StartController controller = fxmlLoader.getController();
        controller.init(primaryStage);
        
        StageUtil.diyStage(primaryStage, root, true, controller.rootPane.getPrefWidth(), controller.rootPane.getPrefHeight());
        primaryStage.show();
    }

}
