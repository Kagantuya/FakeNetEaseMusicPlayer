package top.lucency.controller;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.Duration;
import top.lucency.entities.NetEaseMusic;
import top.lucency.entities.NetEasePlayList;
import top.lucency.stagelauncher.FsStage;
import top.lucency.stagelauncher.LyricStage;
import top.lucency.stagelauncher.NoticeStage;
import top.lucency.stagelauncher.SmallStage;
import top.lucency.entities.LocalMusic;
import top.lucency.entities.Music;
import top.lucency.utils.AnimationUtil;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import top.lucency.utils.ConnectUtil;
import top.lucency.utils.MusicUtil;
import top.lucency.utils.StageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 86181
 */
public class MainController {
    
    /**
     * 通过初始化传入自身对象，从而可以访问到 fxml 中的控件
     */
    public static MainController controller;
    
    /**
     * 窗口
     */
    public static Stage stage;
    
    /**
     * 滑动条左侧颜色条
     */
    public ArrayList<Rectangle> progresses = new ArrayList<>();
    
    /**
     * 滑块
     */
    public ArrayList<Slider> sliders = new ArrayList<>();
    
    /**
     * 滑块当前位置
     */
    public ArrayList<Double> positions = new ArrayList<>(Arrays.asList(0.0, 0.0));
    
    /**
     * 主界面暂停播放 svg
     */
    public static final String PAUSE_SVG = "M-20.8,465.3c8.7,0,15.8,7.1,15.8,15.8S-12.1,497-20.8,497s-15.8-7.1-15.8-15.8S-29.6,465.3-20.8,465.3z M-22.8,475.2h-4 v11.9h4V475.2z M-14.9,475.2h-4v11.9h4V475.2z";
    
    /**
     * 主界面播放 svg
     */
    public static final String PLAY_SVG = "M-44.7,455.9c-8.7,0-15.8,7.1-15.8,15.8s7.1,15.8,15.8,15.8s15.8-7.1,15.8-15.8 C-28.8,463-35.9,455.9-44.7,455.9z M-37.7,472.5l-9.1,6.8c-0.2,0.1-0.3,0.2-0.5,0.2c-0.5,0-0.9-0.4-0.9-0.9l0,0v-13.7 c0-0.5,0.4-0.9,0.9-0.9c0.2,0,0.4,0.1,0.5,0.2l9.1,6.8c0.4,0.3,0.5,0.9,0.2,1.3C-37.6,472.4-37.7,472.4-37.7,472.5z";
    
    /**
     * 主界面静音 svg
     */
    public static final String NO_SOUND = "M-32,472.3C-32,472.3-32,472.3-32,472.3l-2.3,0c-0.4,0-0.8,0.3-0.8,0.8v5.4c0,0.4,0.3,0.8,0.8,0.8h2.3c0,0,0,0,0,0v0l5.6,3.6c0.1,0.1,0.3,0.2,0.5,0.2c0.4,0,0.8-0.3,0.8-0.8c0,0,0-0.1,0-0.1v-12.8c0,0,0-0.1,0-0.1c0-0.4-0.3-0.8-0.8-0.8c-0.2,0-0.3,0.1-0.5,0.2L-32,472.3z";
    
    /**
     * 主界面恢复声音 svg
     */
    public static final String SOUND = "M-32,472.3C-32,472.3-32,472.3-32,472.3l-2.3,0c-0.4,0-0.8,0.3-0.8,0.8v5.4c0,0.4,0.3,0.8,0.8,0.8h2.3c0,0,0,0,0,0v0l5.6,3.6c0.1,0.1,0.3,0.2,0.5,0.2c0.4,0,0.8-0.3,0.8-0.8c0,0,0-0.1,0-0.1v-12.8c0,0,0-0.1,0-0.1c0-0.4-0.3-0.8-0.8-0.8c-0.2,0-0.3,0.1-0.5,0.2L-32,472.3zM-21.4,484.4L-21.4,484.4c2.4-2.1,4-5.2,4-8.7c0-3.5-1.5-6.6-4-8.7l0,0c-0.1-0.1-0.2-0.1-0.4-0.1c-0.3,0-0.6,0.3-0.6,0.6c0,0.1,0,0.3,0.1,0.4l0,0c0,0,0,0,0,0c0,0,0,0,0.1,0.1c2.2,1.9,3.5,4.7,3.5,7.8s-1.4,5.9-3.5,7.8c0,0-0.1,0-0.1,0.1c0,0,0,0,0,0l0,0c-0.1,0.1-0.1,0.2-0.1,0.4c0,0.3,0.3,0.6,0.6,0.6C-21.7,484.5-21.5,484.5-21.4,484.4zM-22.3,480L-22.3,480c1.1-1.1,1.8-2.6,1.8-4.3c0-1.7-0.7-3.2-1.8-4.3l0,0c-0.1-0.1-0.2-0.1-0.3-0.1c-0.3,0-0.6,0.3-0.6,0.6c0,0.1,0,0.2,0.1,0.3l0,0c0,0,0,0,0,0c0,0,0,0,0,0c0.9,0.9,1.4,2.1,1.4,3.4s-0.5,2.5-1.4,3.4c0,0,0,0,0,0c0,0,0,0,0,0l0,0c-0.1,0.1-0.1,0.2-0.1,0.3c0,0.3,0.3,0.6,0.6,0.6C-22.5,480.1-22.4,480.1-22.3,480z";
    
