package com.XD.fitgain.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentDiscoverBinding
import com.XD.fitgain.ui.DiscoverSelected
import com.XD.fitgain.ui.NavigationContainerHome

/**
 * A simple [Fragment] subclass.
 */
class Discover : Fragment() {

    private lateinit var binding: FragmentDiscoverBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        binding.cardviewRestaurante.setOnClickListener {
            startActivityForResult(
                Intent(
                    requireActivity(),
                    DiscoverSelected::class.java
                ), 777
            )
        }



        return binding.root
    }

}
