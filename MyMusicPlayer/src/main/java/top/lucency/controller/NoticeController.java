package top.lucency.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import top.lucency.utils.AnimationUtil;

/**
 * @author 86181
 */
public class NoticeController {
    
    /**
     * 通过初始化传入自身对象，从而可以访问到 fxml 中的控件
     */
    public static NoticeController controller;
    
    /**
     * 在窗口退出前点击的次数
     */
    public static int clickCount = 0;
    
    /**
     * 窗口
     */
    public static Stage stage;
    
    @FXML
    public AnchorPane rootPane;
    @FXML
    public AnchorPane noticePane;
    @FXML
    public Label notice;
    
    /**
     * 初始化界面
     *
     * @param stage      界面
     * @param controller 自身
     */
    public void init(Stage stage, NoticeController controller) {
        NoticeController.controller = controller;
        NoticeController.stage = stage;
    }
    
    /**
     * 显示提示框
     *
     * @param notice 提示信息
     */
    public static void show(String notice) {
        clickCount++;
        Platform.runLater(() -> {
            stage.show();
            controller.notice.setText(notice);
            double fromX = -controller.noticePane.getPrefWidth();
            AnimationUtil.translate(controller.noticePane, 0.5, 0, fromX, 0, 0, 0);
        });
        new Thread(new Wait()).start();
    }
    
}

class Wait extends Thread {
    
    /**
     * 显示时间到了三秒的线程个数
     */
    static int finished = 0;
    
    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            finished++;
            // 个数相等才能隐藏窗口
            if (finished == NoticeController.clickCount) {
                Platform.runLater(() -> {
                    double toX = -NoticeController.controller.noticePane.getPrefWidth();
                    AnimationUtil.translate(NoticeController.controller.noticePane, 0.5, 0, 0, toX, 0, 0)
                            .setOnFinished(actionEvent -> {
                                NoticeController.controller.notice.setText("");
                                NoticeController.stage.close();
                                NoticeController.clickCount = 0;
                                finished = 0;
                            });
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
