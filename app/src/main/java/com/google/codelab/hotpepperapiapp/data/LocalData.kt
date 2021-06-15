package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.RealmClient.fetchStores
import com.google.codelab.hotpepperapiapp.model.Store
import io.realm.Realm
import io.realm.RealmResults

class LocalData {
    fun fetchLocalStoreIds(): RealmResults<Store> {
        val realm = Realm.getDefaultInstance()

        return realm.fetchStores()
    }
}
