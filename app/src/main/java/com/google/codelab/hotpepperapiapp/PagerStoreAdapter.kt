package com.google.codelab.hotpepperapiapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PagerStoreAdapter(private val store: List<Store>, private val listener: ListListener) :
    RecyclerView.Adapter<PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
        PagerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.pager_store, parent, false)
        )

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(store[position])
        holder.itemView.setOnClickListener {
            listener.onClickRow(it, store[position])
        }
    }

    override fun getItemCount(): Int = store.size

    interface ListListener {
        fun onClickRow(tappedView: View, selectedStore: Store)
    }
}

class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val image: ImageView = itemView.findViewById(R.id.pager_image)
    private val name: TextView = itemView.findViewById(R.id.pager_name)
    private val price: TextView = itemView.findViewById(R.id.pager_charge)
    private val genre: TextView = itemView.findViewById(R.id.pager_genre)

    fun bind(store: Store) {
        image.setImageResource(R.drawable.store_image)
        name.text = store.name
        price.text = store.price
        genre.text = store.genre
    }
}
