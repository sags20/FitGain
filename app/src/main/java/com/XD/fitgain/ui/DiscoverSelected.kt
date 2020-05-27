package com.XD.fitgain.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.XD.fitgain.databinding.ActivityDiscoverSelectedBinding

class DiscoverSelected : AppCompatActivity() {
    private lateinit var binding : ActivityDiscoverSelectedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscoverSelectedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnBackScreen.setOnClickListener {
            finish()
        }
    }
}
