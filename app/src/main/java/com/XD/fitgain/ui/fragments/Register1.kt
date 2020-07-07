package com.XD.fitgain.ui.fragments

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
import com.XD.fitgain.databinding.FragmentRegister1Binding
import com.XD.fitgain.ui.NavigationContainerHome
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider

class Register1 : Fragment() {

    private lateinit var binding: FragmentRegister1Binding

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegister1Binding.inflate(inflater, container, false)

        binding.btnBackScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_register1_to_logOut)
        }

        binding.btnContinuar.setOnClickListener {
            it.findNavController().navigate(R.id.action_register1_to_register2)
        }


        //Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        //Firebase Auth instance
        mAuth = FirebaseAuth.getInstance()

        binding.btnLogingoogle.setOnClickListener {
            signIn()
        }

        callbackManager = CallbackManager.Factory.create()
        binding.btnLoginFacebook.setOnClickListener {

            LoginManager.getInstance().logInWithReadPermissions(requireActivity(), listOf("email"))

            LoginManager.getInstance().registerCallback(callbackManager,
                object: FacebookCallback<LoginResult>{
                    override fun onSuccess(result: LoginResult?) {

                        result?.let {
                            val token = it.accessToken
                            val credential = FacebookAuthProvider.getCredential(token.token)
                            mAuth.signInWithCredential(credential)
                                .addOnCompleteListener { it ->
                                    if (it.isSuccessful) {
                                        // Sign in success
                                        val intent = Intent(requireActivity(), NavigationContainerHome::class.java)
                                        startActivity(intent)
                                        requireActivity().finish()


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(requireActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show()
                                    }

                                }
                        }
                        //handleFacebookAccessToken(result!!.accessToken)
                    }

                    override fun onCancel() {

                    }

                    override fun onError(error: FacebookException?) {

                    }

                })
        }


        return binding.root
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

//    private fun handleFacebookAccessToken(token: AccessToken) {
//        mAuth = FirebaseAuth.getInstance()
//
//        val credential = FacebookAuthProvider.getCredential(token.token)
//        mAuth.signInWithCredential(credential)
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    val intent = Intent(requireActivity(), NavigationContainerHome::class.java)
//                    startActivity(intent)
//                    requireActivity().finish()
//
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Toast.makeText(requireActivity(), "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                }
//
//            }
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        //Facebook
        callbackManager = CallbackManager.Factory.create()

        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)


        // Google
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful
                    val account = task.getResult(ApiException::class.java)!!

                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign failed

                }
            } else {
                Log.w("SignInActivity", exception.toString())
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInActivity", "signInWithCredential:success")
                    val intent = Intent(requireActivity(), NavigationContainerHome::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("SignInActivity", "signInWithCredential:failure")
                }
            }
    }

}
