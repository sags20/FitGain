package com.XD.fitgain.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentProfileBinding
import com.XD.fitgain.ui.MainActivity
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class Profile : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        binding = FragmentProfileBinding.inflate(inflater, container, false)

       // Glide.with(this).load(currentUser?.photoUrl.toString()).into(circleImageView)

        binding.btnCerrarSesion.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(activity, MainActivity::class.java))
            requireActivity().finish()

            //Facebook
            LoginManager.getInstance().logOut()
            startActivity(Intent(activity, MainActivity::class.java))
            requireActivity().finish()
        }

        return binding.root
    }

}
