package top.lucency.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.http.client.params.AllClientPNames;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import top.lucency.controller.MainController;
import top.lucency.controller.MyListener;
import top.lucency.controller.NoticeController;
import top.lucency.entities.LocalMusic;
import top.lucency.entities.Music;
import top.lucency.entities.NetEaseMusic;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.*;

/**
 * @author 86181
 */
public class MusicUtil {
    
    /**
     * 存放 ffmpeg.exe 的文件夹路径
     */
    private static final String FFMPEG_PATH = "src/main/resources/tool";
    
    /**
     * 本地音乐根目录
     */
    public static final String ROOT_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath().replaceAll("\\\\", "/") + "/test/";
    
    /**
     * 指针旋转的角度
     */
    private static int angle = 35;
    
    /**
     * 当前指针角度
     */
    private static double currentAngle = -angle;
    
    /**
     * player
     */
    public static MediaPlayer player;
    
    /**
     * media
     */
    public static Media media;
    
    /**
     * 本地音乐名称列表
     */
    public static ArrayList<String> localMusic = new ArrayList<>();
    
    /**
     * 歌单歌曲名称和 ID 列表
     */
    public static ArrayList<Music> playListNameAndIds = new ArrayList<>();
    
    /**
     * 下一首网易云歌曲
     *
     * @return 歌曲信息
     */
    public static Music initNetEaseMusic(Music music) {
        String real = getReal("http://music.163.com/song/media/outer/url?id=" + music.getAuthor() + ".mp3");
        if("https://music.163.com/404".equals(real)){
            NoticeController.show("此单曲为付费单曲无法播放！");
        }
        initPlayer(real);
        return music;
    }
    
