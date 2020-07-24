package top.lucency.stagelauncher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import top.lucency.controller.FsController;
import top.lucency.utils.StageUtil;

/**
 * @author 86181
 */
public class FsStage extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/statics/stages/fs.fxml"));
        Parent root = fxmlLoader.load();
        
        FsController controller = fxmlLoader.getController();
        StageUtil.diyStage(primaryStage, root, true, controller.rootPane.getPrefWidth(), controller.rootPane.getPrefHeight());
        
        // 窗口大小为半屏
        primaryStage.setWidth(StageUtil.screenBounds.getWidth());
        primaryStage.setHeight(StageUtil.screenBounds.getHeight());
        // 窗口置于任务栏上方，由于频谱在下标较大时基本无变化，因此在每个 rectangle 稍宽的时候占用右边部分空间
        primaryStage.setX(100);
        primaryStage.setY(StageUtil.maximumWindowBounds.getHeight() - primaryStage.getHeight());
        
        controller.init(primaryStage, controller);
        primaryStage.show();
    }
    
}
