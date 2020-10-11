package com.XD.fitgain.views.fragments

import android.content.Intent
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
import com.XD.fitgain.views.NavigationContainerHome
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class Register1 : Fragment() {

    private lateinit var binding: FragmentRegister1Binding
    private var isPasswordValid: Boolean = false
    private var isEmailCorrectValid: Boolean = false

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    private val callbackManager = CallbackManager.Factory.create()
    private val db = FirebaseFirestore.getInstance()

    private val RC_SIGN_IN = 120

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

    private fun performGoolgeSignin() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
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
}
