package com.example.weatherapp.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelStore
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel:MainViewModel
    private lateinit var GET:SharedPreferences
    private lateinit var SET:SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GET=getSharedPreferences(packageName, MODE_PRIVATE)
        SET=GET.edit()

        viewModel= ViewModelProviders.of(this).get(MainViewModel::class.java)

        var cName=GET.getString("cityName","ankara")
        edit_city_name.setText(cName)

        viewModel.refreshData(cName!!)

        getLiveData()

        swipe_refresh_layout.setOnRefreshListener {
            ll_data_view.visibility=View.GONE
            textView_error.visibility=View.GONE
            progressBar_loading.visibility=View.GONE

            var cityName=GET.getString("cityName",cName)
            edit_city_name.setText(cityName)
            viewModel.refreshData(cityName!!)
            swipe_refresh_layout.isRefreshing=false
        }

        img_search_city_name.setOnClickListener{
            val cityName=edit_city_name.text.toString()
            SET.putString("cityName",cityName)
            SET.apply()
            viewModel.refreshData(cityName)
            getLiveData()
        }

    }

    private fun getLiveData() {
        viewModel.weather_data.observe(this, Observer { data->
            data?.let {
                ll_data_view.visibility= View.VISIBLE
                progressBar_loading.visibility=View.GONE
                textView_degree.text=data.main.temp.toString()+"°C"
                textView_countryCode.text=data.sys.country.toString()
                textView_cityName.text=data.name.toString()
                textView_humidity.text=data.main.humidity.toString()
                textView_speed.text=data.wind.speed.toString()
                textView_lat.text=data.coord.lat.toString()
                textView_lon.text=data.coord.lon.toString()

               Glide.with(this)
                    .load("http://openweathermap.org/img/wn/"+ data.weather.get(0).icon+"@2x.png")
                    .into(img_cloud)
            }
        })

        viewModel.weather_load.observe(this, Observer { load->
            load?.let {
                if(it){
                    progressBar_loading.visibility=View.VISIBLE
                    textView_error.visibility=View.GONE
                    ll_data_view.visibility=View.GONE
                }else{
                    progressBar_loading.visibility=View.GONE
                }
            }
        })

        viewModel.weather_error.observe(this, Observer { error->
            error?.let{
                if(it){
                    textView_error.visibility=View.VISIBLE
                    ll_data_view.visibility=View.GONE
                    progressBar_loading.visibility=View.GONE
                }else{
                    textView_error.visibility=View.GONE
                }
            }
        })

    }
}