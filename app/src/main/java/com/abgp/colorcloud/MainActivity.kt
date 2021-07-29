package com.abgp.colorcloud

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.view.View.GONE
import android.widget.ProgressBar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.abgp.colorcloud.databinding.ActivityMainBinding
import com.abgp.colorcloud.services.SharedPrefServices
import com.abgp.colorcloud.services.WeatherServices
import com.abgp.colorcloud.ui.auth.LoginActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bnd: ActivityMainBinding

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefServices = SharedPrefServices(this)
        val weatherServices = WeatherServices()

        if(sharedPrefServices.getCurrentUser() == null) {
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        bnd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bnd.root)
        setSupportActionBar(bnd.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = bnd.drawerLayout
        val navView: NavigationView = bnd.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        weatherServices.setWeatherData {
            val pbMain = findViewById<ProgressBar>(R.id.pbMain)
            val tvTemperature = findViewById<TextView>(R.id.tvTemperature)
            val tvMinTemp = findViewById<TextView>(R.id.tvMinTemp)
            val tvMaxTemp = findViewById<TextView>(R.id.tvMaxTemp)
            pbMain.visibility = GONE

            tvTemperature.text = it.main.temp.toString()
            tvMinTemp.text = it.main.temp_min.toString()
            tvMaxTemp.text = it.main.temp_max.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_logout -> {
                val sharedPrefServices = SharedPrefServices(this)
                sharedPrefServices.removeCurrentUser()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}