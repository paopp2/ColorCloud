package com.abgp.colorcloud.ui.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.abgp.colorcloud.MainViewModel
import com.abgp.colorcloud.databinding.FragmentPasswordBinding
import com.abgp.colorcloud.services.SharedPrefServices
import com.abgp.colorcloud.ui.theme.ThemeSetter

class PasswordFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentPasswordBinding? = null

    private val bnd get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPrefServices = SharedPrefServices(requireActivity())
        val currentUser = sharedPrefServices.getCurrentUser()
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        val root: View = bnd.root

        mainViewModel.theme.observe(viewLifecycleOwner, {
            val themeSetter = ThemeSetter(root)
            themeSetter.setTheme(it)
        })

        bnd.btnChangePassword.setOnClickListener {
            val currentPass = bnd.etCurrentPassword.text.toString()
            val newPass = bnd.etNewPassword.text.toString()
            val confNewPass = bnd.etConfNewPassword.text.toString()
            currentUser?.apply {
                if(password == currentPass) {
                    if(newPass == confNewPass) {
                        updatePassword(newPass)
                        sharedPrefServices.updateUser(this)
                        bnd.etConfNewPassword.text?.clear()
                        bnd.etNewPassword.text?.clear()
                        bnd.etCurrentPassword.text?.clear()
                        toast("Password updated")
                    } else {
                        toast("Entered passwords do not match")
                    }
                } else {
                    toast("Wrong password")
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }
}