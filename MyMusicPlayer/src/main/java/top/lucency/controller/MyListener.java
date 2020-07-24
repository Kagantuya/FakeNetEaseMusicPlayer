package top.lucency.controller;

import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import top.lucency.utils.AnimationUtil;
import top.lucency.utils.MusicUtil;

import java.util.ArrayList;

/**
 * @author 86181
 */
public class MyListener {
    
    /**
     * 滑块监听器
     */
    public static ChangeListener<Number> sliderListener;
    
    /**
     * 文本框监听器
     */
    public static ChangeListener<Color> textListener;
    
    /**
     * svgPath 监听器
     */
    public static ChangeListener<Color> svgPathListener;
    
    /**
     * css 背景色监听器
     */
    public static ChangeListener<Color> backGroundListener;
    
    /**
     * 进度条监听器
     */
    public static ChangeListener<Color> progressesListener;
    
    /**
     * 歌单类型标题文字颜色监听器
     */
    public static ChangeListener<Color> catsTitlesListener;
    
    /**
     * 频谱颜色监听器
     */
    public static ChangeListener<Color> fsListener;
    
    /**
     * 当前 cat 边框颜色监听器
     */
    public static ChangeListener<Color> currentCatBorderListener;
    
    /**
     * 主界面左侧栏选中项目的边框监听器
     */
    public static ChangeListener<Color> borderListener;
    
    /**
     * 播放器监听器
     */
    public static ChangeListener<Duration> playerListener;
    
    /**
     * 频谱监听器
     */
    public static AudioSpectrumListener audioListener;
    
    /**
     * 设置滑块监听器
     *
     * @param pane     滑块父级容器
     * @param slider   滑块
     * @param progress 进度条
     * @param color    颜色
     * @return 滑块监听器
     */
    public static ChangeListener<Number> setSliderListener(StackPane pane, Slider slider, Rectangle progress, String color) {
        sliderListener = null;
        sliderListener = (observableValue, number, t1) -> {
            double max = slider.getMax();
            switch (pane.getId()) {
                case "musicPane":
                    MainController.controller.positions.set(0, (double) t1);
                    progress.setStyle(String.format("-fx-fill: linear-gradient(to right, " + color + " %f%%, #c3c3c3 %f%%);", (double) t1 / max * 100, (double) t1 / max * 100));
                    break;
                case "soundPane":
                    MainController.controller.positions.set(1, (double) t1);
                    // 当音量为 0 ，修改音量按钮的 svg
                    if ((double) t1 == 0) {
                        MainController.controller.sound.setContent(MainController.NO_SOUND);
                    } else {
                        MainController.controller.sound.setContent(MainController.SOUND);
                    }
                    MainController.currentSound = (double) t1;
                    progress.setStyle(String.format("-fx-fill: linear-gradient(to right, " + color + " %f%%, #c3c3c3 %f%%);", (double) t1 * 100, (double) t1 * 100));
                    break;
                default:
                    break;
            }
        };
        return sliderListener;
    }
    
    /**
     * 添加滑块监听器
     *
     * @param pane     滑块父级容器
     * @param slider   滑块
     * @param progress 进度条
     * @param color    颜色
     */
    public static void addSliderListener(StackPane pane, Slider slider, Rectangle progress, String color) {
        slider.valueProperty().addListener(setSliderListener(pane, slider, progress, color));
    }
    
    /**
     * 移除滑块监听器
     */
    public static void removeSliderListener() {
        if (sliderListener != null) {
            MainController.controller.sliders.get(0).valueProperty().removeListener(sliderListener);
            MainController.controller.sliders.get(1).valueProperty().removeListener(sliderListener);
        }
    }
    
    /**
     * 移除颜色监听器
     *
     * @param listener 颜色监听器
     */
    public static void removeColorListener(ChangeListener<Color> listener) {
        if (listener != null) {
            AnimationUtil.fromColor.removeListener(listener);
        }
    }
    
