package com.XD.fitgain.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentRegister2Binding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_register2.*


class Register2 : Fragment() {
    private lateinit var binding: FragmentRegister2Binding
    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()
    var nombre =  et_username.getText().toString()
    var fecha = et_bornDate.getText().toString()
    var peso = Integer.parseInt(et_peso.getText().toString())

    val data= hashMapOf(
         "nombre" to nombre,
        "fechaNacimiento" to fecha,
         "weight" to peso
    )





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegister2Binding.inflate(inflater, container, false)

        binding.btnBackScreen?.setOnClickListener {
            it.findNavController().navigate(R.id.action_register2_to_register1)
        }

        binding.btnContinuar!!.setOnClickListener {
            it.findNavController().navigate(R.id.action_register2_to_register3)
            db.collection("Usuarios").add(data).addOnFailureListener {e ->
                Log.w(TAG, "Error", e)
            }


        }



        return binding.root
    }



}
