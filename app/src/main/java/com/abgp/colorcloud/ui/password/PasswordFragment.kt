package com.abgp.colorcloud.ui.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.abgp.colorcloud.databinding.FragmentPasswordBinding

class PasswordFragment : Fragment() {

    private lateinit var passwordViewModel: PasswordViewModel
    private var _binding: FragmentPasswordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val bnd get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        passwordViewModel =
            ViewModelProvider(this).get(PasswordViewModel::class.java)

        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        val root: View = bnd.root

        val textView: TextView = bnd.textSlideshow
        passwordViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}