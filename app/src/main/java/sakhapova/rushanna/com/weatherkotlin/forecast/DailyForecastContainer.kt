package sakhapova.rushanna.com.weatherkotlin.forecast

import com.google.gson.annotations.SerializedName

data class DailyForecastContainer(
    @SerializedName("date_epoch") val dateTs: Long,
    @SerializedName("day") val dailyForecast: DailyForecast
)
