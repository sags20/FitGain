package com.XD.fitgain.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentLogOutBinding
import com.XD.fitgain.views.NavigationContainerHome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LogOut : Fragment() {

    private lateinit var binding: FragmentLogOutBinding

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogOutBinding.inflate(inflater, container, false)

        binding.alreadyHave.setOnClickListener {
            it.findNavController().navigate(R.id.action_logOut_to_logIn)
        }

        binding.tvLogin.setOnClickListener {
            it.findNavController().navigate(R.id.action_logOut_to_logIn)
        }

        binding.buttonSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.action_logOut_to_register1)
        }



        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //auth.signOut()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(activity, NavigationContainerHome::class.java))
        }

    }

}
