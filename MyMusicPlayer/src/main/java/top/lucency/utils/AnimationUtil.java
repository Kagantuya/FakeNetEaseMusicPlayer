package top.lucency.utils;

import javafx.animation.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import top.lucency.controller.MainController;
import top.lucency.controller.MyListener;

/**
 * @author 86181
 */
public class AnimationUtil {
    
    public static SimpleObjectProperty<Color> fromColor = new SimpleObjectProperty<>();
    
    static SimpleObjectProperty<Color> toColor = new SimpleObjectProperty<>();
    
    static KeyValue keyValueOne;
    
    static KeyValue keyValueTwo;
    
    static KeyFrame keyFrame;
    
    static Timeline timeline;
    
    /**
     * 淡入淡出动画
     *
     * @param node  节点
     * @param time  持续时间
     * @param delay 延时
     * @param from  开始透明度
     * @param to    结束透明度
     * @return 淡入淡出动画对象
     */
    public static FadeTransition fade(Node node, double time, double delay, double from, double to) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(time), node);
        fadeTransition.setInterpolator(Interpolator.LINEAR);
        fadeTransition.setDelay(Duration.seconds(delay));
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        fadeTransition.play();
        return fadeTransition;
    }
    
    /**
     * 缩放动画
     *
     * @param node  节点
     * @param time  持续时间
     * @param delay 延时
     * @param fromX X 轴起始大小
     * @param toX   X 轴结束大小
     * @param fromY Y 轴起始大小
     * @param toY   Y 轴结束大小
     */
    public static void scale(Node node, double time, double delay, double fromX, double toX, double fromY, double toY) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(time), node);
        scaleTransition.setInterpolator(Interpolator.LINEAR);
        scaleTransition.setDelay(Duration.seconds(delay));
        scaleTransition.setFromX(fromX);
        scaleTransition.setToX(toX);
        scaleTransition.setFromY(fromY);
        scaleTransition.setToY(toY);
        scaleTransition.play();
    }
    
    /**
     * 旋转动画
     *
     * @param node  节点
     * @param time  持续时间
     * @param delay 延时
     * @param from  开始角度
     * @param to    结束角度
     */
    public static void rotate(Node node, double time, double delay, double from, double to) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(time), node);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setDelay(Duration.seconds(delay));
        rotateTransition.setAutoReverse(true);
        rotateTransition.setFromAngle(from);
        rotateTransition.setToAngle(to);
        rotateTransition.setCycleCount(2);
        rotateTransition.play();
    }
    
    /**
     * 绕点旋转
     *
     * @param node  节点
     * @param from  开始角度
     * @param to    结束角度
     * @param x     旋转中心 x
     * @param y     旋转中心 y
     * @param time  持续时间
     * @param delay 延时
     */
    public static void rotateByPosition(Node node, double from, double to, double x, double y, double time, double delay) {
        Rotate rotate = new Rotate(from, x, y);
        node.getTransforms().add(rotate);
        KeyValue keyValue = new KeyValue(rotate.angleProperty(), to);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(time), keyValue);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(keyFrame);
        timeline.setDelay(Duration.seconds(delay));
        timeline.play();
    }
    
    /**
     * 位移动画
     *
     * @param node  节点
     * @param time  持续时间
     * @param delay 延时
     * @param fromX X 轴起始位置
     * @param toX   X 轴结束位置
     * @param fromY Y 轴起始位置
     * @param toY   Y 轴结束位置
     */
    public static TranslateTransition translate(Node node, double time, double delay, double fromX, double toX, double fromY, double toY) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(time), node);
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.setDelay(Duration.seconds(delay));
        translateTransition.setFromX(fromX);
        translateTransition.setToX(toX);
        translateTransition.setFromY(fromY);
        translateTransition.setToY(toY);
        translateTransition.play();
        return translateTransition;
    }
    
    /**
     * 背景色渐变
     *
     * @param node toolbar
     * @param from 开始颜色
     * @param to   结束颜色
     */
    public static void changeBackgroundColorByStyle(Node node, Color from, Color to) {
        initColors(from, to).addListener(MyListener.setBackGroundColorListener(node));
    }
    
    /**
     * 文字颜色渐变
     *
     * @param node     节点
     * @param from     开始颜色
     * @param to       结束颜色
     * @param width    文字父级容器宽度
     * @param fontSize 字体大小
     */
    public static void changeTextColor(Node node, Color from, Color to, double width, double fontSize) {
        initColors(from, to).addListener(MyListener.setTextListener(node, width, fontSize));
    }
    
    /**
     * svg 颜色渐变
     *
     * @param svgPath svg 元素
     * @param from    开始颜色
     * @param to      结束颜色
     */
    public static void changeSvgPathColor(SVGPath svgPath, Color from, Color to) {
        initColors(from, to).addListener(MyListener.setSvgPathListener(svgPath));
    }
    
    /**
     * label 边框颜色渐变
     * @param node label
     * @param from 开始颜色
     * @param to 结束颜色
     */
    public static void changeBorderColor(Node node, Color from, Color to) {
        initColors(from, to).addListener(MyListener.setBorderListener(node));
    }
    
    /**
     * 当前 cat 边框颜色渐变
     * @param node label
     * @param from 开始颜色
     * @param to 结束颜色
     */
    public static void changeCurrentCatBorderColor(Node node, Color from, Color to) {
        initColors(from, to).addListener(MyListener.setCurrentCatBorderListener(node));
    }
    
    /**
     * 歌单标签文字颜色渐变
     * @param node label
     * @param from 开始颜色
     * @param to 结束颜色
     */
    public static void changeCatsTitlesColor(Node node, Color from, Color to) {
        initColors(from, to).addListener(MyListener.setCatsTitlesListener(node));
    }
    
    /**
     * 可视化频谱颜色渐变
     *
     * @param rectangle svg 元素
     * @param from      开始颜色
     * @param to        结束颜色
     */
    public static void changeFsColor(Rectangle rectangle, Color from, Color to) {
        initColors(from, to).addListener(MyListener.setFsListener(rectangle));
    }
    
    /**
     * 轨道颜色渐变
     *
     * @param from 开始颜色
     * @param to   结束颜色
     */
    public static void changeProgressesColor(Color from, Color to) {
        Slider musicSlider = MainController.controller.sliders.get(0);
        Slider soundSlider = MainController.controller.sliders.get(1);
        
        Rectangle musicRec = MainController.controller.progresses.get(0);
        Rectangle soundRec = MainController.controller.progresses.get(1);
        
        // 重新为滑块设置监听更新颜色
        MyListener.addSliderListener(MainController.controller.musicPane, musicSlider, musicRec, "#" + to.toString().substring(2));
        MyListener.addSliderListener(MainController.controller.soundPane, soundSlider, soundRec, "#" + to.toString().substring(2));
        initColors(from, to).addListener(MyListener.setProgressesListener(musicSlider.getMax()));
    }
    
    /**
     * 从网上白嫖的，不清楚原理
     *
     * @param from 不清楚
     * @param to   不清楚
     * @return 不清楚
     */
    private static SimpleObjectProperty<Color> initColors(Color from, Color to) {
        fromColor.set(from);
        toColor.set(to);
        
        timeline = new Timeline();
        keyValueOne = new KeyValue(toColor, to);
        keyValueTwo = new KeyValue(fromColor, to);
        keyFrame = new KeyFrame(Duration.seconds(0.5), keyValueOne, keyValueTwo);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        timeline.setOnFinished(actionEvent -> timeline = null);
        
        return fromColor;
    }
    
}
