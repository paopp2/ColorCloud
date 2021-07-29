package com.abgp.colorcloud.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abgp.colorcloud.R
import com.abgp.colorcloud.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var bnd : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_register)
    }
}