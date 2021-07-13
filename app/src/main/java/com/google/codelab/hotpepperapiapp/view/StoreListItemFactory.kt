package com.google.codelab.hotpepperapiapp.view

import android.content.Context
import android.view.View
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.databinding.CellStoreBinding
import com.google.codelab.hotpepperapiapp.model.response.NearStore
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem

class StoreItem(private val store: NearStore, val context: Context) :
    BindableItem<CellStoreBinding>() {
    override fun getLayout() = R.layout.cell_store

    override fun bind(viewBinding: CellStoreBinding, position: Int) {
        viewBinding.item = store
    }

    override fun initializeViewBinding(view: View): CellStoreBinding {
        return CellStoreBinding.bind(view)
    }

    override fun isSameAs(other: Item<*>): Boolean =
        (other as? StoreItem)?.store?.id == store.id
}


