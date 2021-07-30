package com.abgp.colorcloud.ui.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abgp.colorcloud.databinding.FragmentPasswordBinding
import com.abgp.colorcloud.services.SharedPrefServices

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
        val sharedPrefServices = SharedPrefServices(requireActivity())
        val currentUser = sharedPrefServices.getCurrentUser()
        passwordViewModel =
            ViewModelProvider(this).get(PasswordViewModel::class.java)

        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        val root: View = bnd.root

        passwordViewModel.text.observe(viewLifecycleOwner, Observer {})

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
                        Toast.makeText(requireActivity(), "Password updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireActivity(), "Entered passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireActivity(), "Wrong password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}