    /**
     * 当前肤色
     */
    public static String currentSkin = "#00BFFF";
    
    /**
     * 当前歌词行数对应的 label
     */
    public static int currentLabel = 0;
    
    /**
     * 当前音量
     */
    public static double currentSound = 0;
    
    /**
     * 跳转时间点，从 b 站一位 up 学到的，暂时没搞清楚为何如此
     */
    static public boolean mouse = false;
    
    /**
     * 歌曲详情页歌曲封面旋转动画对象
     */
    public static RotateTransition musicBigPicTT;
    
    /**
     * 歌曲封面
     */
    Image image;
    
    /**
     * 当前本地音乐播放的下标
     */
    int currentLocalMusicIndex = 0;
    
    /**
     * 当前歌单播放的下标
     */
    int currentNetEaseMusicIndex = 0;
    
    /**
     * 当前音乐播放类型
     */
    public static String currentMusicType;
    
    /**
     * 左侧不同窗口面板入口按钮选中样式
     */
    private static final String ROOT_PANE_BUTTON_NONE_FOCUS_OPTION_STYLE = "-fx-border-width: 0 0 0 3; -fx-border-color: rgba(0, 0, 0, 0);";
    
    /**
     * cats 标题 labels
     */
    private ArrayList<Label> titles = new ArrayList<>();
    
    /**
     * 当前选择的 cat
     */
    private Label currentCatOption;
    
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Button quit;
    @FXML
    public ToolBar toolbar;
    @FXML
    public Pane shadowPane;
    @FXML
    public Button logo;
    @FXML
    public Button small;
    @FXML
    public StackPane musicPane;
    @FXML
    public StackPane soundPane;
    @FXML
    public ColorPicker skin;
    @FXML
    public SVGPath previous;
    @FXML
    public SVGPath playOrPauseSvg;
    @FXML
    public SVGPath next;
    @FXML
    public AnchorPane listPane;
    @FXML
    public VBox list;
    @FXML
    public Label name;
    @FXML
    public Label author;
    @FXML
    public ImageView musicPic;
    @FXML
    public Text currentTime;
    @FXML
    public Text totalTime;
    @FXML
    public SVGPath sound;
    @FXML
    public Label title;
    @FXML
    public Button musicInfo;
    @FXML
    public HBox musicInfoPane;
    @FXML
    public AnchorPane musicDetailPane;
    @FXML
    public Button closeLyric;
    @FXML
    public ImageView musicBigPic;
    @FXML
    public AnchorPane needle;
    @FXML
    public VBox lyric;
    @FXML
    public AnchorPane lyricPane;
    @FXML
    public Label nameBig;
    @FXML
    public Label authorBig;
    @FXML
    public ScrollPane lyricScrollPane;
    @FXML
    public Button lyricBig;
    @FXML
    public SVGPath lyricBigSvg;
    @FXML
    public Button frequencySpectrum;
    @FXML
    public Label localMusicNumber;
    @FXML
    public Button updateLocalMusic;
    @FXML
    public TableColumn<LocalMusic, String> localId;
    @FXML
    public TableColumn<LocalMusic, String> loaclName;
    @FXML
    public TableColumn<LocalMusic, String> localAuthor;
    @FXML
    public TableColumn<LocalMusic, String> localAlbum;
    @FXML
    public TableColumn<LocalMusic, String> loaclSize;
    @FXML
    public TableColumn<LocalMusic, String> localTime;
    @FXML
    public TableView<LocalMusic> localMusicInfo;
    @FXML
    public AnchorPane localMusicRootPane;
    @FXML
    public Label localMusicLabel;
    @FXML
    public Label findMusicLabel;
    @FXML
    public AnchorPane findMusicRootPane;
    @FXML
    public VBox catsBox;
    @FXML
    public AnchorPane catsOptionsPane;
    @FXML
    public Label allCats;
    @FXML
    public Label currentCat;
    @FXML
    public AnchorPane catsRootPane;
    @FXML
    public VBox playListsBox;
    @FXML
    public AnchorPane playListsRootPane;
    @FXML
    public Label playListTitle;
    @FXML
    public ImageView playListImage;
    @FXML
    public AnchorPane playListsDetailsRootPane;
    @FXML
    public Label updatePlayList;
    @FXML
    public Label playListInfo;
    @FXML
    public TableColumn<NetEaseMusic, String> netEaseId;
    @FXML
    public TableColumn<NetEaseMusic, String> netEaseTitle;
    @FXML
    public TableColumn<NetEaseMusic, String> netEaseAuthor;
    @FXML
    public TableColumn<NetEaseMusic, String> netEaseAlbum;
    @FXML
    public TableColumn<NetEaseMusic, String> netEaseTime;
    @FXML
    public TableView<NetEaseMusic> netEaseSongs;
    
