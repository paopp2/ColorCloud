package com.abgp.colorcloud.ui.theme

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.GradientDrawable
import android.view.View

enum class ColorTheme {
    ROSEANNA,
    VOID,
    DUSK,
    GLACIER,
    BOREALIS,
}

class ThemeSetter(private val view: View) {

    fun setTheme(colorTheme:ColorTheme) {
        when(colorTheme) {
            ColorTheme.ROSEANNA -> setFromColors("#F17B41", "#E05BA2", "#CD4BC9")
            ColorTheme.VOID -> setFromColors("#274B74", "#8233C5", "#E963FD")
            ColorTheme.DUSK -> setFromColors("#74276C", "#C53364", "#FD8263")
            ColorTheme.GLACIER ->setFromColors("#8929AD", "#436AAC", "#43B7B8")
            ColorTheme.BOREALIS ->setFromColors("#276174", "#33C58E", "#63FD88")
        }
    }

    private fun setFromColors(firstColor: String, secondColor: String, thirdColor: String){
        val gradient1Drawable = setDrawable(1, firstColor, secondColor, thirdColor)
        val gradient2Drawable = setDrawable(2, firstColor, secondColor, thirdColor)
        val gradient3Drawable = setDrawable(3, firstColor, secondColor, thirdColor)

        val animaDrawable = AnimationDrawable()
        animaDrawable.addFrame(gradient1Drawable, 2000)
        animaDrawable.addFrame(gradient2Drawable, 2000)
        animaDrawable.addFrame(gradient3Drawable, 2000)

        view.background = animaDrawable

        animaDrawable.setEnterFadeDuration(10)
        animaDrawable.setExitFadeDuration(2000)
        animaDrawable.start()
    }

    private fun setDrawable(order: Int, firstColor: String, secondColor: String, thirdColor: String): GradientDrawable {
        return GradientDrawable(
            GradientDrawable.Orientation.TR_BL,
            when(order){
                1 -> intArrayOf(
                    Color.parseColor(firstColor),
                    Color.parseColor(secondColor),
                    Color.parseColor(thirdColor))
                2 -> intArrayOf(
                    Color.parseColor(secondColor),
                    Color.parseColor(thirdColor),
                    Color.parseColor(firstColor))
                3 -> intArrayOf(
                    Color.parseColor(thirdColor),
                    Color.parseColor(firstColor),
                    Color.parseColor(secondColor))
                else -> null
            }
        )
    }
}