package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.ext.addStore
import com.google.codelab.hotpepperapiapp.ext.deleteStore
import com.google.codelab.hotpepperapiapp.ext.fetchStores
import com.google.codelab.hotpepperapiapp.ext.hasFavoriteStore
import com.google.codelab.hotpepperapiapp.model.StoreMapper
import io.reactivex.rxjava3.core.Completable
import io.realm.Realm
import javax.inject.Inject

class LocalData @Inject constructor() {
    fun fetchLocalStoreIds(): List<String> {
        val favoriteIds: MutableList<String> = ArrayList()
        Realm.getDefaultInstance().use { realm ->
            val storeIds = realm.fetchStores()
            StoreMapper.transform(storeIds).map { favoriteIds.add(it.storeId) }
        }

        return favoriteIds
    }

    fun addFavoriteStore(storeId: String): Completable {
        return Realm.getDefaultInstance().use { realm ->
            realm.addStore(storeId)
        }
    }

    fun deleteFavoriteStore(storeId: String): Completable {
        return Realm.getDefaultInstance().use { realm ->
            realm.deleteStore(storeId)
        }
    }

    fun hasFavoriteStore(storeId: String): Boolean {
        return Realm.getDefaultInstance().use { realm ->
            realm.hasFavoriteStore(storeId)
        }
    }
}
