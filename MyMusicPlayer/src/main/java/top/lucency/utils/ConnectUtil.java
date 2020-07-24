package top.lucency.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.lucency.entities.Music;
import top.lucency.entities.NetEasePlayList;

/**
 * @author 86181
 */
public class ConnectUtil {
    
    /**
     * 请求头，防止频繁访问被拉黑 ip
     */
    private static final Map<String, String> HEADERS = new HashMap<>();
    
    static {
        HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        HEADERS.put("Referer", "https//music.163.com");
        HEADERS.put("Host", "music.163.com");
    }
    
    /**
     * 网易云接口 KEY
     */
    private static final String KEY = "ed469ea3da8fc1f2";
    
    /**
     * 连接 url
     *
     * @param string url 字符串
     * @return URL
     */
    public static URL connect(String string) {
        URL url = null;
        try {
            url = new URL(string);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(1);
            httpUrlConnection.setReadTimeout(1);
            httpUrlConnection.connect();
        } catch (Exception ignored) {
        }
        return url;
    }
    
    /**
     * 获取网易云歌单网站源码
     *
     * @param url url
     * @return 网站源码
     */
    public static String getNetEaseHtml(String url) {
        try {
            Connection connection = Jsoup.connect(url).ignoreContentType(true);
            for (String header : HEADERS.keySet()) {
                connection.header(header, HEADERS.get(header));
            }
            connection.method(Connection.Method.GET);
            connection.timeout(1000);
            return connection.execute().body();
        } catch (Exception e) {
            return "error";
        }
    }
    
    /**
     * 取出网易云 api 返回的数据节点
     *
     * @param lyricJson 歌词 json
     * @return 歌曲信息或歌词信息
     */
    public static ArrayList<String> getNetEaseNodes(String lyricJson) {
        ArrayList<String> kv = new ArrayList<>();
        travelNetEaseJson(kv, JSONObject.parseObject(lyricJson));
        return kv;
    }
    
