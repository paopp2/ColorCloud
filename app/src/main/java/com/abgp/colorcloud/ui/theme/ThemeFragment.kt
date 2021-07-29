package com.abgp.colorcloud.ui.theme

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.abgp.colorcloud.R
import com.abgp.colorcloud.databinding.FragmentThemeBinding

class ThemeFragment : Fragment() {

    private lateinit var themeViewModel: ThemeViewModel
    private var _binding: FragmentThemeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val bnd get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        themeViewModel =
            ViewModelProvider(this).get(ThemeViewModel::class.java)

        _binding = FragmentThemeBinding.inflate(inflater, container, false)
        val root: View = bnd.root

        val textView: TextView = bnd.textView2
        themeViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        bnd.rgThemes.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rbRoseanna -> changeTheme("#F17B41", "#E05BA2", "#CD4BC9")
                R.id.rbVoid -> changeTheme("#274B74", "#8233C5", "#E963FD")
                R.id.rbMauve -> changeTheme("#C3ADC3", "#BD959F", "#EEB5B5")
                R.id.rbGlacier -> changeTheme("#8929AD", "#436AAC", "#43B7B8")
                R.id.rbBorealis -> changeTheme("#276174", "#33C58E", "#63FD88")
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeTheme(firstColor: String, secondColor: String, thirdColor: String){
        val gradient1Drawable = GradientDrawable(
            GradientDrawable.Orientation.TR_BL,
            intArrayOf(
                Color.parseColor(firstColor),
                Color.parseColor(secondColor),
                Color.parseColor(thirdColor))
        )
        val gradient2Drawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor(secondColor),
                Color.parseColor(thirdColor),
                Color.parseColor(firstColor))
        )
        val gradient3Drawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor(thirdColor),
                Color.parseColor(firstColor),
                Color.parseColor(secondColor))
        )

        val animaDrawable = AnimationDrawable()
        animaDrawable.addFrame(gradient1Drawable, 2000)
        animaDrawable.addFrame(gradient2Drawable, 2000)
        animaDrawable.addFrame(gradient3Drawable, 2000)

        bnd.clLayout.background = animaDrawable

        animaDrawable.setEnterFadeDuration(10)
        animaDrawable.setExitFadeDuration(2000)
        animaDrawable.start()
    }


}