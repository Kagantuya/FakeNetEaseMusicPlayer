package top.lucency.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import top.lucency.utils.StageUtil;

/**
 * @author 86181
 */
public class LyricController {
    
    /**
     * 通过初始化传入自身对象，从而可以访问到 fxml 中的控件
     */
    public static LyricController controller;
    
    /**
     * 窗口
     */
    public static Stage stage;
    
    /**
     * 歌词面板暂停播放 svg
     */
    public static final String PAUSE_SVG = "M-47.7,458.3c-1.4,0-2.7,1-2.7,2.1v15.7c0,1.1,1.3,2.1,2.7,2.1c1.4,0,2.7-1,2.7-2.1v-15.7C-45,459.3-46.3,458.3-47.7,458.3z M-60.3,458.3c-1.4,0-2.7,1-2.7,2.1v15.7c0,1.1,1.3,2.1,2.7,2.1c1.4,0,2.7-1,2.7-2.1v-15.7C-57.6,459.3-58.9,458.3-60.3,458.3z";
    
    /**
     * 歌词面板播放 svg
     */
    public static final String PLAY_SVG = "M-86.5,453.1l-12.5,7.5c-1.3,0.8-3,0.5-3.8-0.7c-0.3-0.4-0.5-0.9-0.5-1.4v-15.1c0-1.4,1.2-2.5,2.8-2.5c0.5,0,1.1,0.1,1.5,0.4l12.5,7.5c1.3,0.8,1.6,2.3,0.8,3.4C-86,452.6-86.2,452.9-86.5,453.1L-86.5,453.1z";
    
    /**
     * 歌词面板解锁 svg
     */
    private static final String UNLOCK = "M-166.8,347.3h9.8c0.5,0,0.9,0.5,0.9,1.1v8.3c0,0.6-0.4,1.1-0.9,1.1h-9.8c-0.5,0-0.9-0.5-0.9-1.1v-8.3 C-167.7,347.8-167.3,347.3-166.8,347.3z M-162.4,352.9v1.8c0,0.1,0.1,0.1,0.1,0.1h0.8c0.1,0,0.1-0.1,0.1-0.1v-1.8 c0.4-0.3,0.6-0.8,0.6-1.3c0-0.8-0.5-1.4-1.1-1.4c-0.6,0-1.1,0.6-1.1,1.4C-163,352.2-162.8,352.6-162.4,352.9L-162.4,352.9z M-153.2,344.4c0-1.5-1.1-2.9-2.5-2.9c-1.3,0-2.5,1.3-2.5,2.9v3h-1.4v-3.1c0-2.5,1.7-4.7,4-4.7 c2.1,0,4,2,4,4.7v3h-1.5L-153.2,344.4L-153.2,344.4z";
    
    /**
     * 歌词面板锁定 svg
     */
    private static final String LOCK = "M-82.8,401.3c2.1,0,3.8,1.4,4,3.3h2.4c-0.3-3.1-3.1-5.6-6.5-5.6s-6.2,2.5-6.5,5.6h2.6C-86.5,402.7-84.8,401.3-82.8,401.3z M-76.7,406h-12.5c-1.5,0-2.7,1.1-2.7,2.5v8c0,1.4,1.2,2.5,2.7,2.5h12.5c1.5,0,2.7-1.1,2.7-2.5v-8C-74,407.1-75.2,406-76.7,406z M-81.8,412.7v1.8c0,0.6-0.5,1.1-1.2,1.1c-0.7,0-1.2-0.5-1.2-1.1v-1.8c-0.3-0.3-0.5-0.7-0.5-1.1c0-0.9,0.8-1.6,1.7-1.6s1.7,0.7,1.7,1.6C-81.3,412-81.5,412.4-81.8,412.7z";
    
    /**
     * 歌词面板锁定样式
     */
    String lockStyle = "-fx-border-color: transparent;-fx-background-color: transparent";
    
    /**
     * 歌词面板解锁样式
     */
    String unlockStyle = "-fx-border-color:  rgba(0, 0, 0, 0.3);-fx-background-color:  rgba(0, 0, 0, 0.1)";
    
    /**
     * 当前是否被锁定
     */
    public static boolean isLock = false;
    
    @FXML
    public Button previous;
    @FXML
    public Button playOrPause;
    @FXML
    public Button next;
    @FXML
    public Button lock;
    @FXML
    public Button close;
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Label lyric;
    @FXML
    public AnchorPane lyricPane;
    @FXML
    public SVGPath playOrPauseSvg;
    @FXML
    public SVGPath lockSvg;
    
    /**
     * 锁定或解锁歌词面板
     */
    @FXML
    public void toggleLock() {
        // 锁定歌词面板
        if (lockSvg.getContent().equals(UNLOCK)) {
            lockSvg.setContent(LOCK);
            lyricPane.setStyle(lockStyle);
            playOrPause.setOpacity(0);
            next.setOpacity(0);
            close.setOpacity(0);
            previous.setOpacity(0);
            lock.setOpacity(0);
            isLock = true;
        } else {
            // 解锁歌词面板
            lockSvg.setContent(UNLOCK);
            lyricPane.setStyle(unlockStyle);
            playOrPause.setOpacity(1);
            next.setOpacity(1);
            close.setOpacity(1);
            previous.setOpacity(1);
            lock.setOpacity(1);
            isLock = false;
        }
    }
    
    /**
     * 关闭窗口
     */
    @FXML
    public void close() {
        stage.close();
        MainController.controller.lyricBigSvg.setFill(Color.web("#676767"));
    }
    
    /**
     * 播放或暂停
     */
    @FXML
    public void playOrPause() {
        MainController.controller.togglePlay();
    }
    
    @FXML
    public void previous(){
        MainController.controller.previous();
    }
    
    /**
     * 下一首歌
     */
    @FXML
    public void next() {
        MainController.controller.next();
    }
    
    /**
     * 锁定窗口时鼠标移入按钮出现
     */
    @FXML
    public void mouseEntered() {
        if (isLock && LyricController.controller.lock.getOpacity() == 0) {
            new LyricPaneThread().start();
        }
    }
    
    /**
     * 初始化界面
     *
     * @param stage      界面
     * @param controller 自身
     */
    public void init(Stage stage, LyricController controller) {
        LyricController.controller = controller;
        LyricController.stage = stage;
        
        StageUtil.addDragEvent(lyricPane, stage);
    }
    
}

class LyricPaneThread extends Thread {
    
    /**
     * 歌词面板锁定按钮出现
     */
    @Override
    public void run() {
        try {
            LyricController.controller.lock.setOpacity(1);
            Thread.sleep(3000);
            
            // 若面板被锁定则隐藏锁定按钮
            if (LyricController.isLock) {
                LyricController.controller.lock.setOpacity(0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
