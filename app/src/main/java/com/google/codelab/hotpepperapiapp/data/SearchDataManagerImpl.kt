package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.model.Store
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.realm.RealmResults
import retrofit2.Response
import javax.inject.Inject

class SearchDataManagerImpl @Inject constructor(
    private val remote: RemoteData,
    private val local: LocalData
) : SearchDataManager {

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

    override fun addFavoriteStore(storeId: String): Completable {
        return local.addFavoriteStore(storeId)
    }

    override fun deleteFavoriteStore(storeId: String): Completable {
        return local.deleteFavoriteStore(storeId)
    }

    override fun hasFavoriteStore(storeId: String): Single<Boolean> {
        return Single.fromCallable { local.hasFavoriteStore(storeId) }
    }
}
