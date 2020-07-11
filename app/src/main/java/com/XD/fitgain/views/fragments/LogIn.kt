package com.XD.fitgain.views.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentLogInBinding
import com.XD.fitgain.views.NavigationContainerHome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tapadoo.alerter.Alerter

class LogIn : Fragment() {
    private lateinit var binding: FragmentLogInBinding

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater, container, false)

        binding.btnBackScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_logIn_to_logOut)
        }

        binding.btnForgotPassword.setOnClickListener {
            it.findNavController().navigate(R.id.action_logIn_to_forgotPassword)
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etCorreo.text.isEmpty() || binding.etPassword.text.isEmpty()) {
                Alerter.create(activity)
                    .setText("Por favor complete los campos Email y Contraseña.")
                    .setBackgroundColorRes(R.color.alert_default_error_background)
                    .show()
            } else {
                performLogin()
            }

        }

        return binding.root
    }

    private fun performLogin() {
        auth.signInWithEmailAndPassword(
            binding.etCorreo.text.toString(),
            binding.etPassword.text.toString()
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("LOGIN_DEBUG", "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Alerter.create(activity)
                        .setText("Credenciales incorrectas.")
                        .setBackgroundColorRes(R.color.alert_default_error_background)
                        .show()
                    updateUI(null)
                }
            }

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(activity, NavigationContainerHome::class.java))
        }

    }


}
