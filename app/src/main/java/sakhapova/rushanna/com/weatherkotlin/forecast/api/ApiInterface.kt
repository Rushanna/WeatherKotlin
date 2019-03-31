package sakhapova.rushanna.com.weatherkotlin.forecast.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import sakhapova.rushanna.com.weatherkotlin.API_BASE_URL
import sakhapova.rushanna.com.weatherkotlin.forecast.WeatherData

interface ApiInterface {

    @GET("forecast.json")
    fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("days") days: Int
    ): Call<WeatherData>


    companion object ApiFactory {

        fun createApi(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}