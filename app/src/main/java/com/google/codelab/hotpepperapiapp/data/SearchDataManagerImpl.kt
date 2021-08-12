package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import retrofit2.Response
import javax.inject.Inject

class SearchDataManagerImpl @Inject constructor(
    private val remote: RemoteData,
    private val local: LocalData
) : SearchDataManager {
    val lat: BehaviorSubject<Double> = BehaviorSubject.create()
    val lng: BehaviorSubject<Double> = BehaviorSubject.create()

    override fun fetchFavoriteStores(storeId: String): Single<Response<StoresResponse>> {
        return remote.fetchFavoriteStores(storeId)
    }

    override fun fetchStores(
        start: Int
    ): Single<Response<StoresResponse>> {
        return remote.fetchStores(lat.value, lng.value, start)
    }

    override fun fetchLocalStoreIds(): Single<List<String>> {
        return Single.fromCallable { local.fetchLocalStoreIds() }
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

    override fun saveLocation(lat: Double, lng: Double) {
        this.lat.onNext(lat)
        this.lng.onNext(lng)
    }

    override fun hasLocation(): Single<Boolean> {
        return Single.fromCallable { lat.hasValue() && lng.hasValue() }
    }
}
