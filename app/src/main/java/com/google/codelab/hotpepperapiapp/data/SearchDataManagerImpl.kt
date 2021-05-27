package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single
import retrofit2.Response

class SearchDataManagerImpl : SearchDataManager {
    private val remote: RemoteData = RemoteData()

    override fun fetchFavoriteStores(storeId: String): Single<Response<StoresResponse>> {
        return remote.fetchFavoriteStores(storeId)
    }

    override fun fetchStores(lat: Double, lng: Double, start: Int): Single<Response<StoresResponse>> {
        return remote.fetchStores(lat, lng, start)
    }
}
