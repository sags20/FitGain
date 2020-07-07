package com.XD.fitgain.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentRegister2Binding

class Register2 : Fragment() {
    private lateinit var binding: FragmentRegister2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegister2Binding.inflate(inflater, container, false)

        binding.btnContinuar.setOnClickListener {
            it.findNavController().navigate(R.id.action_register2_to_register3)
        }

        binding.btnBackScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_register2_to_register1)
        }

        return binding.root
    }


}
