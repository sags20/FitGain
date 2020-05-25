package com.example.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.login.databinding.FragmentLoggedOutBinding

/**
 * A simple [Fragment] subclass.
 */
class LoggedOut : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentLoggedOutBinding>(
            inflater, R.layout.fragment_logged_out, container, false
        )
        binding.buttonSignUp.setOnClickListener{ view: View ->
            view.findNavController()
                .navigate(R.id.action_loggedOut_to_signUp)

        }

        binding.logIn.setOnClickListener{ view: View ->
            view.findNavController()
                .navigate(R.id.action_loggedOut_to_login)
        }

        return binding.root
    }

}
