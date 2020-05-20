package com.example.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.login.databinding.FragmentRegister3Binding

/**
 * A simple [Fragment] subclass.
 */
class Register3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRegister3Binding>(
            inflater, R.layout.fragment_register3, container, false
        )
        return binding.root
    }

}
