package top.lucency.controller;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import top.lucency.utils.AnimationUtil;
import top.lucency.utils.StageUtil;

/**
 * @author 86181
 */
public class SmallController {
    
    /**
     * 通过初始化传入自身对象，从而可以访问到 fxml 中的控件
     */
    public static SmallController controller;
    
    /**
     * 窗口
     */
    public static Stage stage;
    
    /**
     * 暂停 svg
     */
    public static final String PAUSE_SVG = "M-156,395.3c-8.1,0-14.7,6.6-14.7,14.7s6.6,14.7,14.7,14.7s14.7-6.6,14.7-14.7S-147.9,395.3-156,395.3z M-147,419 c-1.2,1.2-2.5,2.1-4,2.7c-1.6,0.7-3.2,1-5,1s-3.4-0.3-5-1c-1.5-0.6-2.9-1.6-4-2.7s-2.1-2.5-2.7-4c-0.7-1.6-1-3.2-1-5 c0-1.7,0.3-3.4,1-5c0.6-1.5,1.6-2.9,2.7-4c1.2-1.2,2.5-2.1,4-2.7c1.6-0.7,3.2-1,5-1c1.7,0,3.4,0.3,5,1c1.5,0.6,2.9,1.6,4,2.7 c1.2,1.2,2.1,2.5,2.7,4c0.7,1.6,1,3.2,1,5s-0.3,3.4-1,5C-144.9,416.5-145.8,417.8-147,419z M-149.9,402.6h-3.8c-0.1,0-0.2,0.1-0.2,0.2v14.5c0,0.1,0.1,0.2,0.2,0.2h3.8c0.1,0,0.2-0.1,0.2-0.2v-14.5 C-149.7,402.7-149.8,402.6-149.9,402.6z M-158.4,402.6h-3.8c-0.1,0-0.2,0.1-0.2,0.2v14.5c0,0.1,0.1,0.2,0.2,0.2h3.8c0.1,0,0.2-0.1,0.2-0.2v-14.5 C-158.2,402.7-158.3,402.6-158.4,402.6z";
    
    /**
     * 播放 svg
     */
    public static final String PLAY_SVG = "M-19.7,467.5c-8.1,0-14.7,6.6-14.7,14.7s6.6,14.7,14.7,14.7S-5,490.4-5,482.3S-11.6,467.5-19.7,467.5z M-19.7,495.8c-7.4,0-13.5-6.1-13.5-13.5s6.1-13.5,13.5-13.5s13.5,6.1,13.5,13.5S-12.3,495.8-19.7,495.8L-19.7,495.8zM-15.1,480.2l-4.9-3.3c-1.7-1.1-3.1-0.4-3.1,1.7v7.4c0,2,1.4,2.8,3.1,1.7l4.9-3.3C-13.5,483.2-13.5,481.3-15.1,480.2z";
    
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Button quit;
    @FXML
    public Button normal;
    @FXML
    public ToolBar toolbar;
    @FXML
    public Label lyric;
    @FXML
    public ImageView musicPic;
    @FXML
    public Label musicInfo;
    @FXML
    public Button next;
    @FXML
    public Button playOrPause;
    @FXML
    public ToolBar buttons;
    @FXML
    public AnchorPane contents;
    @FXML
    public SVGPath playOrPauseSvg;
    @FXML
    public Label musicInfoHide;
    @FXML
    public Button previous;
    
    /**
     * 鼠标移入
     */
    @FXML
    public void mouseEntered() {
        if (SmallController.controller.musicInfo.getOpacity() == 1) {
            AnimationUtil.fade(SmallController.controller.musicInfo, 0.1, 0, 1, 0);
        }
        if (SmallController.controller.lyric.getOpacity() == 1) {
            AnimationUtil.fade(SmallController.controller.lyric, 0.1, 0, 1, 0);
        }
        if (SmallController.controller.lyric.getOpacity() == 1 || SmallController.controller.musicInfo.getOpacity() == 1) {
            AnimationUtil.fade(buttons, 0.1, 0.1, 0, 1);
        }
        musicInfoHide.setOpacity(1);
        AnimationUtil.fade(musicInfoHide, 0.1, 0, 0, 1);
        AnimationUtil.translate(musicInfoHide, 0.1, 0, 0, 0, 0, 31);
    }
    
    /**
     * 鼠标移出
     */
    @FXML
    public void mouseExited() {
        // 防止频繁执行动画造成错乱
        if (buttons.getOpacity() == 1) {
            if (MainController.controller.playOrPauseSvg.getContent().equals(MainController.PAUSE_SVG)) {
                AnimationUtil.fade(SmallController.controller.lyric, 0.1, 0.1, 0, 1);
            } else {
                AnimationUtil.fade(SmallController.controller.musicInfo, 0.1, 0.1, 0, 1);
            }
            AnimationUtil.fade(buttons, 0.1, 0, 1, 0);
            AnimationUtil.translate(musicInfoHide, 0.1, 0, 0, 0, 31, 0)
                    .setOnFinished(actionEvent -> musicInfoHide.setOpacity(0));
        }
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
     * 播放或暂停
     */
    @FXML
    public void togglePlay() {
        MainController.controller.togglePlay();
    }
    
    /**
     * 打开歌曲详情页
     */
    @FXML
    public void openMusicDetailPane() {
        normal();
        MainController.controller.openMusicDetailPane();
    }
    
    /**
     * 淡出程序
     */
    @FXML
    public void quit() {
        AnimationUtil.fade(rootPane, 1, 0, 1, 0).setOnFinished(actionEvent -> System.exit(0));
    }
    
    /**
     * 恢复大窗口
     */
    @FXML
    public void normal() {
        // 由于主界面一定先于小窗加载，因此不用判断是否为 null ，直接 show
        try {
            stage.close();
            MainController.stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 初始化界面
     *
     * @param stage      界面
     * @param controller 自身
     */
    public void init(Stage stage, SmallController controller) {
        SmallController.controller = controller;
        SmallController.stage = stage;
        
        addDrag();
    }
    
    /**
     * 添加拖拽事件
     */
    private void addDrag() {
        StageUtil.addDragEvent(toolbar, stage);
        StageUtil.addDragEvent(buttons, stage);
        StageUtil.addDragEvent(next, stage);
        StageUtil.addDragEvent(playOrPause, stage);
        StageUtil.addDragEvent(previous, stage);
    }
    
}
