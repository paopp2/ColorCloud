package com.abgp.colorcloud.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.abgp.colorcloud.MainActivity
import com.abgp.colorcloud.databinding.ActivityLoginBinding
import com.abgp.colorcloud.databinding.AlertDialogCustomBinding
import com.abgp.colorcloud.models.User
import com.abgp.colorcloud.services.SharedPrefServices

class LoginActivity : AppCompatActivity() {
    private lateinit var bnd : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bnd.root)
        val sharedPrefServices = SharedPrefServices(this)
        val users = sharedPrefServices.getAllUsers()

        val userListAdapter = UserListAdapter(this, users) { position ->
            val selectedUser = users[position]
            verifyPassword(selectedUser)
        }

        bnd.rvUsersList.adapter = userListAdapter
        bnd.btRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verifyPassword(user: User) {
        val bnd = AlertDialogCustomBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this).setView(bnd.root)
        val sharedPrefServices = SharedPrefServices(this)

        val alertDialog = builder.show()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        bnd.btEnter.setOnClickListener {
            val enteredPass = bnd.etPassword.text.toString()
            if(enteredPass == user.password) {
                Toast.makeText(this, "Welcome ${user.name}", Toast.LENGTH_SHORT).show()
                sharedPrefServices.setCurrentUser(user.name)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show()
            }
            alertDialog.dismiss()
        }
    }
}