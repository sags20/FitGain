package com.XD.fitgain.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.tapadoo.alerter.Alerter

/**
 * A simple [Fragment] subclass.
 */
class ForgotPassword : Fragment() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var binding: FragmentForgotPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        binding.btnBackScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_forgotPassword_to_logIn)
        }

        binding.btnResetPassword.setOnClickListener {
            if (binding.etEmailResetPassword.text.isEmpty()) {
                Alerter.create(activity)
                    .setText("Por favor complete los campos Email.")
                    .setBackgroundColorRes(R.color.alert_default_error_background)
                    .show()
            } else {
                if (isEmailValid(binding.etEmailResetPassword.text.toString())) {
                    performResetPassword()
                }else{
                    Alerter.create(activity)
                        .setText("Formato de correo incorrecto.")
                        .setBackgroundColorRes(R.color.alert_default_error_background)
                        .show()
                }
            }
        }

        return binding.root
    }

    private fun performResetPassword() {
        auth.sendPasswordResetEmail(binding.etEmailResetPassword.text.toString())
        Alerter.create(activity)
            .setText("Se ha enviado un correo con isntrucciones.")
            .setBackgroundColorRes(R.color.alerter_default_success_background)
            .show()
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
