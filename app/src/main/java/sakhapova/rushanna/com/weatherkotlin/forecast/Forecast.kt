package sakhapova.rushanna.com.weatherkotlin.forecast

import com.google.gson.annotations.SerializedName

data class Forecast(@SerializedName("forecastday") val forecast: List<DailyForecastContainer>)