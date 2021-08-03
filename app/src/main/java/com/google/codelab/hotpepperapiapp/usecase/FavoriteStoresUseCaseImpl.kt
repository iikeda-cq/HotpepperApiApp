package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.Signal
import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.StoreMapper
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class FavoriteStoresUseCaseImpl @Inject constructor(
    private val dataManager: SearchDataManagerImpl
) : FavoriteStoreUseCase {
    private val favoriteStoreList: PublishSubject<StoresResponse> = PublishSubject.create()
    private val hasNoFavoriteStores: PublishSubject<Signal> = PublishSubject.create()
    private val errorStream: PublishSubject<Failure> = PublishSubject.create()

    private var currentStoresCount: Int = 0 // 現在何件まで取得済みかを格納する変数
    private val favoriteIds: MutableList<String> = ArrayList()

    private val disposables = CompositeDisposable()

    companion object {
        private const val LIMIT = 20
    }

    override fun fetchFavoriteStores(forceRefresh: Boolean) {
        if (forceRefresh) {
            favoriteIds.clear()
            currentStoresCount = 0
        }

        if (favoriteIds.isEmpty()) {
            dataManager.fetchLocalStoreIds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy { storeIds ->
                    if (storeIds.isEmpty()) {
                        hasNoFavoriteStores.onNext(Signal)
                    }
                    StoreMapper.transform(storeIds).map { favoriteIds.add(it.storeId) }
                    createStoreIdQuery(favoriteIds)?.let { fetchStores(it) }
                }.addTo(disposables)
        } else {
            createStoreIdQuery(favoriteIds)?.let { fetchStores(it) }
        }
    }

    override fun getFavoriteStoreStream(): Observable<StoresResponse> = favoriteStoreList.hide()

    override fun getHasNoFavoriteStream(): Observable<Signal> = hasNoFavoriteStores.hide()

    override fun getErrorStream(): Observable<Failure> = errorStream.hide()

    private fun createStoreIdQuery(storeIds: MutableList<String>): String? {
        var favoriteStoreIds: String? = null

        val nextListCount = if (storeIds.size - currentStoresCount < LIMIT) {
            storeIds.size - currentStoresCount
        } else {
            LIMIT
        }

        // queryに含められるstore_idが20件までのため
        storeIds.subList(currentStoresCount, nextListCount).forEach { storeId ->
            favoriteStoreIds = if (favoriteStoreIds.isNullOrBlank()) {
                storeId
            } else {
                "$favoriteStoreIds,${storeId}"
            }
        }
        return favoriteStoreIds
    }

    private fun fetchStores(ids: String) {
        dataManager.fetchFavoriteStores(ids)
            .subscribeBy { response ->
                favoriteStoreList.onNext(response.body())
                response.body()?.results?.totalPages?.let { currentStoresCount = it }
            }.addTo(disposables)
    }

    override fun dispose() {
        disposables.clear()
    }
}
