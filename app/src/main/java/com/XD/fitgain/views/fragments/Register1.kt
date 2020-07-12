package com.XD.fitgain.views.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentRegister1Binding
import com.XD.fitgain.model.User

class Register1 : Fragment() {

    private lateinit var binding: FragmentRegister1Binding
    private var isPasswordValid: Boolean = false
    private var isEmailCorrectValid: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegister1Binding.inflate(inflater, container, false)

        binding.btnBackScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_register1_to_logOut)
        }

        binding.btnContinuar.setOnClickListener {
            if (isPasswordValid && isEmailCorrectValid) {

                //Create user object
                var nuevoUsuario = User()
                nuevoUsuario.email = binding.etCorreo.text.toString()

                //Passing the user to the next fragment
                var action = Register1Directions.actionRegister1ToRegister2(nuevoUsuario,binding.etPassword.text.toString())
                it.findNavController().navigate(action)

            } else {
                binding.tvIsValidEmail.setTextColor(Color.parseColor("#FEAAAA"))
                binding.tv8characterCheck.setTextColor(Color.parseColor("#FEAAAA"))
            }
        }

        binding.etCorreo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val email = binding.etCorreo.text.toString().trim()
                Log.d("REGISTER_DEBUG", "EMAIL CHANGED $email")
                isEmailCorrectValid = if (!isEmailValid(email)) {
                    //Is not valid email
                    binding.tvIsValidEmail.setTextColor(Color.parseColor("#FEAAAA"))
                    false
                } else {
                    binding.tvIsValidEmail.setTextColor(Color.parseColor("#29CA00"))
                    true
                }
            }

        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val pass = binding.etPassword.text.toString()
                Log.d("REGISTER_DEBUG", "PASS CHANGED $pass")

                isPasswordValid = if (pass.length < 8) {
                    binding.tv8characterCheck.setTextColor(Color.parseColor("#FEAAAA"))
                    false

                } else {
                    binding.tv8characterCheck.setTextColor(Color.parseColor("#29CA00"))
                    true
                }
            }

        })

        return binding.root
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
