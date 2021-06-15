package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.model.Store
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single
import io.realm.RealmResults
import retrofit2.Response

class SearchDataManagerImpl : SearchDataManager {
    private val remote: RemoteData = RemoteData()
    private val local: LocalData = LocalData()

    override fun fetchFavoriteStores(storeId: String): Single<Response<StoresResponse>> {
        return remote.fetchFavoriteStores(storeId)
    }

    override fun fetchStores(
        lat: Double,
        lng: Double,
        start: Int
    ): Single<Response<StoresResponse>> {
        return remote.fetchStores(lat, lng, start)
    }

    override fun fetchLocalStoreIds(): Single<RealmResults<Store>> {
        return Single.just(local.fetchLocalStoreIds())
    }
}
