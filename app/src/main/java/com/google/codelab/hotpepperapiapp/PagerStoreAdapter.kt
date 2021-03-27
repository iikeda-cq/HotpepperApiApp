package com.google.codelab.hotpepperapiapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PagerStoreAdapter (private val items: List<Store>) : RecyclerView.Adapter<PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
        PagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pager_store, parent, false))

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val image: ImageView = itemView.findViewById(R.id.pager_image)
    private val name: TextView = itemView.findViewById(R.id.pager_name)
    private val charge: TextView = itemView.findViewById(R.id.pager_charge)

    fun bind(store: Store) {
        image.setImageResource(R.drawable.store_image)
        name.text = store.name
        charge.text = store.price

    }
}
