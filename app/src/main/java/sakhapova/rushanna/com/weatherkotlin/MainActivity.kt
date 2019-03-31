package sakhapova.rushanna.com.weatherkotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import sakhapova.rushanna.com.weatherkotlin.adapter.WeatherAdapter
import sakhapova.rushanna.com.weatherkotlin.forecast.WeatherData
import sakhapova.rushanna.com.weatherkotlin.forecast.api.ApiInterface

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var weatherAdapter: WeatherAdapter? = null
    private var locationManager: LocationManager? = null
    private val api = ApiInterface.createApi()
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = rvWeather
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.layoutManager = layoutManager

        weatherAdapter = WeatherAdapter()
        recyclerView.adapter = weatherAdapter

        swipeRefreshLayout = swipeСontainer
        swipeRefreshLayout?.setOnRefreshListener(this)
        swipeRefreshLayout?.setColorSchemeColors(
            Color.RED, Color.GREEN, Color.BLUE, Color.CYAN
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        checkAndRequestGeoPermission()
    }

    private fun showMessageOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@MainActivity)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .create()
            .show()
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(appSettingsIntent, REQUEST_CODE_LOCATION_PERMISSION)
    }

    private fun checkAndRequestGeoPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        } else {
            setupLocationManager()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                setupLocationManager()
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showMessageOK(getString(R.string.Message),
                        DialogInterface.OnClickListener { _, _ ->
                            requestPermissions(
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                REQUEST_CODE_LOCATION_PERMISSION
                            )
                            openApplicationSettings()
                        })
                } else {
                    checkAndRequestGeoPermission()
                }
            }
        }
    }


    override fun onStop() {
        if (locationManager != null) {
            locationManager?.removeUpdates(locationListener)
        }

        super.onStop()
    }

    @SuppressLint("MissingPermission")
    private fun setupLocationManager() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager != null) {
            val provider = locationManager!!.getBestProvider(Criteria(), true)

            if (provider != null) {
                loadData(AUTO_IP_QUERY)
                locationManager?.requestLocationUpdates(
                    provider,
                    10000,
                    10f,
                    locationListener
                )
            }
        }
    }

    private fun loadData(query: String) {
        swipeRefreshLayout?.isRefreshing = true
        api.getForecast(API_KEY, query, 10).enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                swipeRefreshLayout?.isRefreshing = false
                if (response.isSuccessful) {
                    weatherAdapter?.weatherData = response.body()
                    weatherAdapter?.notifyDataSetChanged()
                } else {
                    Toast.makeText(applicationContext, "Unknown error.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                swipeRefreshLayout?.isRefreshing = false
                Toast.makeText(applicationContext, "Unable to load data. Check Internet connection.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            loadData(location.latitude.toString() + "," + location.longitude)
            locationManager?.removeUpdates(this)
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

        }

        override fun onProviderEnabled(s: String) {

        }

        override fun onProviderDisabled(s: String) {

        }
    }

    override fun onRefresh() {
        loadData(AUTO_IP_QUERY)
    }
}
