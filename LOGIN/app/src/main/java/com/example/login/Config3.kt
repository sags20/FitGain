package com.example.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.login.databinding.FragmentConfig3Binding

/**
 * A simple [Fragment] subclass.
 */
class Config3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentConfig3Binding>(
            inflater, R.layout.fragment_config3, container, false
        )
        return binding.root
    }

}