    @FXML
    public void previous() {
        new Thread(new LoadMusicThread(currentMusicType, "previous")).start();
    }
    
    /**
     * 更新当前标签下的歌单
     */
    @FXML
    public void updatePlayList() {
        new Thread(new UpdatePlayList(currentCat.getText(), "manual")).start();
    }
    
    /**
     * 打开或关闭歌单类型界面
     */
    @FXML
    public void toggleCatsPane() {
        if (catsRootPane.getScaleX() == 0) {
            openRootPane(catsRootPane);
        } else {
            closeRootPane(catsRootPane);
        }
    }
    
    /**
     * 左侧 label 点击样式改变
     *
     * @param label label
     */
    public void setLabelStyle(Label label) {
        label.setStyle("-fx-border-width: 0 0 0 3;-fx-border-color: " + currentSkin + ";-fx-background-color: #eff0f1;");
    }
    
    /**
     * 关闭根控件
     *
     * @param rootPane 根控件
     */
    private void closeRootPane(AnchorPane rootPane) {
        rootPane.setScaleX(0);
        rootPane.setScaleY(0);
    }
    
    /**
     * 打开根控件
     *
     * @param rootPane 根控件
     */
    private void openRootPane(AnchorPane rootPane) {
        rootPane.setScaleX(1);
        rootPane.setScaleY(1);
    }
    
    /**
     * 打开发现音乐面板
     */
    @FXML
    public void openFindMusicRootPane() {
        localMusicLabel.setStyle(ROOT_PANE_BUTTON_NONE_FOCUS_OPTION_STYLE);
        setLabelStyle(findMusicLabel);
        closeRootPane(localMusicRootPane);
        closeRootPane(playListsDetailsRootPane);
        openRootPane(findMusicRootPane);
    }
    
    /**
     * 打开本地音乐面板
     */
    @FXML
    public void openLocalMusicRootPane() {
        findMusicLabel.setStyle(ROOT_PANE_BUTTON_NONE_FOCUS_OPTION_STYLE);
        setLabelStyle(localMusicLabel);
        closeRootPane(findMusicRootPane);
        closeRootPane(playListsDetailsRootPane);
        openRootPane(localMusicRootPane);
        new Thread(new UpdateTableView("auto")).start();
    }
    
    /**
     * 更新本地音乐目录
     */
    @FXML
    public void updateLocalMusic() {
        new Thread(new UpdateTableView("manual")).start();
    }
    
    /**
     * 更新本地 table view
     *
     * @param data 数据组
     */
    public static void updateLocalSongs(ObservableList<LocalMusic> data) {
        Platform.runLater(() -> {
            MainController.controller.localMusicInfo.setItems(null);
            MainController.controller.localId.setCellValueFactory(new PropertyValueFactory<>("id"));
            MainController.controller.loaclName.setCellValueFactory(new PropertyValueFactory<>("name"));
            MainController.controller.localAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
            MainController.controller.localAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
            MainController.controller.loaclSize.setCellValueFactory(new PropertyValueFactory<>("size"));
            MainController.controller.localTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            MainController.controller.localMusicInfo.setItems(data);
        });
    }
    
    /**
     * 更新网易云歌单音乐 table view
     *
     * @param data 数据组
     */
    public static void updatePlayListSongs(ObservableList<NetEaseMusic> data) {
        Platform.runLater(() -> {
            MainController.controller.netEaseId.setCellValueFactory(new PropertyValueFactory<>("id"));
            MainController.controller.netEaseTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            MainController.controller.netEaseAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
            MainController.controller.netEaseAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
            MainController.controller.netEaseTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            MainController.controller.netEaseSongs.setItems(data);
        });
    }
    
