package top.lucency.stagelauncher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import top.lucency.controller.LyricController;
import top.lucency.utils.StageUtil;

/**
 * @author 86181
 */
public class LyricStage extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/statics/stages/lyric.fxml"));
        Parent root = fxmlLoader.load();
        
        LyricController controller = fxmlLoader.getController();
        controller.init(primaryStage, controller);
        
        StageUtil.diyStage(primaryStage, root, true, controller.rootPane.getPrefWidth(), controller.rootPane.getPrefHeight());
        
        primaryStage.setY(5);
        primaryStage.show();
    }
    
}
