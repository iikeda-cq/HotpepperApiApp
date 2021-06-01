package com.google.codelab.hotpepperapiapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.databinding.PagerStoreBinding
import com.google.codelab.hotpepperapiapp.model.StoreModel

class PagerStoreAdapter(private val store: List<StoreModel>, private val listener: ListListener) :
    RecyclerView.Adapter<PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PagerStoreBinding.inflate(layoutInflater, parent, false)
        return PagerViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(store[position])
        holder.itemView.setOnClickListener {
            listener.onClickRow(it, store[position])
        }
    }

    override fun getItemCount(): Int = store.size

    interface ListListener {
        fun onClickRow(tappedView: View, selectedStore: StoreModel)
    }
}

class PagerViewHolder(val binding: PagerStoreBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(store: StoreModel) {
        binding.pagerImage.setImageResource(R.drawable.store_image)
        binding.pagerName.text = store.name
        binding.pagerCharge.text = store.price
        binding.pagerGenre.text = store.genre
    }
}
