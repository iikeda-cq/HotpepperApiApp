package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.model.businessmodel.StoreListBusinessModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface SearchDataManager {
    fun fetchStores(
        start: Int = 1
    ): Single<StoreListBusinessModel>

    fun fetchFavoriteStores(storeId: String): Single<StoreListBusinessModel>

    fun fetchLocalStoreIds(): Single<List<String>>

    fun addFavoriteStore(storeId: String): Completable

    fun deleteFavoriteStore(storeId: String): Completable

    fun hasFavoriteStore(storeId: String): Single<Boolean>

    fun saveLocation(lat: Double, lng: Double)

    fun hasLocation(): Single<Boolean>
}
