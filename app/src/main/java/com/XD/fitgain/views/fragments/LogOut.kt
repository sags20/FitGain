package com.XD.fitgain.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentLogOutBinding

class LogOut : Fragment() {

    private lateinit var binding: FragmentLogOutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogOutBinding.inflate(inflater, container, false)

        binding.alreadyHave.setOnClickListener {
            it.findNavController().navigate(R.id.action_logOut_to_logIn)
        }

        binding.tvLogin.setOnClickListener {
            it.findNavController().navigate(R.id.action_logOut_to_logIn)
        }

        binding.buttonSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.action_logOut_to_register1)
        }



        return binding.root
    }

}
