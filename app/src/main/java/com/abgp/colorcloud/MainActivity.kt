package com.abgp.colorcloud

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.abgp.colorcloud.databinding.ActivityMainBinding
import com.abgp.colorcloud.services.SharedPrefServices
import com.abgp.colorcloud.ui.auth.LoginActivity
import com.abgp.colorcloud.utils.Utils
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header_main.view.*

private const val TAG = "MainActivity"
private const val REQUEST_CODE = 1421

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bnd: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefServices = SharedPrefServices(this)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        bnd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bnd.root)

        val drawerLayout: DrawerLayout = bnd.drawerLayout
        val navView: NavigationView = bnd.navView
        val headerView = navView.inflateHeaderView(R.layout.nav_header_main)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )

        setSupportActionBar(bnd.appBarMain.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val currentUser = sharedPrefServices.getCurrentUser()
        if(currentUser == null) {
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            headerView.tvNameNavDrawer.text = currentUser.name
            mainViewModel.theme.value = currentUser.colorTheme
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            getLastLoc()
        }
    }

    private fun getLastLoc(){
        if(!Utils.isInternetAvailable(this)) {
            Toast.makeText(
                this,
                "Internet is turned off. Using preset values instead",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        Log.d(TAG,"CheckLocPermission: ${checkLocPermission()}")
        if(checkLocPermission()){
            Log.d(TAG,"IsLocEnabled: ${locationEnabled()}")
            if(locationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    val location: Location? = task.result
                    if(location == null){
                        val locationRequest = LocationRequest.create().apply {
                            interval = 100
                            fastestInterval = 0
                            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                            maxWaitTime= 100
                            numUpdates = 1
                        }
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                        Looper.myLooper()?.apply {
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest, locationCallback, this
                            )
                        }
                    } else {
                        Log.d(TAG,"Your Location: "+ location.longitude + " " + location.latitude)
                        mainViewModel.geoData.value = location
                    }
                }
            } else {
                Toast.makeText(this,"Device location turned off, using a preset location", Toast.LENGTH_SHORT).show()
                usePresetLocation()
            }
        } else {
            requestLocPermission()
        }
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            Log.d(TAG,"Location callback: "+ lastLocation.longitude.toString() + lastLocation.latitude.toString())
            mainViewModel.geoData.value = lastLocation
        }
    }

    private fun checkLocPermission(): Boolean {
        return(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    private fun requestLocPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_CODE
        )
    }

    private fun locationEnabled():Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "Location Permission Granted")
                getLastLoc()
            }else{
                Log.d(TAG, "Location Permission Denied")
                Toast.makeText(this, "Permission denied, using preset location", Toast.LENGTH_SHORT).show()
                usePresetLocation()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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

    private fun usePresetLocation() {
        val presetLocation = Location("dummy_provider")
        presetLocation.longitude = 123.88557169849281
        presetLocation.latitude = 10.315413375969552
        mainViewModel.geoData.value = presetLocation
    }
}