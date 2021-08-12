package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

interface SearchDataManager {
    fun fetchStores(
        start: Int = 1
    ): Single<Response<StoresResponse>>

    fun fetchFavoriteStores(storeId: String): Single<Response<StoresResponse>>

    fun fetchLocalStoreIds(): Single<List<String>>

    fun addFavoriteStore(storeId: String): Completable

    fun deleteFavoriteStore(storeId: String): Completable

    fun hasFavoriteStore(storeId: String): Single<Boolean>

    fun saveLocation(lat: Double, lng: Double)

    fun hasLocation(): Single<Boolean>
}
