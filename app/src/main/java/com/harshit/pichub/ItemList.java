package com.harshit.pichub;

public class ItemList {
    private int favourite;
    private int likes;
    private int downloads;
    private int views;
    private String imageUrl;
    private String downloadImageUrl;
    private int mId;

    public ItemList(int favourite, int likes, int downloads, int views, String imageUrl, String downloadImageUrl, int mId) {
        this.favourite = favourite;
        this.likes = likes;
        this.downloads = downloads;
        this.views = views;
        this.imageUrl = imageUrl;
        this.downloadImageUrl = downloadImageUrl;
        this.mId = mId;
    }

    public ItemList() {
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDownloadImageUrl() {
        return downloadImageUrl;
    }

    public void setDownloadImageUrl(String downloadImageUrl) {
        this.downloadImageUrl = downloadImageUrl;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}