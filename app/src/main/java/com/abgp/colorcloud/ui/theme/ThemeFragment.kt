package com.abgp.colorcloud.ui.theme

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
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
                R.id.rbRoseanna -> changeTheme("#ffafbd", "#ffc3a0")
                R.id.rbPurpleLove -> changeTheme("#cc2b5e", "#753a88")
                R.id.rbMauve -> changeTheme("#42275a", "#734b6d")
                R.id.rbSexyBlue -> changeTheme("#2193b0", "#6dd5ed")
                R.id.rbFrost -> changeTheme("#000428", "#004e92")
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeTheme(firstColor: String, secondColor: String){
        val gradient1Drawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor(firstColor),
                Color.parseColor(secondColor),
                Color.parseColor(firstColor))
        )
        val gradient2Drawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor(secondColor),
                Color.parseColor(firstColor),
                Color.parseColor(firstColor))
        )
        val gradient3Drawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor(firstColor),
                Color.parseColor(firstColor),
                Color.parseColor(secondColor))
        )

        // mora ni syag maghimo kag gradient animation nga drawable. pero programmatically.
        val animaDrawable = AnimationDrawable()
        animaDrawable.addFrame(gradient1Drawable, 3000)
        animaDrawable.addFrame(gradient2Drawable, 3000)
        animaDrawable.addFrame(gradient3Drawable, 3000)


        // diri dapit iya iset ang animation as background
        bnd.clLayout.background = animaDrawable              // set background sa root_layout
        animaDrawable.setEnterFadeDuration(10)
        animaDrawable.setExitFadeDuration(3000)
        animaDrawable.start()
    }


}