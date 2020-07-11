package com.XD.fitgain.ui.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentLogInBinding
import com.XD.fitgain.ui.NavigationContainerHome
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LogIn : Fragment() {
    private lateinit var binding: FragmentLogInBinding

    private val callbackManager = CallbackManager.Factory.create()

    private val db = FirebaseFirestore.getInstance()

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


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
            startActivity(Intent(activity, NavigationContainerHome::class.java))
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

        binding.btnLoginFacebook.setOnClickListener {
            Log.d("FACEBOOK_LOGIN", "INICIANDO AUTH")
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        result?.let {
                            val token = it.accessToken
                            val credential = FacebookAuthProvider.getCredential(token.token)
                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Log.d("FACEBOOK_LOGIN", "LOGIN CREADO DE MANERA EXITOSA")


                                        val currentUser = mAuth.currentUser
                                        val currentPoints = 0

                                        val user = hashMapOf(
                                            "nombre" to currentUser?.displayName,
                                            "email" to currentUser?.email,
                                            "photoUrl" to currentUser?.photoUrl.toString(),
                                            "points" to currentPoints
                                        )
                                        db.collection("Usuarios").document(currentUser?.uid.toString()).set(user, SetOptions.merge())
                                        val intent = Intent(requireActivity(), NavigationContainerHome::class.java)
                                        startActivity(intent)
                                        requireActivity().finish()

                                    } else {
                                        Log.d(
                                            "FACEBOOK_LOGIN",
                                            "Error cuando intentamos crear en firebase."
                                        )
                                    }
                                }
                        }
                    }

                    override fun onCancel() {
                        Log.d("FACEBOOK_LOGIN", "CANCELADO POR EL USUARIIO")
                    }

                    override fun onError(error: FacebookException?) {
                        Log.d("FACEBOOK_LOGIN", "ERROR ${error!!.message}")
                    }
                })
        }


        return binding.root
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //Facebook

        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
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
                    val currentUser = mAuth.currentUser
                    val currentPoints = 0


                    val user = hashMapOf(
                        "nombre" to currentUser?.displayName,
                        "email" to currentUser?.email,
                        "photoUrl" to currentUser?.photoUrl.toString(),
                        "points" to currentPoints
                    )
                    db.collection("Usuarios").document(currentUser?.uid.toString()).set(user, SetOptions.merge())
                    val intent = Intent(requireActivity(), NavigationContainerHome::class.java)
                    startActivity(intent)
                    requireActivity().finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("SignInActivity", "signInWithCredential:failure")
                }
            }
    }

    private fun getHash() {
        try {
            val info = activity?.packageManager?.getPackageInfo(
                "com.example.firebasetest",
                PackageManager.GET_SIGNATURES
            )
            if (info != null) {
                for (signature in info.signatures) {
                    val md: MessageDigest = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val sign: String = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                    Log.d("FACEBOOK_LOGIN", sign)
                    Toast.makeText(activity?.applicationContext, sign, Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

}
