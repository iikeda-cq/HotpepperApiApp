package com.google.codelab.hotpepperapiapp

import com.google.codelab.hotpepperapiapp.model.Store
import com.google.codelab.hotpepperapiapp.model.StoreModel
import io.realm.RealmResults
import java.util.*

object StoreMapper {
    // RealmのStoreからdata classのStoreModelに変換を行う
    fun transform(store: RealmResults<Store>): MutableList<StoreModel> {
        val storeModel: MutableList<StoreModel> = ArrayList()

        store.forEach { store ->
            storeModel.add(
                StoreModel(
                    storeId = store.storeId,
                )
            )
        }
        return storeModel
    }
}
