package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.model.Store
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.RealmResults
import retrofit2.Response

interface SearchDataManager {
    fun fetchStores(
        lat: Double,
        lng: Double,
        start: Int = 1
    ): Single<Response<StoresResponse>>

    fun fetchFavoriteStores(storeId: String): Single<Response<StoresResponse>>

    fun fetchLocalStoreIds(): Single<RealmResults<Store>>

    fun addFavoriteStore(storeId: String): Completable

    fun deleteFavoriteStore(storeId: String): Completable
}
