package com.abgp.colorcloud.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.abgp.colorcloud.MainViewModel
import com.abgp.colorcloud.databinding.FragmentWeatherBinding
import com.abgp.colorcloud.models.WeatherData
import com.abgp.colorcloud.ui.theme.ThemeSetter
import com.abgp.colorcloud.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.math.round

private const val TAG = "WeatherFragment"

class WeatherFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentWeatherBinding? = null

    private val bnd get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val root: View = bnd.root

        if(Utils.isInternetAvailable(requireActivity())) {
            with(mainViewModel) {
                weatherData.observe(viewLifecycleOwner, {
                    it?.apply {setWeatherDataUI(bnd, it)}
                })
                theme.observe(viewLifecycleOwner, {
                    val themeSetter = ThemeSetter(root)
                    themeSetter.setTheme(it)
                })
            }
        } else {
            bnd.pbMain.visibility = GONE
            bnd.rlWeatherFragment.visibility = VISIBLE
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(bnd.pbMain.visibility == VISIBLE) {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(5000)
                if(mainViewModel.weatherData.value == null) {
                    toast("Timeout reached, using preset values instead")
                    bnd.pbMain.visibility = GONE
                    bnd.rlWeatherFragment.visibility = VISIBLE
                }
            }
        }
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

    private fun toast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }
}