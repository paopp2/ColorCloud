package com.abgp.colorcloud

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.abgp.colorcloud.databinding.ActivityMainBinding
import com.abgp.colorcloud.services.SharedPrefServices
import com.abgp.colorcloud.ui.auth.LoginActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bnd: ActivityMainBinding

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