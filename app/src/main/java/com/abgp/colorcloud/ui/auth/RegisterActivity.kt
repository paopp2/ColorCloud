package com.abgp.colorcloud.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.abgp.colorcloud.MainActivity
import com.abgp.colorcloud.R
import com.abgp.colorcloud.databinding.ActivityRegisterBinding
import com.abgp.colorcloud.models.User
import com.abgp.colorcloud.services.SharedPrefServices

class RegisterActivity : AppCompatActivity() {
    private lateinit var bnd : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bnd.root)

        bnd.btnRegister.setOnClickListener {
            val username = bnd.etUsername.text.toString()
            val pass = bnd.etPassword.text.toString()
            val confPass = bnd.etConfPassword.text.toString()
            if(pass == confPass) {
                val sharedPrefServices = SharedPrefServices(this)
                val newUser = User(username, pass, "Blue")
                with(sharedPrefServices) {
                    addUser(newUser)
                    setCurrentUser(newUser.name)
                }
                Toast.makeText(this, "Welcome ${newUser.name}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}