    /**
     * 设置文本监听器
     *
     * @param node     node
     * @param width    控件宽度
     * @param fontSize 字体
     * @return 文本监听器
     */
    public static ChangeListener<Color> setTextListener(Node node, double width, double fontSize) {
        textListener = null;
        textListener = (observableValue, color, t1) -> node.setStyle(MainController.setLabel(width, "#" + t1.toString().substring(2), fontSize));
        return textListener;
    }
    
    /**
     * 设置边框监听器
     *
     * @param node node
     * @return 边框监听器
     */
    public static ChangeListener<Color> setBorderListener(Node node) {
        borderListener = null;
        borderListener = (observableValue, color, t1) -> node.setStyle("-fx-border-width: 0 0 0 3;-fx-border-color: #" + t1.toString().substring(2) + ";");
        return borderListener;
    }
    
    /**
     * 设置歌单标签文字颜色监听器
     *
     * @param node node
     * @return 歌单标签文字颜色监听器
     */
    public static ChangeListener<Color> setCatsTitlesListener(Node node) {
        catsTitlesListener = null;
        catsTitlesListener = (observableValue, color, t1) -> node.setStyle("-fx-text-fill: #" + t1.toString().substring(2) + ";-fx-font-family: KaiTi;-fx-font-size: 14px;-fx-alignment: center");
        return catsTitlesListener;
    }
    
    /**
     * 设置当前 cat 边框颜色监听器
     *
     * @param node node
     * @return 当前 cat 边框颜色监听器
     */
    public static ChangeListener<Color> setCurrentCatBorderListener(Node node) {
        currentCatBorderListener = null;
        currentCatBorderListener = (observableValue, color, t1) -> node.setStyle("-fx-text-fill: #707070;-fx-font-family: KaiTi;-fx-font-size: 14px;-fx-padding: 5 0 5 0;-fx-cursor: hand;-fx-alignment: center;-fx-border-color: #" + t1.toString().substring(2) + ";");
        return currentCatBorderListener;
    }
    
    /**
     * 移除边框监听器
     */
    public static void removeBorderListener() {
        if (borderListener != null) {
            AnimationUtil.fromColor.removeListener(borderListener);
        }
    }
    
    /**
     * 移除当前 cat 边框颜色监听器
     */
    public static void removeCurrentCatBorderListener() {
        if (currentCatBorderListener != null) {
            AnimationUtil.fromColor.removeListener(currentCatBorderListener);
        }
    }
    
    /**
     * 移除歌单标签文字颜色监听器
     */
    public static void removeCatsTitleListener() {
        if (catsTitlesListener != null) {
            AnimationUtil.fromColor.removeListener(catsTitlesListener);
        }
    }
    
    /**
     * 设置 svgPath 监听器
     *
     * @param svgPath svgPath
     * @return svgPath 监听器
     */
    public static ChangeListener<Color> setSvgPathListener(SVGPath svgPath) {
        svgPathListener = null;
        svgPathListener = (observableValue, color, t1) -> svgPath.setFill(Color.web("#" + t1.toString().substring(2)));
        return svgPathListener;
    }
    
    /**
     * 设置频谱颜色监听器
     *
     * @param rectangle rectangle
     * @return 频谱颜色监听器
     */
    public static ChangeListener<Color> setFsListener(Rectangle rectangle) {
        fsListener = null;
        fsListener = (observableValue, color, t1) -> rectangle.setFill(Color.web("#" + t1.toString().substring(2)));
        return fsListener;
    }
    
    /**
     * 设置背景色监听器
     *
     * @param node node
     * @return 背景色监听器
     */
    public static ChangeListener<Color> setBackGroundColorListener(Node node) {
        backGroundListener = null;
        backGroundListener = (observableValue, color, t1) -> node.setStyle("-fx-background-color: #" + t1.toString().substring(2));
        return backGroundListener;
    }
    
