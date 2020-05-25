package com.XD.fitgain.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentCompleteScreenBinding

class CompleteScreen : Fragment() {

    private lateinit var binding: FragmentCompleteScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompleteScreenBinding.inflate(inflater, container, false)

        return binding.root
    }
}
