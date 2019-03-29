package sakhapova.rushanna.com.weatherkotlin.forecast

import com.google.gson.annotations.SerializedName

data class DailyForecast (@SerializedName("avgtemp_c") val averageTemperature: Float, val condition: Condition)