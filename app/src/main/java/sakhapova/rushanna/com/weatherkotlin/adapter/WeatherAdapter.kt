package sakhapova.rushanna.com.weatherkotlin.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_item_current.view.*
import kotlinx.android.synthetic.main.view_item_day.view.*
import sakhapova.rushanna.com.weatherkotlin.Constants.Companion.VIEW_TYPE_DAILY
import sakhapova.rushanna.com.weatherkotlin.Constants.Companion.VIEW_TYPE_NOW
import sakhapova.rushanna.com.weatherkotlin.R
import sakhapova.rushanna.com.weatherkotlin.forecast.*
import java.text.SimpleDateFormat
import java.util.*


class WeatherAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @SuppressLint("ConstantLocale", "SimpleDateFormat")
     val simpleDateFormat = SimpleDateFormat("EEE, d MMMM ", Locale.ENGLISH)

    var weatherData: WeatherData? = null

    class CurrentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvCondition = itemView.tvCondition
        private val conditionIv = itemView.ivCondition
        private val tvFeelsLike = itemView.tvFeelsLike
        private val tvTemperature = itemView.tvTemperature
        private val tvCityCountry = itemView.tvCityCountry
        private val tvDataTime = itemView.tvDataTime


        @SuppressLint("SetTextI18n")
        fun onBind(currentData: HourlyForecast, locationData: LocationData, simpleDateFormat: SimpleDateFormat) {
            tvCondition.text = currentData.condition.text
            tvTemperature.text = itemView.context.getString(R.string.temperature_format, currentData.temperature.toInt())
            tvFeelsLike.text = itemView.context.getString(R.string.temperature_feels_like_format, currentData.temperatureFeelsLike.toInt())
            tvCityCountry.text = locationData.country + ", " + locationData.name
            tvDataTime.text = simpleDateFormat.format(Date())
            Picasso.with(itemView.context).load(currentData.condition.iconUrl.replace("//", "https://")).into(conditionIv)
       }
    }

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvData = itemView.tvDateDay
        private val ivCondition = itemView.ivConditionDay
        private val tvTemperature = itemView.tvTemperatureDay

        fun onBind(simpleDateFormat: SimpleDateFormat, dailyForecastContainer: DailyForecastContainer, dailyForecast: DailyForecast){
            tvData.text = simpleDateFormat.format(dailyForecastContainer.dateTs*1000)
            tvTemperature.text = itemView.context.getString(R.string.temperature_format, dailyForecast.averageTemperature.toInt())
            Picasso.with(itemView.context).load(dailyForecast.condition.iconUrl.replace("//", "https://")).into(ivCondition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return if (viewType == VIEW_TYPE_NOW) {
            val view: View = layoutInflater.inflate(R.layout.view_item_current, parent, false)

            WeatherAdapter.CurrentViewHolder(view)
        } else {
            val view = layoutInflater.inflate(R.layout.view_item_day, parent, false)

            DayViewHolder(view)
        }
    }

    @SuppressLint("StringFormatMatches")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        if (viewType == VIEW_TYPE_NOW) {

            val currentViewHolder = holder as WeatherAdapter.CurrentViewHolder
            val currentData = weatherData?.currentData
            val locationData = weatherData?.location
            currentViewHolder.onBind(currentData!!, locationData!!,simpleDateFormat)
        } else {
            val dayViewHolder = holder as DayViewHolder

            val dailyForecastContainer = weatherData?.forecast?.forecast!![position - 1]
            val dailyForecast = dailyForecastContainer.dailyForecast
            dayViewHolder.onBind(simpleDateFormat, dailyForecastContainer, dailyForecast)

        }
    }

    override fun getItemCount(): Int = weatherData?.forecast?.forecast?.size?.plus(1) ?: 0


        override fun getItemViewType(position: Int): Int {
            return when (position) {
                0 -> VIEW_TYPE_NOW
                else -> VIEW_TYPE_DAILY
            }
        }
    }

