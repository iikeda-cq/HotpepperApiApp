package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single

interface SearchDataManager {
    fun fetchStores(
        lat: Double,
        lng: Double,
        start: Int = 1
    ): Single<StoresResponse>

    fun fetchFavoriteStores(storeId: String): Single<StoresResponse>
}
