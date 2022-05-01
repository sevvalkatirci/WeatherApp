package com.example.weatherapp.service

import com.example.weatherapp.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q=izmir&APPID=1cf9a94de5b794f739d54bd82a2a4916

interface WeatherAPI {
    @GET("data/2.5/weather?&units=metric&APPID=1cf9a94de5b794f739d54bd82a2a4916")
    fun getdata(
        @Query("q") cityName:String
    ):Single<WeatherModel>
}