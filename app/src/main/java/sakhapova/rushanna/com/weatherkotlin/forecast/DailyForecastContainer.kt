package sakhapova.rushanna.com.weatherkotlin.forecast

import com.google.gson.annotations.SerializedName
import java.util.*

 data class DailyForecastContainer(@SerializedName("date_epoch") val dateTs: Long,
                                   @SerializedName("day") val dailyForecast: DailyForecast,
                                   var date: Date)
