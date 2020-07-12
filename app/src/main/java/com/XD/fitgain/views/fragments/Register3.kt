package com.XD.fitgain.views.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentRegister3Binding
import com.XD.fitgain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tapadoo.alerter.Alerter
import dmax.dialog.SpotsDialog
import java.util.*

class Register3 : Fragment() {

    private lateinit var nuevoUsuario: User
    private lateinit var password: String

    private lateinit var binding: FragmentRegister3Binding

    private var isMaleSelected = true

    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()

    lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegister3Binding.inflate(inflater, container, false)
        alertDialog = SpotsDialog.Builder().setContext(activity).build()

        binding.btnFinalizar.setOnClickListener {
            if (isMaleSelected) {
                nuevoUsuario.genero = "Masculino"
            } else {
                nuevoUsuario.genero = "Femenino"
            }
            performCreateNewUser(it)

        }

        binding.maleSelect.setOnClickListener {
            binding.maleSelectedIndicator.drawable.setTint(
                getColor(
                    requireActivity(),
                    R.color.light_purple
                )
            )
            binding.femaleSelectedIndicator.drawable.setTint(
                getColor(
                    requireActivity(),
                    R.color.light_gray
                )
            )
            isMaleSelected = true
        }

        binding.femaleSelect.setOnClickListener {
            binding.femaleSelectedIndicator.drawable.setTint(
                getColor(
                    requireActivity(),
                    R.color.light_purple
                )
            )
            binding.maleSelectedIndicator.drawable.setTint(
                getColor(
                    requireActivity(),
                    R.color.light_gray
                )
            )
            isMaleSelected = false
        }

        return binding.root
    }


    private fun performCreateNewUser(view: View) {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(nuevoUsuario.email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("REGISTER_DEBUG", "User created!!")
                    uploadImageToFirebaseStorage(view)

                } else {
                    Log.d("REGISTER_DEBUG", "create user fail: ${it.exception}")
                    Alerter.create(activity)
                        .setText("Ha ocurrido un error ${it.exception!!.message}")
                        .setBackgroundColorRes(R.color.alert_default_error_background)
                        .show()
                }
            }
    }

    private fun uploadImageToFirebaseStorage(view: View) {
        if (nuevoUsuario.photoUrl.isEmpty()) {
            nuevoUsuario.photoUrl =
                "https://images.unsplash.com/photo-1591291621164-2c6367723315?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=751&q=80"
            createUserInDatabase()
            view.findNavController().navigate(R.id.action_register3_to_completeScreen)
            return
        }

        alertDialog.show()
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(Uri.parse(nuevoUsuario.photoUrl))
            .addOnSuccessListener {
                Log.d("REGISTER_DEBUG", "Succesfully upload image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("REGISTER_DEBUG", "File location: ${it}")

                    nuevoUsuario.photoUrl = it.toString()
                    createUserInDatabase()
                    alertDialog.dismiss()
                    view.findNavController().navigate(R.id.action_register3_to_completeScreen)
                }


            }
            .addOnFailureListener {

            }
    }


    private fun createUserInDatabase() {
        nuevoUsuario.uid = FirebaseAuth.getInstance().uid ?: ""

        db.collection("Usuarios").document(nuevoUsuario.uid).set(nuevoUsuario)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("REGISTER_DEBUG", "User saved in db!!")
                } else {
                    Log.d("REGISTER_DEBUG", "create user in db fail: ${it.exception}")
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            var args = Register3Args.fromBundle(requireArguments())
            nuevoUsuario = args.userPart2
            password = args.password
        }
    }


}
