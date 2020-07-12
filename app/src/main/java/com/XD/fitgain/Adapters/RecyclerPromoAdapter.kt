package com.XD.fitgain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.XD.fitgain.R
import com.XD.fitgain.model.Busines
import com.XD.fitgain.model.Promo
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.promo_recycler_style.view.*

class RecyclerPromoAdapter(
    var promoListItems: List<Promo>,
    var itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class PromoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(promo: Promo, clickListener: OnItemClickListener) {
            Glide.with(itemView.context).load(promo.photoUrl).into(itemView.img_promo)
            itemView.tv_titulo.text = promo.titulo

            var descripcionCompleta = promo.descripcion

            itemView.tv_descripcion.text = descripcionCompleta.substring(0,
                descripcionCompleta.length.coerceAtMost(35)) + " ..."

            itemView.tv_puntos.text = "${promo.pointsRequired} PS"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.promo_recycler_style, parent, false)
        return PromoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return promoListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PromoViewHolder).bind(promoListItems[position], itemClickListener)
    }

    interface OnItemClickListener {
        fun onItemClick(promo: Promo)
    }
}