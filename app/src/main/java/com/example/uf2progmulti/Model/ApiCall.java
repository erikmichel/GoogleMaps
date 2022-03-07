package com.example.uf2progmulti.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCall {
    @GET("json?")
    Call<SunriseApi> getData(@Query("lat") String lat, @Query("lng") String lng);

    @GET("?method=flickr.photos.search&api_key=79d466885188b99d6762980d64029892&format=json&nojsoncallback=1")
    Call<FlickrApi> getDataFlickr(@Query("lat") String lat, @Query("lon") String lng);
}
