package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.Signal
import com.google.codelab.hotpepperapiapp.data.SearchDataManager
import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.model.businessmodel.Store
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.lang.Integer.min
import javax.inject.Inject

class FavoriteStoresUseCaseImpl @Inject constructor(
    private val dataManager: SearchDataManager
) : FavoriteStoreUseCase {
    private val favoriteStoreList: PublishSubject<List<Store>> = PublishSubject.create()
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
                    fetchStores(createStoreIdQuery(storeIds))
                }.addTo(disposables)
        } else {
            fetchStores(createStoreIdQuery(favoriteIds))
        }
    }

    override fun getFavoriteStoreStream(): Observable<List<Store>> = favoriteStoreList.hide()

    override fun getHasNoFavoriteStream(): Observable<Signal> = hasNoFavoriteStores.hide()

    override fun getErrorStream(): Observable<Failure> = errorStream.hide()

    private fun createStoreIdQuery(storeIds: List<String>): String {
        val nextListCount = if (min(storeIds.size - currentStoresCount, LIMIT) > 0) {
            storeIds.size - currentStoresCount
        } else {
            LIMIT
        }

        // queryに含められるstore_idが20件までのため
        return storeIds.subList(currentStoresCount, nextListCount).joinToString(",")
    }

    private fun fetchStores(ids: String) {
        dataManager.fetchFavoriteStores(ids)
            .subscribeBy { model ->
                favoriteStoreList.onNext(model.store)
                currentStoresCount = model.totalPages
            }.addTo(disposables)
    }

    override fun dispose() {
        disposables.clear()
    }
}
