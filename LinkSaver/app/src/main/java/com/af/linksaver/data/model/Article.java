package com.af.linksaver.data.model;

import java.util.ArrayList;
import java.util.List;

public class Article {
    private String id;
    private String title;
    private String url;
    private String excerpt;
    private String imageUrl;
    private long timestamp;
    private String userId;
    private boolean isArchived;
    private boolean isFavorite;
    private List<String> tags;

    // Construtores, getters e setters
    public Article() {
        // Necessário para Firebase
    }

    public Article(String title, String url, String userId) {
        this.title = title;
        this.url = url;
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
        this.isArchived = false;
        this.isFavorite = false;
        this.tags = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