    /**
     * 打开或关闭频谱面板
     */
    @FXML
    public void toggleFrequencySpectrumStage() {
        if (FsController.stage == null) {
            try {
                new FsStage().start(new Stage());
                StageUtil.image = null;
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!FsController.stage.isShowing()) {
            FsController.stage.show();
        } else {
            FsController.stage.hide();
        }
    }
    
    /**
     * 打开或关闭歌词面板
     */
    @FXML
    public void toggleLyricPane() {
        // 当前面板已打开
        if (LyricController.stage.isShowing()) {
            LyricController.stage.close();
            lyricBigSvg.setFill(Color.web("#676767"));
        } else {
            // 当前面板已关闭
            LyricController.controller.rootPane.setOpacity(1);
            LyricController.stage.show();
            lyricBigSvg.setFill(Color.web(currentSkin));
        }
    }
    
    /**
     * 声音按钮静音与恢复切换
     */
    @FXML
    public void toggleSound() {
        // 当前不为静音
        if (sound.getContent().equals(SOUND)) {
            currentSound = sliders.get(1).getValue();
            sliders.get(1).setValue(0);
            sound.setContent(NO_SOUND);
        } else {
            // 当前已静音
            sliders.get(1).setValue(currentSound);
            sound.setContent(SOUND);
        }
    }
    
    /**
     * 播放或暂停
     */
    @FXML
    public void togglePlay() {
        // 当前已暂停则播放
        if (playOrPauseSvg.getContent().equals(PAUSE_SVG)) {
            MusicUtil.pause();
            playOrPauseSvg.setContent(PLAY_SVG);
            LyricController.controller.playOrPauseSvg.setContent(LyricController.PLAY_SVG);
            SmallController.controller.playOrPauseSvg.setContent(SmallController.PLAY_SVG);
            // 小窗更新信息
            if (SmallController.controller.buttons.getOpacity() == 0) {
                AnimationUtil.fade(SmallController.controller.lyric, 0.1, 0, 1, 0);
                AnimationUtil.fade(SmallController.controller.musicInfo, 0.1, 0.1, 0, 1);
            }
        } else {
            // 当前正在播放且 Media Player 已被初始化则暂停
            if (MusicUtil.play()) {
                playOrPauseSvg.setContent(PAUSE_SVG);
                LyricController.controller.playOrPauseSvg.setContent(LyricController.PAUSE_SVG);
                SmallController.controller.playOrPauseSvg.setContent(SmallController.PAUSE_SVG);
                // 小窗更新信息
                if (SmallController.controller.buttons.getOpacity() == 0) {
                    AnimationUtil.fade(SmallController.controller.musicInfo, 0.1, 0, 1, 0);
                    AnimationUtil.fade(SmallController.controller.lyric, 0.1, 0.1, 0, 1);
                }
            }
        }
    }
    
    /**
     * 下一首歌
     */
    @FXML
    public void next() {
        new Thread(new LoadMusicThread(currentMusicType, "next")).start();
    }
    
    /**
     * 双击播放本地歌曲
     */
    private void bindLocalMusicClicked() {
        localMusicInfo.setRowFactory(localMusicTableView -> {
            TableRow<LocalMusic> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    currentMusicType = "local";
                    currentLocalMusicIndex = row.getIndex() - 1;
                    next();
                }
            });
            return row;
        });
    }
    
    /**
     * 双击播放歌单歌曲
     */
    private void bindNetEaseMusicClicked() {
        netEaseSongs.setRowFactory(netEaseMusicTableView -> {
            TableRow<NetEaseMusic> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    currentMusicType = "netEase";
                    currentNetEaseMusicIndex = row.getIndex() - 1;
                    next();
                }
            });
            return row;
        });
    }
    
    /**
     * 下一首网易云歌
     */
    public void nextNetEase(String indexType) {
        if ("next".equals(indexType)) {
            currentNetEaseMusicIndex++;
            if (currentNetEaseMusicIndex == 10) {
                currentNetEaseMusicIndex = 0;
            }
        } else {
            currentNetEaseMusicIndex--;
            if (currentNetEaseMusicIndex == -1) {
                currentNetEaseMusicIndex = 9;
            }
        }
        Music music = MusicUtil.initNetEaseMusic(MusicUtil.playListNameAndIds.get(currentNetEaseMusicIndex));
        updateUi(music);
    }
    
    /**
     * 下一首本地歌曲
     */
    public void nextLocal(String indexType) {
        if ("next".equals(indexType)) {
            currentLocalMusicIndex++;
            if (currentLocalMusicIndex == MusicUtil.localMusic.size()) {
                currentLocalMusicIndex = 0;
            }
        } else {
            currentLocalMusicIndex--;
            if (currentLocalMusicIndex == -1) {
                currentLocalMusicIndex = MusicUtil.localMusic.size() - 1;
            }
        }
        Music music = MusicUtil.initLocalMusic(MusicUtil.ROOT_PATH + MusicUtil.localMusic.get(currentLocalMusicIndex));
        updateUi(music);
    }
    
    /**
     * 更新 UI
     *
     * @param music 实体
     */
    private void updateUi(Music music) {
        String musicName;
        String musicAuthor;
        ArrayList<Map<String, String>> lyric;
        
        musicName = music.getName();
        musicAuthor = music.getAuthor();
        lyric = music.getLyric();
        
        Platform.runLater(() -> {
            // 更新文字信息
            name.setText(musicName);
            author.setText(musicAuthor);
            nameBig.setText(musicName);
            authorBig.setText(musicAuthor);
            LyricController.controller.lyric.setText("");
            SmallController.controller.lyric.setText("");
            SmallController.controller.musicInfo.setText(musicName + "\n" + musicAuthor);
            SmallController.controller.musicInfo.setOpacity(0);
            SmallController.controller.musicInfoHide.setText(musicName + " —— " + musicAuthor);
            
            // 修改为暂停图标
            playOrPauseSvg.setContent(PAUSE_SVG);
            LyricController.controller.playOrPauseSvg.setContent(LyricController.PAUSE_SVG);
            SmallController.controller.playOrPauseSvg.setContent(SmallController.PAUSE_SVG);
            
            // 加载歌词
            if (lyric == null) {
                ArrayList<Map<String, String>> tmp = new ArrayList<>();
                Map<String, String> map = new HashMap<>(1);
                map.put("00:00", "非常抱歉，暂未查询到此歌歌词");
                tmp.add(map);
                addLyric(tmp);
            } else {
                addLyric(lyric);
            }
            
            // 设置图片
            if (music.getPicUrl() == null) {
                image = null;
            } else {
                image = new Image(music.getPicUrl(), 200, 200, false, false);
            }
            MainController.controller.musicBigPic.setImage(image);
            MainController.controller.musicPic.setImage(image);
            SmallController.controller.musicPic.setImage(image);
            image = null;
        });
    }
    
    /**
     * 淡出程序
     */
    @FXML
    public void quit() {
        AnimationUtil.fade(rootPane, 1, 0, 1, 0).setOnFinished(actionEvent -> System.exit(0));
    }
    
    /**
     * 最小化窗口
     */
    @FXML
    public void min() {
        stage.setIconified(true);
    }
    
    /**
     * 小窗模式
     */
    @FXML
    public void small() {
        try {
            stage.close();
            SmallController.controller.rootPane.setOpacity(1);
            SmallController.stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 关闭主界面的歌曲详情页
     */
    @FXML
    private void closeMusicDetailPane() {
        AnimationUtil.fade(musicDetailPane, 0.3, 0, 1, 0);
        AnimationUtil.fade(name, 0.5, 0.2, 0, 1);
        AnimationUtil.fade(author, 0.5, 0.2, 0, 1);
        AnimationUtil.fade(musicPic, 0.5, 0.2, 0, 1);
        AnimationUtil.fade(musicInfo, 0.5, 0.2, 0, 1);
        AnimationUtil.scale(musicDetailPane, 0.3, 0, 1, 0, 1, 0);
        AnimationUtil.translate(musicDetailPane, 0.3, 0, 0, -390, 0, 180);
    }
    
    /**
     * 鼠标移入主面板左下角歌曲信息区
     */
    @FXML
    public void mouseEntered() {
        AnimationUtil.fade(musicInfo, 0.1, 0, 0, 1);
    }
    
    /**
     * 鼠标移开主面板左下角歌曲信息区
     */
    public void mouseExited() {
        AnimationUtil.fade(musicInfo, 0.1, 0, 1, 0);
    }
    
    /**
     * 打开主界面歌曲详情页
     */
    @FXML
    public void openMusicDetailPane() {
        AnimationUtil.fade(musicDetailPane, 0.3, 0, 0, 0.9);
        AnimationUtil.fade(name, 0.3, 0, 1, 0);
        AnimationUtil.fade(author, 0.3, 0, 1, 0);
        AnimationUtil.fade(musicPic, 0.3, 0, 1, 0);
        AnimationUtil.fade(musicInfo, 0.3, 0, 1, 0);
        AnimationUtil.scale(musicDetailPane, 0.3, 0, 0, 1, 0, 1);
        AnimationUtil.translate(musicDetailPane, 0.3, 0, -390, 0, 180, 0);
    }
    
    /**
     * 渐变修改皮肤颜色
     */
    @FXML
    public void changeSkin() {
        String fromColor = "#" + toolbar.getStyle().split("#")[1].split(";")[0];
        String toColor = "#" + skin.getValue().toString().substring(2);
        
        Color from = Color.web(fromColor);
        Color to = Color.web(toColor);
        
        MyListener.removeColorListener(MyListener.backGroundListener);
        AnimationUtil.changeBackgroundColorByStyle(toolbar, from, to);
        AnimationUtil.changeBackgroundColorByStyle(toolbar, from, to);
        
        MyListener.removeColorListener(MyListener.textListener);
        if (lyric.getChildren().size() > 0) {
            AnimationUtil.changeTextColor(lyric.getChildren().get(currentLabel), from, to, 285, 18);
        }
        AnimationUtil.changeTextColor(LyricController.controller.lyric, from, to, 890, 38);
        
        MyListener.removeColorListener(MyListener.svgPathListener);
        AnimationUtil.changeSvgPathColor(previous, from, to);
        AnimationUtil.changeSvgPathColor(playOrPauseSvg, from, to);
        AnimationUtil.changeSvgPathColor(next, from, to);
        AnimationUtil.changeSvgPathColor(lyricBigSvg, from, to);
        
        MyListener.removeSliderListener();
        MyListener.removeColorListener(MyListener.progressesListener);
        AnimationUtil.changeProgressesColor(from, to);
        
        MyListener.removeColorListener(MyListener.fsListener);
        for (Rectangle rectangle : FsController.rectangles) {
            AnimationUtil.changeFsColor(rectangle, from, to);
        }
        
        MyListener.removeBorderListener();
        if (!localMusicLabel.getStyle().equals(ROOT_PANE_BUTTON_NONE_FOCUS_OPTION_STYLE)) {
            AnimationUtil.changeBorderColor(localMusicLabel, from, to);
        }
        if (!findMusicLabel.getStyle().equals(ROOT_PANE_BUTTON_NONE_FOCUS_OPTION_STYLE)) {
            AnimationUtil.changeBorderColor(findMusicLabel, from, to);
        }
        
        MyListener.removeCatsTitleListener();
        for (Label label : titles) {
            AnimationUtil.changeCatsTitlesColor(label, from, to);
        }
        
        if (currentCatOption != null) {
            MyListener.removeCurrentCatBorderListener();
            AnimationUtil.changeCurrentCatBorderColor(currentCatOption, from, to);
        }
        
        // 小窗口也同时改色，保存当前颜色
        SmallController.controller.toolbar.setStyle("-fx-background-color:" + toColor);
        SmallController.controller.musicInfoHide.setStyle("-fx-background-color:" + toColor);
        currentSkin = toColor;
    }
    
    /**
     * 初始化界面
     *
     * @param stage      界面
     * @param controller 自身
     */
    public void init(Stage stage, MainController controller) {
        MainController.controller = controller;
        MainController.stage = stage;
        fadeIn();
        
        // 所有 arrayList 中均为播放进度条第一，音量调第二
        initStackPane(musicPane);
        initStackPane(soundPane);
        rotateMusicBigPane();
        initSound();
        initCats();
        
        bindLocalMusicClicked();
        bindNetEaseMusicClicked();
        bindProperty();
        addDragEvent();
        
        // 预加载窗口
        try {
            new LyricStage().start(new Stage());
            new SmallStage().start(new Stage());
            new NoticeStage().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LyricController.stage.close();
        SmallController.stage.close();
        NoticeController.stage.close();

//        new Thread(new AutoUpdate("auto")).start();
        new Thread(new UpdateTableView("auto")).start();
        new Thread(new UpdatePlayList("全部", "auto")).start();
    }
    
    /**
     * 初始化歌单类型
     */
    private void initCats() {
        String style = "-fx-text-fill: #707070;-fx-font-family: KaiTi;-fx-font-size: 14px;-fx-padding: 5 0 5 0;-fx-cursor: hand;-fx-alignment: center;";
        setCatsMouseEvents(allCats, style);
        
        Map<String, ArrayList<String>> cats = ConnectUtil.getNetEaseCats();
        for (String key : cats.keySet()) {
            if (key.length() != 0) {
                GridPane gridPane = new GridPane();
                gridPane.setPrefWidth(390);
                gridPane.setStyle("-fx-padding: 5 0 5 0;");
                
                Label title = new Label(key);
                title.setPrefWidth(65);
                title.setStyle("-fx-text-fill: " + currentSkin + ";-fx-font-family: KaiTi;-fx-font-size: 14px;-fx-alignment: center");
                titles.add(title);
                gridPane.add(title, 0, 0);
                
                ArrayList<String> catsContents = cats.get(key);
                for (int i = 0; i < catsContents.size(); i++) {
                    Label catContent = new Label(catsContents.get(i));
                    catContent.setPrefWidth(65);
                    catContent.setStyle(style + "-fx-border-color:  #F7F7F7;");
                    setCatsMouseEvents(catContent, style);
                    gridPane.add(catContent, i % 5 + 1, i / 5);
                }
                catsBox.getChildren().add(gridPane);
            }
        }
    }
    
    /**
     * 设置种类 label 鼠标事件
     *
     * @param cat   label
     * @param style 样式
     */
    private void setCatsMouseEvents(Label cat, String style) {
        String defaultStyle = style + "-fx-border-color:  #F7F7F7;";
        String enteredStyle = style + "-fx-background-color: #F7F7F780;-fx-border-color:  #F7F7F7;";
        String exitedStyle = style + "-fx-background-color: rgba(0, 0, 0, 0);-fx-border-color:  #F7F7F7;";
        String focusedStyle = style + "-fx-border-color: " + currentSkin + ";";
        
        cat.setOnMouseEntered(mouseEvent -> {
            if (!cat.getStyle().equals(focusedStyle)) {
                cat.setStyle(enteredStyle);
            }
        });
        cat.setOnMouseExited(mouseEvent -> {
            if (!cat.getStyle().equals(focusedStyle)) {
                cat.setStyle(exitedStyle);
            }
        });
        cat.setOnMouseClicked(mouseEvent -> {
            // 其余 label 全部设为初始样式
            allCats.setStyle(defaultStyle);
            ObservableList<Node> nodes = catsBox.getChildren();
            // 遍历每个 grid Pane
            for (int i = 0; i < nodes.size(); i++) {
                if (i != 0) {
                    GridPane gridPane = (GridPane) nodes.get(i);
                    ObservableList<Node> labels = gridPane.getChildren();
                    // 遍历每个 grid Pane 的 label
                    for (int j = 0; j < labels.size(); j++) {
                        if (j != 0) {
                            Label label = (Label) labels.get(j);
                            label.setStyle(defaultStyle);
                        }
                    }
                }
            }
            currentCatOption = cat;
            cat.setStyle(focusedStyle);
            new Thread(new UpdatePlayList(cat.getText(), "manual")).start();
            closeRootPane(catsRootPane);
            currentCat.setText(cat.getText());
        });
    }
    
    /**
     * 更新歌单
     *
     * @param cat 标签
     */
    public static void updatePlayList(String cat) {
        ArrayList<NetEasePlayList> playLists = ConnectUtil.getNetEasePlayList(cat);
        // 每行歌单个数
        int rowCounts = 4;
        
        // 遍历所有 hBox ，由于每个 hBox 拥有 rowCounts 个歌单，因此设置步长为 rowCounts
        for (int i = 0; i < playLists.size(); i += rowCounts) {
            // 第一行不是 hBox ，因此 get 中需要 +1
            HBox hBox = (HBox) MainController.controller.playListsBox.getChildren().get(i / rowCounts + 1);
            // 遍历 hBox 中的所有 vBox
            for (int j = 0; j < rowCounts; j++) {
                // i 只是 hBox 的下标，要遍历其中的 vBox 还需要 +j
                int index = i + j;
                // 避免歌单没有填满一行的越界异常
                if (index == playLists.size()) {
                    return;
                }
                
                String title = playLists.get(index).getPlayListTitle();
                String picUrl = playLists.get(index).getPlayListPicUrl();
                String listUrl = playLists.get(index).getPlayListUrl();
                String info = playLists.get(index).getPlayListInfo();
                
                // 打开歌单详情页
                VBox vBox = (VBox) hBox.getChildren().get(j);
                ImageView imageView = (ImageView) vBox.getChildren().get(0);
                Label label = (Label) vBox.getChildren().get(1);
                imageView.setImage(new Image(picUrl, imageView.getFitWidth(), imageView.getFitHeight(), true, false));
                vBox.setOnMouseClicked(mouseEvent -> {
                    MainController.controller.netEaseSongs.setItems(null);
                    ImageView playListImageView = MainController.controller.playListImage;
                    playListImageView.setImage(new Image(picUrl, playListImageView.getFitWidth(), playListImageView.getFitHeight(), true, false));
                    MainController.controller.playListInfo.setText(info);
                    MainController.controller.playListTitle.setText(title);
                    new Thread(new GetPlayListSongs(listUrl)).start();
                    MainController.controller.closeRootPane(MainController.controller.findMusicRootPane);
                    MainController.controller.openRootPane(MainController.controller.playListsDetailsRootPane);
                });
                Platform.runLater(() -> label.setText(title));
            }
        }
    }
    
    /**
     * 歌曲详情页歌词区添加歌词
     *
     * @param lyrics 时间轴和歌词信息
     */
    public void addLyric(ArrayList<Map<String, String>> lyrics) {
        Platform.runLater(() -> lyric.getChildren().clear());
        ArrayList<String> times = new ArrayList<>();
        
        // 全部歌词加载到歌词列表中
        for (int i = 0; i < lyrics.size(); i++) {
            for (String key : lyrics.get(i).keySet()) {
                // 分离时间节点
                times.add(key);
                Label label = new Label(lyrics.get(i).get(key));
                // 第一行歌词高亮，逐行添加歌词
                if (i == 0) {
                    label.setStyle(setLabel(285, currentSkin, 18));
                } else {
                    label.setStyle(setLabel(285, "black", 13));
                }
                Platform.runLater(() -> lyric.getChildren().add(label));
            }
        }
        
        MyListener.removePlayerListener();
        MusicUtil.player.currentTimeProperty().addListener(MyListener.setPlayerListener(times));
    }
    
    /**
     * 设置 Label 样式
     *
     * @param width    宽度
     * @param color    字体颜色
     * @param fontSize 字体大小
     * @return 样式
     */
    public static String setLabel(double width, String color, double fontSize) {
        return "-fx-text-alignment: center;" +
                "-fx-font-family: KaiTi;" +
                "-fx-padding: 6 0 6 0;" +
                "-fx-alignment: center;" +
                "-fx-pref-width: " + width + ";" +
                "-fx-text-fill:" + color + ";" +
                "-fx-font-size: " + fontSize + ";";
    }
    
    /**
     * 歌曲详情页封面旋转
     */
    private void rotateMusicBigPane() {
        musicBigPicTT = new RotateTransition(Duration.seconds(30), musicBigPic);
        musicBigPicTT.setOnFinished(actionEvent -> musicBigPicTT.play());
        musicBigPicTT.setInterpolator(Interpolator.LINEAR);
        musicBigPicTT.setFromAngle(0);
        musicBigPicTT.setToAngle(360);
        musicBigPicTT.play();
    }
    
    /**
     * 窗口淡入
     */
    private void fadeIn() {
        AnimationUtil.fade(rootPane, 0.5, 0, 0, 1);
    }
    
    /**
     * 控件拖拽绑定窗口位置
     */
    private void addDragEvent() {
        StageUtil.addDragEvent(toolbar, stage);
        StageUtil.addDragEvent(logo, stage);
        StageUtil.addDragEvent(title, stage);
    }
    
    /**
     * 初始化 StackPane
     *
     * @param pane StackPane
     */
    private void initStackPane(StackPane pane) {
        Slider slider = new Slider();
        slider.setId("slider");
        sliders.add(slider);
        
        Rectangle progress = new Rectangle();
        // 颜色条和滑块绑定
        progress.heightProperty().bind(slider.heightProperty().subtract(10));
        progress.widthProperty().bind(slider.widthProperty());
        progress.setFill(Color.web("#c3c3c3"));
        progresses.add(progress);
        
        MyListener.addSliderListener(pane, slider, progress, currentSkin);
        pane.getChildren().addAll(progress, slider);
    }
    
    /**
     * 初始化音量条
     */
    private void initSound() {
        sliders.get(1).setMax(1);
        sliders.get(1).setMin(0);
        sliders.get(1).setValue(0.5);
        progresses.get(1).setStyle(String.format("-fx-fill: linear-gradient(to right, #00BFFF %d%%, #c3c3c3 %d%%);", 50, 50));
    }
    
    /**
     * 列表的容器高度绑定与列表的高度
     */
    private void bindProperty() {
        listPane.prefHeightProperty().bind(list.heightProperty());
        lyricPane.prefHeightProperty().bind(lyric.heightProperty());
        catsOptionsPane.prefHeightProperty().bind(catsBox.heightProperty());
        playListsRootPane.prefHeightProperty().bind(playListsBox.heightProperty());
    }
    
}

class UpdateTableView extends Thread {
    
    static boolean isFinished = true;
    
    String type;
    
    UpdateTableView(String type) {
        this.type = type;
    }
    
    @Override
    public void run() {
        File file = new File(MusicUtil.ROOT_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        if (isFinished) {
            isFinished = false;
            MusicUtil.getAllLocalMusic(MusicUtil.ROOT_PATH);
            Platform.runLater(() -> MainController.controller.localMusicNumber.setText("共" + MusicUtil.localMusic.size() + "首"));
            if ("manual".equals(type)) {
                NoticeController.show("文件夹更新成功！");
            }
            isFinished = true;
        } else {
            if ("manual".equals(type)) {
                NoticeController.show("请耐心等待文件夹更新，不要频繁操作！");
            }
        }
    }
    
}

/**
 * 每 0.5 秒自动更新本地音乐文件夹与相关 UI 界面和 array list，防止出现文件修改造成异常
 */
class AutoUpdate extends Thread {
    
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(500);
                new Thread(new UpdateTableView("auto")).start();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}

/**
 * 更新歌单线程
 */
class UpdatePlayList extends Thread {
    
    private static String cat = "";
    
    private static String type = "";
    
    UpdatePlayList(String cat, String type) {
        UpdatePlayList.cat = cat;
        UpdatePlayList.type = type;
    }
    
    @Override
    public void run() {
        MainController.controller.updatePlayList.setDisable(true);
        MainController.updatePlayList(cat);
        if ("manual".equals(type)) {
            NoticeController.show("歌单更新成功！");
        }
        MainController.controller.updatePlayList.setDisable(false);
    }
    
}

class GetPlayListSongs extends Thread {
    
    public static String listUrl;
    
    GetPlayListSongs(String listUrl) {
        GetPlayListSongs.listUrl = listUrl;
    }
    
    @Override
    public void run() {
        MusicUtil.getNetEaseSongInfo(listUrl);
    }
    
}