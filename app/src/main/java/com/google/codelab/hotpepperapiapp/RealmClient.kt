package com.google.codelab.hotpepperapiapp

import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.createObject
import io.realm.kotlin.where

object RealmClient {
    private const val STORE_ID = "storeId"

    fun addStore(realm: Realm, storeId: String) {
        realm.executeTransaction {
            val currentId = realm.where<Store>().max("id")
            val nextId = (currentId?.toLong() ?: 0L) + 1L
            val store = realm.createObject<Store>(nextId)

            store.storeId = storeId
        }
    }

    fun deleteStore(realm: Realm, id: String) {
        val target = realm.where(Store::class.java)
            .equalTo(STORE_ID, id)
            .findAll()

        realm.executeTransaction {
            target.deleteFromRealm(0)
        }
    }

    fun fetchStores(realm: Realm): RealmResults<Store> {
        return realm.where(Store::class.java)
            .distinct(STORE_ID)
            .findAll()
    }

    fun fetchFirstStore(realm: Realm, storeId: String): Store? {
        return realm.where(Store::class.java)
            .equalTo(STORE_ID, storeId)
            .findFirst()
    }
}
