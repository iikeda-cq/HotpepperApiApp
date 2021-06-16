package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.RealmClient.addStore
import com.google.codelab.hotpepperapiapp.RealmClient.deleteStore
import com.google.codelab.hotpepperapiapp.RealmClient.fetchStores
import com.google.codelab.hotpepperapiapp.model.Store
import io.reactivex.Completable
import io.realm.Realm
import io.realm.RealmResults

class LocalData {
    val realm: Realm = Realm.getDefaultInstance()

    fun fetchLocalStoreIds(): RealmResults<Store> {
        return realm.fetchStores()
    }

    fun addFavoriteStore(storeId: String): Completable {
        return realm.addStore(storeId)
    }

    fun deleteFavoriteStore(storeId: String): Completable{
        return realm.deleteStore(storeId)
    }
}
