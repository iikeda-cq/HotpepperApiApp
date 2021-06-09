package com.google.codelab.hotpepperapiapp.view

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.model.response.NearStore
import com.google.codelab.hotpepperapiapp.databinding.CellStoreBinding
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import com.google.codelab.hotpepperapiapp.model.StoreModel

class StoreItem(private val store: NearStore, val context: Context) : BindableItem<CellStoreBinding>() {
    override fun getLayout() = R.layout.cell_store

    override fun bind(viewBinding: CellStoreBinding, position: Int) {
        Glide.with(context).load(store.photo.photo.logo).into(viewBinding.storeImage)
        viewBinding.storeName.text = store.name
        viewBinding.storePrice.text = store.budget.average
        viewBinding.storeGenre.text = store.genre.name
    }

    override fun initializeViewBinding(view: View): CellStoreBinding {
        return CellStoreBinding.bind(view)
    }

    override fun isSameAs(other: Item<*>): Boolean =
        (other as? StoreItem)?.store?.id == store.id
}


