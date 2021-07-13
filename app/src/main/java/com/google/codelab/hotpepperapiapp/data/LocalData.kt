package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.ext.addStore
import com.google.codelab.hotpepperapiapp.ext.deleteStore
import com.google.codelab.hotpepperapiapp.ext.hasFavoriteStore
import com.google.codelab.hotpepperapiapp.ext.fetchStores
import com.google.codelab.hotpepperapiapp.model.Store
import io.reactivex.rxjava3.core.Completable
import io.realm.Realm
import io.realm.RealmResults
import javax.inject.Inject

class LocalData @Inject constructor(private val realm: Realm){
    fun fetchLocalStoreIds(): RealmResults<Store> {
        // Caused by: java.lang.IllegalStateException: This Realm instance has already been closed, making it unusable.
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

    fun hasFavoriteStore(storeId: String): Boolean {
        return Realm.getDefaultInstance().use { realm ->
            realm.hasFavoriteStore(storeId)
        }
    }
}
