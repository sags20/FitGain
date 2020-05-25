package com.example.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.login.R
import com.example.login.databinding.FragmentCompleteScreenBinding

/**
 * A simple [Fragment] subclass.
 */
class CompleteScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCompleteScreenBinding>(
            inflater, R.layout.fragment_complete_screen, container, false
        )
        return binding.root
    }

}
