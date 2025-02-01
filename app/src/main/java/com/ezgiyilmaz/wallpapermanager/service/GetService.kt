package com.ezgiyilmaz.wallpapermanager.service

import com.ezgiyilmaz.wallpapermanager.model.Wallpaper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetService {
    @GET("planetary/apod") // NASA'nın Astronomy Picture of the Day API'si
    fun getWallpapers(
        @Query("api_key") apiKey: String,
        @Query("count") count: Int, // Birden fazla fotoğraf çekmek için
    ): Call<List<Wallpaper>> // Dönen veriyi liste olarak alıyoruz

    }
