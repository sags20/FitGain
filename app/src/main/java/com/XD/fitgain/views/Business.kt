package com.XD.fitgain.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.XD.fitgain.R
import com.XD.fitgain.model.Busines
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_business.*

class Business : AppCompatActivity() {
    private lateinit var busines: Busines

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)

        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        busines = intent.getParcelableExtra("Busines") as Busines
        setDataFromIntent(busines)
    }

    private fun setDataFromIntent(busines: Busines) {
        Glide.with(this).load(busines.photoUrl).into(photo)
        tv_categoria.text = busines.categoria
        tv_restaurante.text = "${busines.nombre} - Promos"
    }
}