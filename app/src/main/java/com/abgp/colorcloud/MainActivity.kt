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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
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
import kotlinx.android.synthetic.main.fragment_weather.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bnd: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefServices = SharedPrefServices(this)
        val mainViewModel : MainViewModel by viewModels()

        val currentUser = sharedPrefServices.getCurrentUser()
        if(currentUser == null) {
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        
        /* EXAMPLE
        testbutton.setOnClickListener {
            Log.d("Debug:",checkLocPermission().toString())
            Log.d("Debug:",isLocEnabled().toString())
            requestLocPermission()
            fusedLocationProviderClient.lastLocation.addOnSuccessListener{location: Location? ->
                 // return or set the latitufe or long here
                //location?.latitude
                //location?.longitude
                Log.d("Latitude: ",location?.latitude.toString())
            Log.d("Longitude: ",location?.longitude.toString())
             }
            getLastLoc()
        }*/


        bnd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bnd.root)
        setSupportActionBar(bnd.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = bnd.drawerLayout
        val navView: NavigationView = bnd.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        mainViewModel.theme.value = currentUser?.colorTheme
    }
    private fun getLastLoc(){
        if(checkLocPermission()){
            if(isLocEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->

                    var location: Location? = task.result
                    if(location == null){

                        var locationRequest = LocationRequest.create().apply {
                            interval = 100
                            fastestInterval = 0
                            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                            maxWaitTime= 100
                            numUpdates = 1
                        }

                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

                        fusedLocationProviderClient!!.requestLocationUpdates(
                            locationRequest,locationCallback, Looper.myLooper()
                        )

                    }else{
                        Log.d("Your Current Location:" ,"Your Location:"+ location.longitude + " " + location.latitude)
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
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Location (Callback):","Last last location: "+ lastLocation.longitude.toString() + lastLocation.latitude.toString())
        }
    }

    private fun checkLocPermission(): Boolean {
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false
    }
    private fun requestLocPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            1421
        )
    }
    private fun isLocEnabled():Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1421){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLoc()
                Log.d("Permission", "You have permission")
            }else{
                Log.d("Permission", "Permission Denied")
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