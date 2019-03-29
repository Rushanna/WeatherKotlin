package sakhapova.rushanna.com.weatherkotlin.forecast

import com.google.gson.annotations.SerializedName

data class Condition(@SerializedName("icon") val iconUrl: String, val text: String)