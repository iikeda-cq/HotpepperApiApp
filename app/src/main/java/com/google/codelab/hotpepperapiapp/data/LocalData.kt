package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.ext.addStore
import com.google.codelab.hotpepperapiapp.ext.deleteStore
import com.google.codelab.hotpepperapiapp.ext.fetchFavoriteStore
import com.google.codelab.hotpepperapiapp.ext.fetchStores
import com.google.codelab.hotpepperapiapp.model.Store
import io.reactivex.Completable
import io.realm.Realm
import io.realm.RealmResults

class LocalData {
    fun fetchLocalStoreIds(): RealmResults<Store> {
        // Caused by: java.lang.IllegalStateException: This Realm instance has already been closed, making it unusable.
        val realm = Realm.getDefaultInstance()
        return realm.fetchStores()
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

    fun fetchFavoriteStore(storeId: String): Boolean {
        return Realm.getDefaultInstance().use { realm ->
            realm.fetchFavoriteStore(storeId)
        }
    }
}
