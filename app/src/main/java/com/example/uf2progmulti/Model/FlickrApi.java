package com.example.uf2progmulti.Model;

public class FlickrApi {
    public Photos photos;
    public String stat;

    public FlickrApi() {
    }

    public FlickrApi(Photos photos, String stat) {
        this.photos = photos;
        this.stat = stat;
    }

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
