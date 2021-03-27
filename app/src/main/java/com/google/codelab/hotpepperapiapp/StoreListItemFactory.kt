package com.google.codelab.hotpepperapiapp

import android.view.View
import com.google.codelab.hotpepperapiapp.databinding.CellStoreBinding
import com.xwray.groupie.viewbinding.BindableItem

class StoreItem(private val store: Store) : BindableItem<CellStoreBinding>() {
    override fun getLayout() = R.layout.cell_store

    override fun bind(viewBinding: CellStoreBinding, position: Int) {
        viewBinding.storeImage.setImageResource(R.drawable.store_image)
        viewBinding.storeName.text = store.name
        viewBinding.storeCharge.text = store.price
    }

    override fun initializeViewBinding(view: View): CellStoreBinding {
        return CellStoreBinding.bind(view)
    }
}


