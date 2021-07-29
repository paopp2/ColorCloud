package com.abgp.colorcloud.services

import android.content.Context
import android.util.Log
import com.abgp.colorcloud.models.User
import com.google.gson.Gson

class SharedPrefServices(context: Context) {
    private val allUsersSharedPref =  context.getSharedPreferences("AllUsersSharedPref", Context.MODE_PRIVATE)
    private val currentUserSharedPref =  context.getSharedPreferences("CurrentUserSharedPref", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Set as current user
    fun setCurrentUser(name: String) {
        val user = getUser(name) ?: throw Exception("User doesn't exist")
        with(currentUserSharedPref.edit()) {
            val json = gson.toJson(user)
            putString("CurrentUser", json)
            apply()
        }
    }

    // Gets current user
    fun getCurrentUser() : User? {
        return if (currentUserSharedPref.contains("CurrentUser")) {
            val json = currentUserSharedPref.getString("CurrentUser", null)
            gson.fromJson(json, User::class.java)
        } else null
    }

    // Removes the current user
    fun removeCurrentUser() {
        with(currentUserSharedPref.edit()) {
            remove("CurrentUser")
            commit()
        }
    }

    // Saves this User to local storage
    fun addUser(user: User) {
        val json = gson.toJson(user)
        if (allUsersSharedPref.contains(user.name)) {
            throw Exception("Username is taken")
        } else {
            with(allUsersSharedPref.edit()) {
                putString(user.name, json)
                apply()
            }
        }
    }

    // Updates an existing user
    fun updateUser(name: String) {
        val user = getUser(name) ?: throw Exception("User doesn't exist")
        val json = gson.toJson(user)
        with(allUsersSharedPref.edit()) {
            putString(user.name, json)
            apply()
        }
    }

    // Returns a list of all stored Users
    fun getAllUsers(): List<User> {
        val all = allUsersSharedPref.all
        val users = arrayListOf<User>()
        for ((_, value) in all) {
            val user = gson.fromJson(value as String, User::class.java)
            users.add(user)
        }
        return users.sortedBy { it.name }
    }

    // Retrieves a user from SharedPreferences
    private fun getUser(name: String): User? {
        return if (allUsersSharedPref.contains(name)) {
            val json = allUsersSharedPref.getString(name, "NULL")
            gson.fromJson(json, User::class.java)
        } else {
            Log.d("ERROR", "User not found")
            null
        }
    }

    // Clears both 'allUsers' and 'currentUser' sharedPreferences
    fun resetLocalData() {
        with(allUsersSharedPref.edit()) {
            clear()
            commit()
        }
        with(currentUserSharedPref.edit()) {
            clear()
            commit()
        }
    }
}
