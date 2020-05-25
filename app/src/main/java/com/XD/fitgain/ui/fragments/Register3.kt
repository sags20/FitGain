package com.XD.fitgain.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentRegister3Binding

class Register3 : Fragment() {

    private lateinit var binding: FragmentRegister3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegister3Binding.inflate(inflater, container, false)

        binding.btnFinalizar.setOnClickListener {
            it.findNavController().navigate(R.id.action_register3_to_completeScreen)
        }

        binding.maleSelect.setOnClickListener {
            binding.maleSelectedIndicator.drawable.setTint(getColor(requireActivity(),R.color.light_purple))
            binding.femaleSelectedIndicator.drawable.setTint(getColor(requireActivity(),R.color.light_gray))
        }

        binding.femaleSelect.setOnClickListener {
            binding.femaleSelectedIndicator.drawable.setTint(getColor(requireActivity(),R.color.light_purple))
            binding.maleSelectedIndicator.drawable.setTint(getColor(requireActivity(),R.color.light_gray))
        }

        return binding.root
    }
}
