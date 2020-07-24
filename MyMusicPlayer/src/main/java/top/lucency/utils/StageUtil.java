package top.lucency.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

/**
 * @author 86181
 */
public class StageUtil {
    
    /**
     * 任务栏 logo 图标路径
     */
    public static final String LOGO_DIR = "statics/logo/logo.png";
    
    /**
     * 除去任务栏的电脑屏幕对象
     */
    public static Rectangle maximumWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    
    /**
     * 包括任务栏的电脑屏幕对象
     */
    public static Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    
    /**
     * 任务栏图标
     */
    public static Image image = new Image(LOGO_DIR, 100, 100, true, false);
    
    /**
     * 自定义窗体
     *
     * @param stage         窗体
     * @param root          根节点
     * @param isAlwaysOnTop 是否总是置顶
     * @param width         窗体宽度
     * @param height        窗体高度
     */
    public static void diyStage(Stage stage, Parent root, boolean isAlwaysOnTop, double width, double height) {
        Scene scene = new Scene(root);
        scene.setFill(null);
        stage.setScene(scene);
        
        stage.getIcons().add(image);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(isAlwaysOnTop);
        
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);
    }
    
    private static double xOffSet = 0;
    private static double yOffSet = 0;
    
    /**
     * 添加拖拽
     *
     * @param node  节点
     * @param stage 窗体
     */
    public static void addDragEvent(Node node, Stage stage) {
        node.setOnMousePressed(event -> {
            xOffSet = event.getSceneX();
            yOffSet = event.getSceneY();
        });
        node.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffSet);
            stage.setY(event.getScreenY() - yOffSet);
        });
    }
    
}
