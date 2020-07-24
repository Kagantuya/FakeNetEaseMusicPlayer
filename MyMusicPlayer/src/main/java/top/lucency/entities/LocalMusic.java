package top.lucency.entities;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author 86181
 */
public class LocalMusic {
    
    /**
     * 顺序
     */
    private SimpleStringProperty id;
    
    /**
     * 歌名
     */
    private SimpleStringProperty name;
    
    /**
     * 歌手
     */
    private SimpleStringProperty author;
    
    /**
     * 专辑名
     */
    private SimpleStringProperty album;
    
    /**
     * 歌曲大小
     */
    private SimpleStringProperty size;
    
    /**
     * 歌曲总时长
     */
    private SimpleStringProperty time;
    
    public LocalMusic(String id, String name, String author, String album, String size, String time) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.author = new SimpleStringProperty(author);
        this.album = new SimpleStringProperty(album);
        this.size = new SimpleStringProperty(size);
        this.time = new SimpleStringProperty(time);
    }
    
    public String getId() {
        return id.get();
    }
    
    public SimpleStringProperty idProperty() {
        return id;
    }
    
    public void setId(String id) {
        this.id.set(id);
    }
    
    public String getName() {
        return name.get();
    }
    
    public SimpleStringProperty nameProperty() {
        return name;
    }
    
    public void setName(String name) {
        this.name.set(name);
    }
    
    public String getAuthor() {
        return author.get();
    }
    
    public SimpleStringProperty authorProperty() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author.set(author);
    }
    
    public String getAlbum() {
        return album.get();
    }
    
    public SimpleStringProperty albumProperty() {
        return album;
    }
    
    public void setAlbum(String album) {
        this.album.set(album);
    }
    
    public String getSize() {
        return size.get();
    }
    
    public SimpleStringProperty sizeProperty() {
        return size;
    }
    
    public void setSize(String size) {
        this.size.set(size);
    }
    
    public String getTime() {
        return time.get();
    }
    
    public SimpleStringProperty timeProperty() {
        return time;
    }
    
    public void setTime(String time) {
        this.time.set(time);
    }
}
