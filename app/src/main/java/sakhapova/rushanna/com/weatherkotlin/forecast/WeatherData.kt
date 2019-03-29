package sakhapova.rushanna.com.weatherkotlin.forecast

import com.google.gson.annotations.SerializedName

data class WeatherData(@SerializedName("location") val location: LocationData,
                       @SerializedName("current") val currentData: HourlyForecast,
                       @SerializedName("forecast") val forecast: Forecast)