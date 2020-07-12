package com.XD.fitgain.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.XD.fitgain.databinding.FragmentConfig1Binding

class Config1 : Fragment() {

    private lateinit var binding: FragmentConfig1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfig1Binding.inflate(inflater, container, false)

        return binding.root
    }


}
