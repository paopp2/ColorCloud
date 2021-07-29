package com.abgp.colorcloud.models

class User(val name: String, var password: String, var colorTheme : String) {

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun updateColorTheme(newColorTheme: String) {
        colorTheme = newColorTheme
    }
}
