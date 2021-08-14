package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.model.businessmodel.StoreListBusinessModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class SearchDataManagerImpl @Inject constructor(
    private val remote: RemoteData,
    private val local: LocalData
) : SearchDataManager {
    val lat: BehaviorSubject<Double> = BehaviorSubject.create()
    val lng: BehaviorSubject<Double> = BehaviorSubject.create()

    /**
     * 修正前は戻り値の型が Response<StoresResponse> であり、UseCaseがRepositoryやAPI側に依存していた。
     * 戻り値を[StoreListBusinessModel]にすることで
     *  1. アプリでレスポンスのデータが扱いやすくなる
     *  2. UseCaseがRepositoryに依存することがなくなる
     *  3. UseCaseがAPI側の仕様変更を気にしなくてよくなった
     */
    override fun fetchFavoriteStores(storeId: String): Single<StoreListBusinessModel> {
        return remote.fetchFavoriteStores(storeId)
            .map { StoreListBusinessModel.transform(it) }
    }

    // 修正前
//    override fun fetchFavoriteStores(storeId: String): Single<Response<StoresResponse>> {
//        return remote.fetchFavoriteStores(storeId)

    override fun fetchStores(start: Int): Single<StoreListBusinessModel> {
        return remote.fetchStores(lat.value, lng.value, start)
            .map { StoreListBusinessModel.transform(it) }
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
