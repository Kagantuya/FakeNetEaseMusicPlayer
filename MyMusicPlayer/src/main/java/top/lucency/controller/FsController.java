package top.lucency.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import top.lucency.utils.StageUtil;

import java.util.ArrayList;

/**
 * @author 86181
 */
public class FsController {
    
    /**
     * 通过初始化传入自身对象，从而可以访问到 fxml 中的控件
     */
    public static FsController controller;
    
    /**
     * 窗口
     */
    public static Stage stage;
    
    /**
     * 频谱条数目
     */
    public static ArrayList<Rectangle> rectangles = new ArrayList<>();
    
    /**
     * 频谱条
     */
    Rectangle rectangle;
    
    /**
     * 每个 rectangle 圆角大小
     */
    int arcSize = 8;
    
    /**
     * 每个 rectangle 宽度
     */
    int recWidth = 8;
    
    /**
     * 一半 rectangle 宽度
     */
    int halfRecWidth = recWidth / 2;
    
    /**
     * 每个 rectangle 间隔
     */
    int spacing = 3;
    
    /**
     * rectangle 数量
     */
    public static int recCount = 128;
    
    @FXML
    public AnchorPane rootPane;
    
    /**
     * 初始化界面
     *
     * @param stage      界面
     * @param controller 自身
     */
    public void init(Stage stage, FsController controller) {
        FsController.controller = controller;
        FsController.stage = stage;
        
        bindRootPane();
        initRectangle();
    }
    
    /**
     * 根控件绑定窗口大小
     */
    private void bindRootPane() {
        rootPane.setPrefWidth(StageUtil.screenBounds.getWidth());
        rootPane.setPrefHeight(StageUtil.screenBounds.getHeight());
    }
    
    /**
     * 初始化柱状图
     */
    private void initRectangle() {
        double centerX = stage.getWidth() / 2;
        
        for (int i = 0; i < recCount; i++) {
            rectangle = new Rectangle(recWidth, 0);
            rectangle.setStyle("-fx-arc-height: " + arcSize + ";-fx-arc-width: " + arcSize + ";");
            // 左部柱状图
            if (i < 64) {
                rectangle.setX(centerX - (recWidth + spacing) * i - halfRecWidth);
            } else {
                // 右部柱状图
                rectangle.setX(centerX + spacing * (i % 64 + 1) + recWidth * (i % 64) + halfRecWidth);
            }
            rectangle.setY(rootPane.getPrefHeight());
            rectangles.add(rectangle);
            rectangle.setOpacity(0.5);
            rootPane.getChildren().add(rectangle);
        }
    }
    
}
