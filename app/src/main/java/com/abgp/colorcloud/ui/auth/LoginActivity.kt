package com.abgp.colorcloud.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abgp.colorcloud.R
import com.abgp.colorcloud.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var bnd : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bnd.root)

        bnd.btRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}