    /**
     * 获取 302 跳转后的真实地址
     *
     * @param url 原始地址
     * @return 真实地址
     */
    public static String getReal(String url) {
        try {
            HttpClient client = new HttpClient();
            HttpMethod method = new GetMethod(url);
            HttpParams params = client.getParams();
            params.setParameter(AllClientPNames.HANDLE_REDIRECTS, false);
            client.executeMethod(method);
            return method.getURI().getURI();
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
    }
    
    /**
     * 下一首本地歌曲
     *
     * @return 歌曲信息
     */
    public static Music initLocalMusic(String path) {
        initPlayer("file:/" + path);
        new Thread(new Wait()).start();
        while (true) {
            // 获取信息超时
            if (Wait.isOverTime) {
                Wait.isOverTime = false;
                return new Music("未知歌名", "未知歌手", null, null);
            }
            if (media.getMetadata().get("artist") != null && media.getMetadata().get("title") != null) {
                String author = (String) media.getMetadata().get("artist");
                String name = (String) media.getMetadata().get("title");
                return ConnectUtil.searchLocalMusicInfo(name, author);
            }
        }
    }
    
    /**
     * 获得本地音乐文件夹中名称列表
     * 由于 ffmpeg 中文件不能包含空格，因此将所有文件去空格重命名后操作
     *
     * @param path 本地音乐文件夹路径
     */
    public static void getAllLocalMusic(String path) {
        ObservableList<LocalMusic> data = FXCollections.observableArrayList();
        File[] list = new File(path).listFiles();
        localMusic.clear();
        // 有效歌曲计数
        int id = 0;
        
        for (File f : Objects.requireNonNull(list)) {
            
            String allName = f.getName();
            String newAllName = replaceLegalization(allName);
            String name = allName.substring(0, allName.lastIndexOf("."));
            String newName = replaceLegalization(name);
            String suffix = getSuffix(allName);
            String musicPath = f.getPath();
            String newPath = convertLastString(musicPath, name, newName);
            
            renameFile(musicPath, name);
            
            if ("wav".equals(suffix) || "mp3".equals(suffix) || "flac".equals(suffix)) {
                
                // 只有转换到 mp3 才便于数据收集分析
                if (!"mp3".equals(suffix)) {
                    File tmp = new File(convertLastString(newPath, suffix, "mp3"));
                    if (!tmp.exists()) {
                        convertToMp3(newPath);
                    }
                }
                
                data.add(getLocalMusicInfo(convertLastString(newPath, suffix, "mp3"), id + 1));
                localMusic.add(convertLastString(newAllName, suffix, "mp3"));
                id++;
            }
        }
        
        MainController.updateLocalSongs(data);
    }
    
    /**
     * 获得网易云歌单中歌曲的信息
     */
    public static void getNetEaseSongInfo(String listUrl) {
        playListNameAndIds = ConnectUtil.getNetEaseListSong(listUrl);
        ObservableList<NetEaseMusic> data = FXCollections.observableArrayList();
        for (int i = 0; i < playListNameAndIds.size(); i++) {
            Music music = playListNameAndIds.get(i);
            data.add(new NetEaseMusic(String.format("%03d", i + 1), music.getName(), music.getAuthor(), "album", "time"));
        }
        MainController.updatePlayListSongs(data);
    }
    
    /**
     * 重命名为去掉空格的文件
     *
     * @param path 原路径
     * @param name 原文件名
     */
    private static void renameFile(String path, String name) {
        String newName = replaceLegalization(name);
        String newPath = convertLastString(path, name, newName);
        new File(path).renameTo(new File(newPath));
    }
    
    /**
     * 替换所有空格
     *
     * @param origin 原始字串
     * @return 替换后的结果
     */
    private static String replaceLegalization(String origin) {
        return origin.replaceAll(" ", "").replaceAll("\\u00A0", "");
    }
    
    /**
     * 解析本地歌曲的时长，专辑，大小
     *
     * @param path 歌曲路径
     * @param id   当前个数
     * @return 音乐实体
     */
    private static LocalMusic getLocalMusicInfo(String path, int id) {
        MP3File file = null;
        try {
            file = new MP3File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MP3AudioHeader header = Objects.requireNonNull(file).getMP3AudioHeader();
        AbstractID3v2Tag tag = file.getID3v2Tag();
        
        String index = String.format("%03d", id);
        String size = getMb(new File(path).length());
        String time = convertToMinAndSec(header.getTrackLength());
        
        if (tag.frameMap.get("TIT2") != null) {
            String name = tag.getFirst(FieldKey.TITLE);
            String author = tag.getFirst(FieldKey.ARTIST);
            String album = tag.getFirst(FieldKey.ALBUM);
            return new LocalMusic(index, name, author, album, size, time);
        } else {
            return new LocalMusic(index, "未知歌名", "未知歌手", "未知专辑", size, time);
        }
    }
    
    /**
     * 转换歌曲大小为 mb 单位
     *
     * @param size b 单位
     * @return mb 单位
     */
    public static String getMb(double size) {
        return String.format("%.2fMB", size / 1024 / 1024);
    }
    
    /**
     * 初始化 MediaPlayer
     */
    private static void initPlayer(String url) {
        
        if (player != null) {
            player.dispose();
        }
        media = new Media(url);
        player = new MediaPlayer(media);
        
        Slider musicSlider = MainController.controller.sliders.get(0);
        Slider soundSlider = MainController.controller.sliders.get(1);
        
        // 频谱监听
        player.setAudioSpectrumListener(MyListener.setAudioListener());
        player.setOnReady(() -> {
            // 设置当前音量
            player.setVolume(MainController.currentSound);
            // 绑定音量
            player.volumeProperty().bind(soundSlider.valueProperty());
            // 音轨跳转绑定
            musicSlider.setOnMousePressed(mouseEvent -> MainController.mouse = true);
            musicSlider.setOnMouseReleased(mouseEvent -> {
                MainController.mouse = false;
                player.seek(Duration.seconds(musicSlider.getValue()));
            });
            player.play();
            
            double total = player.getTotalDuration().toSeconds();
            musicSlider.setMax(total);
            MainController.controller.totalTime.setText(convertToMinAndSec(total));
        });
        
        // 放完自动切歌
        player.setOnEndOfMedia(() -> MainController.controller.next());
        
        if (currentAngle != angle) {
            // 指针滑入
            AnchorPane pane = MainController.controller.needle;
            AnimationUtil.rotateByPosition(pane, 0, 35, 0, pane.getHeight() / 2, 0.3, 0);
            currentAngle = angle;
        }
    }
    
    /**
     * 将秒数转换为分秒格式
     *
     * @param total 总秒数
     * @return 分秒格式
     */
    public static String convertToMinAndSec(double total) {
        return String.format(" %02d :" + " %02d", (int) total / 60, (int) total % 60);
    }
    
    /**
     * 将频谱数据转换为可视化的 rectangle 高度
     *
     * @param num 频谱数据
     * @return rectangle 高度
     */
    public static int getHeight(float num) {
        return (int) ((num + 60) * 5);
    }
    
    /**
     * 时间转为秒
     *
     * @param time 时间
     * @return 秒数
     */
    public static int getSeconds(String time) {
        // 是数字
        if (Character.isDigit(time.split(":")[0].charAt(0))) {
            int min = Integer.parseInt(time.substring(0, 2));
            int sec = Integer.parseInt(time.substring(3, 5));
            return min * 60 + sec;
        }
        return 0;
    }
    
    /**
     * 暂停
     */
    public static void pause() {
        // 指针滑出
        AnchorPane pane = MainController.controller.needle;
        AnimationUtil.rotateByPosition(pane, 0, -35, 0, pane.getHeight() / 2, 0.3, 0.2);
        currentAngle = -angle;
        MainController.musicBigPicTT.pause();
        player.pause();
    }
    
    /**
     * 播放
     *
     * @return Media Player 是否被初始化
     */
    public static boolean play() {
        if (player != null) {
            // 指针滑入
            AnchorPane pane = MainController.controller.needle;
            AnimationUtil.rotateByPosition(pane, 0, 35, 0, pane.getHeight() / 2, 0.3, 0);
            MainController.musicBigPicTT.play();
            currentAngle = angle;
            player.play();
            return true;
        }
        return false;
    }
    
    /**
     * 获得路径后缀
     *
     * @param path 路径
     * @return 后缀
     */
    private static String getSuffix(String path) {
        String[] spits = path.split("\\\\");
        String[] parts = spits[spits.length - 1].split("\\.");
        return parts[parts.length - 1];
    }
    
    /**
     * 替换最后一个符合条件的字符串
     *
     * @param string  原始字串
     * @param match   被匹配的字串
     * @param replace 替换字串
     * @return 替换后的字串
     */
    private static String convertLastString(String string, String match, String replace) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int lastIndexOf = stringBuilder.lastIndexOf(match);
        if (-1 == lastIndexOf) {
            return string;
        }
        return stringBuilder.replace(lastIndexOf, lastIndexOf + match.length(), replace).toString();
    }
    
    /**
     * 音频转换为 mp3 格式
     *
     * @param path 转换的文件路径
     */
    private static void convertToMp3(String path) {
        String suffix = getSuffix(path);
        String to = convertLastString(path, suffix, "mp3");
        Runtime run = null;
        long start = System.currentTimeMillis();
        try {
            run = Runtime.getRuntime();
            Process p = run.exec(new File(FFMPEG_PATH).getAbsolutePath() + "/ffmpeg -y -i " + path + " -ar 44100 -ac 2 -acodec mp3 -ab 128k " + to);
            p.getOutputStream().close();
            p.getInputStream().close();
            p.getErrorStream().close();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Objects.requireNonNull(run).freeMemory();
        }
        deleteMusic(path);
    }
    
    /**
     * 删除本地音乐
     *
     * @param url 音乐地址
     */
    private static void deleteMusic(String url) {
        new File(url).delete();
    }
    
}

class Wait extends Thread {
    
    public static boolean isOverTime = false;
    
    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            if (MusicUtil.media != null && MusicUtil.media.getMetadata().get("title") == null) {
                isOverTime = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
