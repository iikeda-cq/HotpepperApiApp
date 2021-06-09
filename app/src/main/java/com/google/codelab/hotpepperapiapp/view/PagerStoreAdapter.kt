package com.google.codelab.hotpepperapiapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.model.response.NearStore
import com.google.codelab.hotpepperapiapp.databinding.PagerStoreBinding
import com.google.codelab.hotpepperapiapp.model.StoreModel

class PagerStoreAdapter(private val store: List<NearStore>,
                        val context: Context,
                        private val onCellClick: (NearStore) -> Unit) :
    RecyclerView.Adapter<PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PagerStoreBinding.inflate(layoutInflater, parent, false)
        return PagerViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(store[position], context)
        holder.itemView.setOnClickListener {
            onCellClick(store[position])
        }
    }

    override fun getItemCount(): Int = store.size
}

class PagerViewHolder(val binding: PagerStoreBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(store: NearStore, context: Context) {
        Glide.with(context).load(store.photo.photo.logo).into(binding.pagerImage)
        binding.pagerName.text = store.name
        binding.pagerCharge.text = store.budget.average
        binding.pagerGenre.text = store.genre.name
    }
}
