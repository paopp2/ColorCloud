package com.abgp.colorcloud.models

import com.abgp.colorcloud.ui.theme.ColorTheme

class User(val name: String, var password: String, var colorTheme : ColorTheme) {

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun updateColorTheme(newColorTheme: ColorTheme) {
        colorTheme = newColorTheme
    }
}
