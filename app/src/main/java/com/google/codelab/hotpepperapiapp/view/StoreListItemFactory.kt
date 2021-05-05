package com.google.codelab.hotpepperapiapp.view

import android.view.View
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.model.Store
import com.google.codelab.hotpepperapiapp.databinding.CellStoreBinding
import com.xwray.groupie.viewbinding.BindableItem

class StoreItem(private val store: Store) : BindableItem<CellStoreBinding>() {
    override fun getLayout() = R.layout.cell_store

    override fun bind(viewBinding: CellStoreBinding, position: Int) {
        viewBinding.storeImage.setImageResource(R.drawable.store_image)
        viewBinding.storeName.text = store.name
        viewBinding.storePrice.text = store.price
        viewBinding.storeGenre.text = store.genre
    }

    override fun initializeViewBinding(view: View): CellStoreBinding {
        return CellStoreBinding.bind(view)
    }
}