    /**
     * 拆分出歌曲信息或歌词信息键值对列表
     *
     * @param jsonObject jsonObject
     */
    private static void travelNetEaseJson(ArrayList<String> kv, JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            if (jsonObject.get(key) instanceof JSONObject) {
                travelNetEaseJson(kv, JSONObject.parseObject(String.valueOf(jsonObject.get(key))));
            } else if (jsonObject.get(key) instanceof JSONArray) {
                kv.add(jsonObject.get(key).toString());
            } else {
                kv.add(jsonObject.get(key).toString());
            }
        }
    }
    
    /**
     * 获得网易云歌词
     *
     * @param id 歌曲 ID
     * @return 时间轴和歌词
     */
    public static ArrayList<Map<String, String>> getNetEaseLyric(String id) {
        String lyricJson = getNetEaseHtml("http://music.163.com/api/song/media?id=" + id);
        ArrayList<String> kv = getNetEaseNodes(lyricJson);
        if (kv.size() == 1) {
            ArrayList<Map<String, String>> lyrics = new ArrayList<>();
            Map<String, String> map = new HashMap<>(1);
            map.put("00:00", "暂无歌词");
            lyrics.add(map);
            return lyrics;
        }
        return analysisLyric(getNetEaseNodes(lyricJson).get(1));
    }
    
    /**
     * 获取歌单分类
     *
     * @return 歌单分类列表
     */
    public static Map<String, ArrayList<String>> getNetEaseCats() {
        String url = "https://music.163.com/discover/playlist/?order=hot&cat=%E5%85%A8%E9%83%A8";
        String html = getNetEaseHtml(url);
        
        Document document = Jsoup.parse(html);
        Elements dlItems = document.getElementsByClass("f-cb");
        
        Map<String, ArrayList<String>> cats = new HashMap<>(1);
        for (Element dl : dlItems) {
            Elements dt = dl.select("dt");
            Elements dd = dl.select("dd");
            Elements aItems = dd.select("a");
            ArrayList<String> contents = new ArrayList<>();
            for (Element a : aItems) {
                contents.add(a.text());
            }
            cats.put(dt.text(), contents);
        }
        return cats;
    }
    
    /**
     * 随机访问一页网易云歌单网页
     *
     * @param cat 标签
     * @return 歌单列表
     */
    public static ArrayList<NetEasePlayList> getNetEasePlayList(String cat) {
        if ("全部歌单".equals(cat)) {
            cat = "全部";
        }
        cat = URLEncoder.encode(cat, StandardCharsets.UTF_8);
        int offset = (int) (Math.random() * 37.0D + 1.0D) * 35;
        String url = "https://music.163.com/discover/playlist/?order=hot&cat=" + cat + "&limit=35&offset=" + offset;
        String html = getNetEaseHtml(url);
        
        Document document = Jsoup.parse(html);
        Elements liItems = document.getElementsByClass("u-cover u-cover-1");
        Elements aItems = liItems.select("a");
        Elements imageItems = liItems.select("img");
        
        ArrayList<NetEasePlayList> playLists = new ArrayList<>();
        // 由于获得的超链接标签中永远是第一个为歌单 url 第二个为播放图标 url （无用），因此设置步长为 2 ，同时也能和 img 标签下标对应
        for (int i = 0; i < aItems.size(); i += 2) {
            Element a = aItems.get(i);
            String playListTitle = a.attr("title");
            String tmpHref = a.attr("href");
            String playListPicUrl = imageItems.get(i / 2).attr("src");
            String playListUrl = "https://music.163.com" + tmpHref;
            playLists.add(new NetEasePlayList(playListTitle, playListPicUrl, playListUrl, getPlayListInfo(playListUrl)));
        }
        return playLists;
    }
    
    /**
     * 获得歌单简介
     *
     * @param url url
     * @return 简介
     */
    private static String getPlayListInfo(String url) {
        String html = getNetEaseHtml(url.replace("#/", ""));
        Document document = Jsoup.parse(html);
        
        Elements pHideItems = document.getElementsByClass("intr f-brk f-hide");
        Elements pItems = document.getElementsByClass("intr f-brk");
        String playListInfo;
        if (pHideItems.text().length() == 0) {
            playListInfo = pItems.text();
        } else {
            playListInfo = pHideItems.text();
        }
        return playListInfo;
    }
    
    /**
     * 从网易云歌单源码中拆分出音乐列表
     *
     * @param url url
     */
    public static ArrayList<Music> getNetEaseListSong(String url) {
        String html = getNetEaseHtml(url.replace("#/", ""));
        Document document = Jsoup.parse(html);
        ArrayList<Music> arrayList = new ArrayList<>();
        
        Elements ulItems = document.getElementsByClass("f-hide");
        Elements aItems = ulItems.select("a");
        for (Element a : aItems) {
            String name = a.text();
            String id = a.attr("href").substring(9);
            arrayList.add(new Music(name, id, getNetEaseMusicPicUrl(id), getNetEaseLyric(id)));
        }
        return arrayList;
    }
    
    /**
     * 获得单曲图片
     *
     * @param musicId id
     * @return 图片路径
     */
    public static String getNetEaseMusicPicUrl(String musicId) {
        URL url = connect("https://api88.net/api/netease/?key=" + KEY + "&id=" + musicId + "&type=song");
        ArrayList<Map<String, String>> kv = new ArrayList<>();
        travelLocalJson(kv, JSONObject.parseObject(readData(url)));
        return kv.get(1).get("pic");
    }
    
    // ===================================================================================== //
    
    /**
     * 根据歌名和歌手获得歌曲信息
     *
     * @param name   歌名
     * @param author 歌手
     * @return 歌曲实体
     */
    public static Music searchLocalMusicInfo(String name, String author) {
        String id = URLEncoder.encode(name, StandardCharsets.UTF_8);
        URL url = connect("https://api88.net/api/netease/?key=" + KEY + "&id=" + id + "&type=so&cache=0&nu=100");
        ArrayList<Map<String, String>> kvs = getRelevanceMusic(url);
        
        ArrayList<ArrayList<Map<String, String>>> songs = new ArrayList<>();
        // 把数据以每首歌六个 kv 组合拆分
        for (int i = 1; i < kvs.size(); i += 6) {
            ArrayList<Map<String, String>> song = new ArrayList<>();
            for (int j = i; j < i + 6; j++) {
                song.add(kvs.get(j));
            }
            songs.add(song);
        }
        
        // 判断每一首歌中歌手和歌名是否吻合
        for (ArrayList<Map<String, String>> arrayList : songs) {
            if (author.trim().equals(arrayList.get(0).get("author")) && name.trim().equals(arrayList.get(3).get("title"))) {
                String picUrl = arrayList.get(2).get("pic");
                String lyricUrl = arrayList.get(4).get("lrc");
                ArrayList<Map<String, String>> lyric = getLocalLyric(lyricUrl);
                return new Music(name, author, picUrl, lyric);
            }
        }
        
        return new Music(name, author, null, null);
    }
    
    /**
     * 获得本地歌曲歌词
     *
     * @param url url
     * @return 时间轴和歌词
     */
    private static ArrayList<Map<String, String>> getLocalLyric(String url) {
        String origin = readData(connect(url));
        return analysisLyric(origin);
    }
    
    /**
     * 按照歌名搜索相关的歌曲，处理返回的 json 数据
     *
     * @param url url
     * @return 相关联的歌曲信息键值对列表
     */
    private static ArrayList<Map<String, String>> getRelevanceMusic(URL url) {
        ArrayList<Map<String, String>> kv = new ArrayList<>();
        travelLocalJson(kv, JSONObject.parseObject(readData(url)));
        return kv;
    }
    
    /**
     * 拆分搜索到的歌曲信息键值对并添加进列表
     *
     * @param kv         列表
     * @param jsonObject 原始 json 数据
     */
    private static void travelLocalJson(ArrayList<Map<String, String>> kv, JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof String || value instanceof Number) {
                Map<String, String> map = new HashMap<>(1);
                map.put(key, String.valueOf(value));
                kv.add(map);
                continue;
            }
            if (value instanceof JSONObject) {
                travelLocalJson(kv, (JSONObject) value);
                continue;
            }
            if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                for (int i = 0; i < jsonArray.size(); i++) {
                    travelLocalJson(kv, jsonArray.getJSONObject(i));
                }
            }
        }
    }
    
    // ===================================================================================== //
    
    /**
     * 读取数据
     *
     * @return 数据
     */
    private static String readData(URL url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URLConnection uc = url.openConnection();
            InputStream in = uc.getInputStream();
            InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String string;
            while ((string = br.readLine()) != null) {
                stringBuilder.append(string).append("\n");
            }
            in.close();
            isr.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    
    /**
     * 分析歌词，拆分成时间轴对应歌词的列表
     *
     * @param origin 原始数据
     * @return 时间轴和歌词
     */
    private static ArrayList<Map<String, String>> analysisLyric(String origin) {
        ArrayList<Map<String, String>> lyrics = new ArrayList<>();
        if (origin.length() != 0) {
            for (String string : origin.split("\n")) {
                if (string.length() != 0 && string.contains("[")) {
                    String time = string.substring(string.indexOf("[") + 1, string.indexOf("]"));
                    String lyricContent = string.substring(string.indexOf("]") + 1).trim();
                    Map<String, String> map = new HashMap<>(1);
                    map.put(time, lyricContent);
                    lyrics.add(map);
                }
                if ("true".equals(string) || "暂无歌词".equals(string)) {
                    Map<String, String> map = new HashMap<>(1);
                    map.put("00:00", "纯音乐，请欣赏");
                    lyrics.add(map);
                    break;
                }
            }
        }
        return lyrics;
    }
    
}
