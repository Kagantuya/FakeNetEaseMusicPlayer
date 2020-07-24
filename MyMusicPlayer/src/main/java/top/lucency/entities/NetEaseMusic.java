package top.lucency.entities;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author 86181
 */
public class NetEaseMusic {
    
    private SimpleStringProperty id;
    
    private SimpleStringProperty title;
    
    private SimpleStringProperty author;
    
    private SimpleStringProperty album;
    
    private SimpleStringProperty time;
    
    public NetEaseMusic(String id, String title, String author, String album, String time) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.album = new SimpleStringProperty(album);
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
    
    public String getTitle() {
        return title.get();
    }
    
    public SimpleStringProperty titleProperty() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title.set(title);
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
