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
import com.abgp.colorcloud.MainViewModel
import com.abgp.colorcloud.R
import com.abgp.colorcloud.databinding.FragmentThemeBinding

class ThemeFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentThemeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val bnd get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        _binding = FragmentThemeBinding.inflate(inflater, container, false)
        val root: View = bnd.root

        mainViewModel.theme.observe(viewLifecycleOwner, {
            when(it) {
                ColorTheme.VOID -> bnd.rbVoid.isChecked = true
                ColorTheme.DUSK -> bnd.rbDusk.isChecked = true
                ColorTheme.GLACIER -> bnd.rbGlacier.isChecked = true
                ColorTheme.BOREALIS -> bnd.rbBorealis.isChecked = true
                else -> bnd.rbRoseanna.isChecked = true
            }
            val themeSetter = ThemeSetter(bnd.root)
            themeSetter.setTheme(it)
        })

        bnd.rgThemes.setOnCheckedChangeListener { _, checkedId ->
            mainViewModel.theme.value = when (checkedId) {
                R.id.rbRoseanna -> ColorTheme.ROSEANNA
                R.id.rbVoid -> ColorTheme.VOID
                R.id.rbDusk -> ColorTheme.DUSK
                R.id.rbGlacier -> ColorTheme.GLACIER
                R.id.rbBorealis -> ColorTheme.BOREALIS
                else -> null
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}