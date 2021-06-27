package com.google.codelab.hotpepperapiapp.ext

import com.google.codelab.hotpepperapiapp.model.Store
import io.reactivex.Completable
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.createObject
import io.realm.kotlin.where

private const val STORE_ID = "storeId"
private const val ID = "id"

fun Realm.addStore(storeId: String): Completable {
    this.executeTransaction { realm ->
        val currentId = realm.where<Store>().max(ID)
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
        .sort(ID, Sort.ASCENDING)
}

fun Realm.fetchFavoriteStore(storeId: String): Boolean {
    val favorite = this.where(Store::class.java)
        .equalTo(STORE_ID, storeId)
        .findFirst()

    return favorite != null
}

