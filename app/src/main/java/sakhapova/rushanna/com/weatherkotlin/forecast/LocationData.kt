package sakhapova.rushanna.com.weatherkotlin.forecast

import com.google.gson.annotations.SerializedName

data class LocationData(@SerializedName("name") val name: String,
                        @SerializedName("country") val country: String)