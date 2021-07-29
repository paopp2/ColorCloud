package com.abgp.colorcloud.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abgp.colorcloud.databinding.ActivityLoginBinding
import com.abgp.colorcloud.services.SharedPrefServices

class LoginActivity : AppCompatActivity() {
    private lateinit var bnd : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bnd.root)
        val sharedPrefServices = SharedPrefServices(this)

        val users = sharedPrefServices.getAllUsers()
        var userString = ""
        for(user in users) {
            userString += "\n${user.name} ${user.colorTheme}"
        }
        bnd.tvTemporary.text = userString

        bnd.btRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}