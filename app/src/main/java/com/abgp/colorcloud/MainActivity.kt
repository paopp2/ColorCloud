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
import androidx.activity.viewModels
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
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView

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
        val mainViewModel : MainViewModel by viewModels()

        bnd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bnd.root)

        val drawerLayout: DrawerLayout = bnd.drawerLayout
        val navView: NavigationView = bnd.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )

        setSupportActionBar(bnd.appBarMain.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if(sharedPrefServices.getCurrentUser() == null) {
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            Log.d(TAG,"CheckLocPermission: ${checkLocPermission()}")
            Log.d(TAG,"IsLocEnabled: ${isLocEnabled()}")

            getLastLoc()
        }
    }

    private fun getLastLoc(){
        if(checkLocPermission()){
            if(isLocEnabled()){
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
                                locationRequest,locationCallback, this
                            )
                        }

                    }else{
                        Log.d(TAG,"Your Location:"+ location.longitude + " " + location.latitude)
                        mainViewModel.geoData.value = location
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
            }
        }else{
            requestLocPermission()
        }
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            Log.d(TAG,"Last last location: "+ lastLocation.longitude.toString() + lastLocation.latitude.toString())
            mainViewModel.geoData.value = lastLocation
        }
    }

    private fun checkLocPermission(): Boolean {
        return(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    private fun requestLocPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    private fun isLocEnabled():Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLoc()
                Log.d(TAG, "Location Permission Granted")
            }else{
                Log.d(TAG, "Location Permission Denied")
            }
        }
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
}