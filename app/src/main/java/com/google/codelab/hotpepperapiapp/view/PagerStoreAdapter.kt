package com.google.codelab.hotpepperapiapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.Shop

class PagerStoreAdapter(
    private val store: MutableList<Shop>,
    private val listener: ListListener,
    val context: Context
) :
    RecyclerView.Adapter<PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
        PagerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.pager_store, parent, false)
        )

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(store[position], context)
        holder.itemView.setOnClickListener {
            listener.onClickRow(it, store[position])
        }
    }

    override fun getItemCount(): Int = store.size

    interface ListListener {
        fun onClickRow(tappedView: View, selectedStore: Shop)
    }
}

class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val image: ImageView = itemView.findViewById(R.id.pager_image)
    private val name: TextView = itemView.findViewById(R.id.pager_name)
    private val price: TextView = itemView.findViewById(R.id.pager_charge)
    private val genre: TextView = itemView.findViewById(R.id.pager_genre)

    fun bind(store: Shop, context: Context) {
        Glide.with(context).load(store.photo.photo.logo).into(image)
        name.text = store.name
        price.text = store.budget.average
        genre.text = store.genre.name
    }
}
