package com.XD.fitgain.ui.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentLogInBinding
import com.XD.fitgain.ui.NavigationContainerHome
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogIn : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance();

        binding.btnBackScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_logIn_to_logOut)
        }

        binding.btnForgotPassword.setOnClickListener {
            it.findNavController().navigate(R.id.action_logIn_to_forgotPassword)
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(activity, NavigationContainerHome::class.java))
            mAuth!!.signInWithEmailAndPassword(et_correo.text.toString(), et_password.text.toString())
                .addOnCompleteListener(this,
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                        }

                    })

        }

        return binding.root
    }


}

private fun Any.addOnCompleteListener(logIn: LogIn, onCompleteListener: OnCompleteListener<AuthResult?>) {

}