    /**
     * 设置进度条监听器
     *
     * @param max 进度条最大值
     * @return 进度条监听器
     */
    public static ChangeListener<Color> setProgressesListener(double max) {
        
        Rectangle musicRec = MainController.controller.progresses.get(0);
        Rectangle soundRec = MainController.controller.progresses.get(1);
        
        double musicPos = MainController.controller.positions.get(0);
        double soundPos = MainController.controller.positions.get(1);
        
        progressesListener = null;
        progressesListener = (observableValue, color, t1) -> {
            musicRec.setStyle(String.format("-fx-fill: linear-gradient(to right, #" + t1.toString().substring(2) + " %f%%, #c3c3c3 %f%%);", musicPos / max * 100, musicPos / max * 100));
            soundRec.setStyle(String.format("-fx-fill: linear-gradient(to right, #" + t1.toString().substring(2) + " %f%%, #c3c3c3 %f%%);", soundPos * 100, soundPos * 100));
        };
        return progressesListener;
    }
    
    /**
     * Media Player 添加时间点监听
     *
     * @param times 时间节点列表
     */
    public static ChangeListener<Duration> setPlayerListener(ArrayList<String> times) {
        int[] currentTime = {-1, 0};
        playerListener = null;
        playerListener = (observableValue, duration, t1) -> {
            int seconds = (int) t1.toSeconds();
            currentTime[1] = seconds;
            
            // 当前时间更新
            MainController.controller.currentTime.setText(MusicUtil.convertToMinAndSec(seconds));
            
            // 绑定跳转
            if (!MainController.mouse) {
                MainController.controller.sliders.get(0).setValue(t1.toSeconds());
            }
            
            // 设置歌词，每秒只匹配一次
            if (currentTime[0] != currentTime[1]) {
                currentTime[0] = seconds;
                
                // 遍历时间轴匹配指定时间的歌词
                for (int i = 0; i < times.size(); i++) {
                    if (seconds == MusicUtil.getSeconds(times.get(i))) {
                        MainController.currentLabel = i;
                        
                        // 歌词面板设置当前歌词
                        Label currentLyricLabel = (Label) MainController.controller.lyric.getChildren().get(i);
                        LyricController.controller.lyric.setText(currentLyricLabel.getText());
                        LyricController.controller.lyric.setStyle(MainController.setLabel(890, MainController.currentSkin, 38));
                        
                        // 小窗口设置歌词
                        SmallController.controller.lyric.setText(currentLyricLabel.getText());
                        
                        // 当前的 label 设置高亮，其余设置为黑色
                        currentLyricLabel.setStyle(MainController.setLabel(285, MainController.currentSkin, 18));
                        for (int j = 0; j < times.size(); j++) {
                            if (j != i) {
                                MainController.controller.lyric.getChildren().get(j).setStyle(MainController.setLabel(285, "black", 13));
                            }
                        }
                        
                        // 滚轮自动下滑
                        Label label = (Label) MainController.controller.lyric.getChildren().get(0);
                        double labelHeight = label.getPrefHeight();
                        MainController.controller.lyricScrollPane.setVvalue(labelHeight * i / times.size() * labelHeight);
                        break;
                    }
                }
            }
        };
        return playerListener;
    }
    
    /**
     * 移除播放器监听器
     */
    public static void removePlayerListener() {
        if (playerListener != null) {
            MusicUtil.player.currentTimeProperty().removeListener(playerListener);
        }
    }
    
    static {
        audioListener = (v, v1, floats, floats1) -> {
            if (FsController.stage == null || !FsController.stage.isShowing()) {
                return;
            }
            for (int i = 0; i < FsController.recCount; i++) {
                int height = MusicUtil.getHeight(floats[i]);
                Rectangle rectangle = FsController.rectangles.get(i);
                rectangle.setHeight(height);
                rectangle.setY(FsController.controller.rootPane.getPrefHeight() - height);
                rectangle.setFill(Color.web(MainController.currentSkin));
            }
        };
    }
    
    /**
     * 设置频谱监听器
     */
    public static AudioSpectrumListener setAudioListener() {
        return audioListener;
    }
    
}
