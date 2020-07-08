package com.XD.fitgain.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentRegister1Binding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register1.*

class Register1 : Fragment() {

    private lateinit var binding: FragmentRegister1Binding
    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegister1Binding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance();

        binding.btnBackScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_register1_to_logOut)
        }

        binding.btnContinuar.setOnClickListener {
            it.findNavController().navigate(R.id.action_register1_to_register2)
            mAuth!!.createUserWithEmailAndPassword(et_correo.text.toString(), et_password.text.toString())
                .addOnCompleteListener(this,
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        }

                    })
        }

        return binding.root
    }

}

private fun Any.addOnCompleteListener(register1: Register1, onCompleteListener: OnCompleteListener<AuthResult?>) {

}
