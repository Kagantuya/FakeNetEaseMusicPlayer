package top.lucency.controller;

import top.lucency.utils.AnimationUtil;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import top.lucency.stagelauncher.MainStage;

/**
 * @author 86181
 */
public class StartController {
    
    @FXML
    public AnchorPane rootPane;
    @FXML
    public SVGPath logoNote;
    @FXML
    public SVGPath logoDisk;
    @FXML
    public Circle wave;
    
    /**
     * 初始化
     *
     * @param stage 界面
     */
    public void init(Stage stage) {
        setLogo(stage);
        setWaveAnimation();
    }
    
    /**
     * 设置启动动画
     *
     * @param stage 界面
     */
    private void setLogo(Stage stage) {
        // 动画序列
        AnimationUtil.fade(logoNote, 1.2, 0, 0, 1);
        AnimationUtil.rotate(logoNote, 0.5, 0.5, 0, -15);
        AnimationUtil.fade(logoDisk, 1.2, 0, 0, 1);
        
        AnimationUtil.scale(logoNote, 1.2, 1.5, 0.2, 0.21, 0.2, 0.21);
        AnimationUtil.scale(logoDisk, 1.2, 1.5, 0.2, 0.21, 0.2, 0.21);
        AnimationUtil.fade(logoNote, 1.2, 1.5, 1, 0);
        
        // 结束动画开启主界面，关闭当前窗口
        AnimationUtil.fade(logoDisk, 1.2, 1.5, 1, 0).setOnFinished(actionEvent -> {
            try {
                new MainStage().start(new Stage());
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * 设置波纹动画
     */
    private void setWaveAnimation() {
        AnimationUtil.scale(wave, 1.2, 0.2, 1, 1.8, 1, 1.8);
        AnimationUtil.fade(wave, 1.2, 0.2, 1, 0);
    }
    
}
