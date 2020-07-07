package com.XD.fitgain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.XD.fitgain.R
import com.XD.fitgain.model.Busines
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.category_recycler_style.view.*
import java.text.SimpleDateFormat
import java.util.*

class RecyclerBusinesAdapter(var postListItems: List<Busines>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class BussinesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(bussines: Busines) {
            Glide.with(itemView.context).load(bussines.photoUrl).into(itemView.img_restaurantLogo)
            itemView.tv_nombreRestaurante.text = bussines.nombre

            val milliseconds = bussines.createdAt!!.seconds * 1000 + bussines.createdAt.nanoseconds / 1000000
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(milliseconds)
            val date = sdf.format(netDate).toString()
            itemView.tv_createdAt.text = "Desde: $date"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_recycler_style, parent, false)
        return BussinesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BussinesViewHolder).bind(postListItems[position])
    }

}