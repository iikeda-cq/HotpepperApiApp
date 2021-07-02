package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.model.Store
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
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

    fun fetchFavoriteStore(storeId: String): Single<Boolean>
}
