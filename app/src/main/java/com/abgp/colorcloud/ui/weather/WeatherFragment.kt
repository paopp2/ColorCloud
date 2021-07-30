package com.abgp.colorcloud.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.abgp.colorcloud.MainViewModel
import com.abgp.colorcloud.databinding.FragmentWeatherBinding
import com.abgp.colorcloud.models.WeatherData
import com.abgp.colorcloud.ui.theme.ThemeSetter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class WeatherFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentWeatherBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val bnd get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val root: View = bnd.root

        with(mainViewModel) {
            weatherData.observe(viewLifecycleOwner, { it ->
                it?.apply {
                    setWeatherDataUI(bnd, it)
                }
            })
            theme.observe(viewLifecycleOwner, {
                val themeSetter = ThemeSetter(bnd.root)
                themeSetter.setTheme(it)
            })
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setWeatherDataUI(bnd: FragmentWeatherBinding, weatherData: WeatherData) {
        with(weatherData) {
            with(bnd) {
                pbMain.visibility = GONE
                rlWeatherFragment.visibility = VISIBLE
                tvTemperature.text = main.temp.toString()

                val sfSun = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val sfUp = SimpleDateFormat("dd MMM yyy, hh:mm a", Locale.getDefault())
                sfSun.timeZone = TimeZone.getTimeZone("GMT+8")
                sfUp.timeZone = TimeZone.getTimeZone("GMT+8")

                val sunset = sys.sunset.toLong()
                val sunrise = sys.sunrise.toLong()
                val updatedAt = dt.toLong()

                tvUpdatedAt.text = sfUp.format(Date(updatedAt*1000))
                tvPressure.text = main.pressure.toString() + " hPa"
                tvHumidity.text = main.humidity.toString() + " %"
                tvStatus.text = weather[0].description.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else toString()
                }
                tvTimeSunrise.text =  sfSun.format(Date(sunrise*1000))
                tvTimeSunset.text = sfSun.format(Date(sunset*1000))
                tvWind.text = wind.speed.toString() + " km/h"
                tvAddress.text = name + ", " + sys.country
                tvTemperature.text = round(main.temp).toInt().toString() + "°c"
                tvMinTemp.text = "Min Temp: " + String.format("%.1f", main.temp_min).toString() + "°c"
                tvMaxTemp.text = "Max Temp: " + String.format("%.1f", main.temp_max).toString() + "°c"
            }
        }
    }
}