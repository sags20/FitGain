package com.XD.fitgain.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentRegister1Binding

class Register1 : Fragment() {

    private lateinit var binding: FragmentRegister1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegister1Binding.inflate(inflater, container, false)

        binding.btnBackScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_register1_to_logOut)
        }

        binding.btnContinuar.setOnClickListener {
            it.findNavController().navigate(R.id.action_register1_to_register2)
        }

        return binding.root
    }

}
