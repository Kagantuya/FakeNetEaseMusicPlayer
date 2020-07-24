package top.lucency.entities;

/**
 * @author 86181
 */
public class NetEasePlayList {
    
    /**
     * 歌单标题
     */
    private String playListTitle;
    
    /**
     * 歌单图片地址
     */
    private String playListPicUrl;
    
    /**
     * 歌单地址
     */
    private String playListUrl;
    
    /**
     * 歌单简介
     */
    private String playListInfo;
    
    public NetEasePlayList(String playListTitle, String playListPicUrl, String playListUrl, String playListInfo) {
        this.playListTitle = playListTitle;
        this.playListPicUrl = playListPicUrl;
        this.playListUrl = playListUrl;
        this.playListInfo = playListInfo;
    }
    
    public String getPlayListTitle() {
        return playListTitle;
    }
    
    public void setPlayListTitle(String playListTitle) {
        this.playListTitle = playListTitle;
    }
    
    public String getPlayListPicUrl() {
        return playListPicUrl;
    }
    
    public void setPlayListPicUrl(String playListPicUrl) {
        this.playListPicUrl = playListPicUrl;
    }
    
    public String getPlayListUrl() {
        return playListUrl;
    }
    
    public void setPlayListUrl(String playListUrl) {
        this.playListUrl = playListUrl;
    }
    
    public String getPlayListInfo() {
        return playListInfo;
    }
    
    public void setPlayListInfo(String playListInfo) {
        this.playListInfo = playListInfo;
    }
}
