package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.RealmClient.addStore
import com.google.codelab.hotpepperapiapp.RealmClient.deleteStore
import com.google.codelab.hotpepperapiapp.RealmClient.fetchFavoriteStore
import com.google.codelab.hotpepperapiapp.RealmClient.fetchStores
import com.google.codelab.hotpepperapiapp.model.Store
import io.reactivex.Completable
import io.realm.Realm
import io.realm.RealmResults

class LocalData {
    fun fetchLocalStoreIds(): RealmResults<Store> {
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
