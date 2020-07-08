package com.XD.fitgain.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentForgotPasswordBinding

/**
 * A simple [Fragment] subclass.
 */
class ForgotPassword : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        binding.btnBackScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_forgotPassword_to_logIn)
        }

        return binding.root
    }

}
