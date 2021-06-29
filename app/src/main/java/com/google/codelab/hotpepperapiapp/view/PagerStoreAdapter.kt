package com.google.codelab.hotpepperapiapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.codelab.hotpepperapiapp.databinding.PagerStoreBinding
import com.google.codelab.hotpepperapiapp.model.response.NearStore

class PagerStoreAdapter(
    private val store: List<NearStore>,
    private val onCellClick: (NearStore) -> Unit
) :
    RecyclerView.Adapter<PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PagerStoreBinding.inflate(layoutInflater, parent, false)
        return PagerViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(store[position])
        holder.itemView.setOnClickListener {
            onCellClick(store[position])
        }
    }

    override fun getItemCount(): Int = store.size
}

class PagerViewHolder(val binding: PagerStoreBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(store: NearStore) {
        binding.item = store
    }
}
