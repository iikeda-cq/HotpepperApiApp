package com.google.codelab.hotpepperapiapp

import com.google.codelab.hotpepperapiapp.model.Store
import io.reactivex.Completable
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.createObject
import io.realm.kotlin.where

// ToDo classに変更する
object RealmClient {
    private const val STORE_ID = "storeId"

    fun Realm.addStore(storeId: String): Completable {
        this.executeTransaction { realm ->
            val currentId = realm.where<Store>().max("id")
            val nextId = (currentId?.toLong() ?: 0L) + 1L
            val store = realm.createObject<Store>(nextId)

            store.storeId = storeId
        }
        return Completable.complete()
    }

    fun Realm.deleteStore(id: String): Completable {
        val target = this.where(Store::class.java)
            .equalTo(STORE_ID, id)
            .findAll()

        this.executeTransaction {
            target.deleteFromRealm(0)
        }
        return Completable.complete()
    }

    fun Realm.fetchStores(): RealmResults<Store> {
        return this.where(Store::class.java)
            .distinct(STORE_ID)
            .findAll()
            .sort("id", Sort.ASCENDING)
    }

    fun Realm.fetchFirstStore(storeId: String): Store? {
        return this.where(Store::class.java)
            .equalTo(STORE_ID, storeId)
            .findFirst()
    }
}
