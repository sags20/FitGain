package com.XD.fitgain.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentCompleteScreenBinding
import com.XD.fitgain.ui.NavigationContainerHome

class CompleteScreen : Fragment() {

    private lateinit var binding: FragmentCompleteScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompleteScreenBinding.inflate(inflater, container, false)

        binding.buttonGetStarted.setOnClickListener {
            startActivity(Intent(activity, NavigationContainerHome::class.java))
        }

        return binding.root
    }
}
