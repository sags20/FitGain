package com.XD.fitgain.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentLogInBinding
import com.XD.fitgain.views.NavigationContainerHome

class LogIn : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater, container, false)

        binding.btnBackScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_logIn_to_logOut)
        }

        binding.btnForgotPassword.setOnClickListener {
            it.findNavController().navigate(R.id.action_logIn_to_forgotPassword)
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(activity, NavigationContainerHome::class.java))
        }

        return binding.root
    }


}
