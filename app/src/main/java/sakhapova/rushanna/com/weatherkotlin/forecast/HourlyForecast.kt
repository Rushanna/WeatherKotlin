package sakhapova.rushanna.com.weatherkotlin.forecast

import com.google.gson.annotations.SerializedName

data class HourlyForecast(
    @SerializedName("temp_c") val temperature: Float,
    @SerializedName("feelslike_c") val temperatureFeelsLike: Float,
    val condition: Condition
)