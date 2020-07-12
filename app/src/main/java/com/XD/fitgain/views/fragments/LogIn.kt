package com.XD.fitgain.views.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentLogInBinding
import com.XD.fitgain.model.User
import com.XD.fitgain.views.NavigationContainerHome
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tapadoo.alerter.Alerter
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LogIn : Fragment() {
    private lateinit var binding: FragmentLogInBinding

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    private val callbackManager = CallbackManager.Factory.create()
    private val db = FirebaseFirestore.getInstance()

    private val RC_SIGN_IN = 120

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater, container, false)

        //getHash()

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

        //----------------- GOOGLE SIGN IN ----------------------
        //Configure Google Sign In

        binding.btnLogingoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            performGoolgeSignin()
        }
        //----------------- FIN GOOGLE SIGN IN ----------------------

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


                                        val currentUser = auth.currentUser
                                        var nuevoUsuario = User()

                                        nuevoUsuario.photoUrl = currentUser!!.photoUrl.toString()
                                        nuevoUsuario.nombre = currentUser.displayName.toString()
                                        nuevoUsuario.email = currentUser.email.toString()
                                        nuevoUsuario.uid = currentUser.uid

                                        val userExist =
                                            db.collection("Usuarios").document(currentUser.uid)
                                                .get()
                                        Log.d("FACEBOOK_LOGIN",userExist.toString())
                                        if (userExist == null) {
                                            db.collection("Usuarios").document(currentUser.uid)
                                                .set(nuevoUsuario, SetOptions.merge())
                                        } else {
                                            val data = hashMapOf(
                                                "photoUrl" to currentUser.photoUrl.toString(),
                                                "nombre" to currentUser.displayName.toString(),
                                                "email" to currentUser.email.toString(),
                                                "uid" to currentUser.uid
                                            )
                                            db.collection("Usuarios").document(currentUser.uid)
                                                .set(data, SetOptions.merge())
                                        }

                                        val intent = Intent(
                                            requireActivity(),
                                            NavigationContainerHome::class.java
                                        )
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

    private fun performGoolgeSignin() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
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
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInActivity", "signInWithCredential:success")
                    val currentUser = auth.currentUser
                    var nuevoUsuario = User()

                    nuevoUsuario.photoUrl = currentUser!!.photoUrl.toString()
                    nuevoUsuario.nombre = currentUser.displayName.toString()
                    nuevoUsuario.email = currentUser.email.toString()
                    nuevoUsuario.uid = currentUser.uid

                    val userExist = db.collection("Usuarios").document(currentUser.uid).get()

                    if (userExist == null) {
                        db.collection("Usuarios").document(currentUser.uid)
                            .set(nuevoUsuario, SetOptions.merge())
                    } else {
                        val data = hashMapOf(
                            "photoUrl" to currentUser.photoUrl.toString(),
                            "nombre" to currentUser.displayName.toString(),
                            "email" to currentUser.email.toString(),
                            "uid" to currentUser.uid
                        )
                        db.collection("Usuarios").document(currentUser.uid)
                            .set(data, SetOptions.merge())
                    }

                    val intent = Intent(requireActivity(), NavigationContainerHome::class.java)
                    startActivity(intent)
                    requireActivity().finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("SignInActivity", "signInWithCredential:failure")
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(activity, NavigationContainerHome::class.java))
        }

    }
}
