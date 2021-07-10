package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.StoreMapper
import com.google.codelab.hotpepperapiapp.model.StoreModel
import com.google.codelab.hotpepperapiapp.model.getMessage
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class FavoriteStoresUseCaseImpl @Inject constructor(
    private val dataManager: SearchDataManagerImpl
) : FavoriteStoreUseCase {
    private val localStoreIdList: PublishSubject<List<StoreModel>> = PublishSubject.create()
    private val favoriteStoreList: PublishSubject<StoresResponse> = PublishSubject.create()
    private val responseTotalPages: PublishSubject<Int> = PublishSubject.create()
    private val errorStream: PublishSubject<Failure> = PublishSubject.create()

    private var currentStoresCount: Int = 0 // 現在何件まで取得済みかを格納する変数

    override fun fetchFavoriteStores(storeIdList: List<StoreModel>) {
        var favoriteStoreIds: String? = null

        responseTotalPages.subscribeBy {
            currentStoresCount += it
        }

        // queryに含められるstore_idが20件までのため、すでにフェッチ済みのstore_idの数だけリストから削除する
        storeIdList.drop(currentStoresCount)
            .forEachIndexed { index, store ->
                // APIの仕様上、一度に20件までのデータしか取得できないため
                if (index < 20) {
                    favoriteStoreIds = if (favoriteStoreIds.isNullOrBlank()) {
                        store.storeId
                    } else {
                        favoriteStoreIds + "," + store.storeId
                    }
                }
            }

        dataManager.fetchFavoriteStores(favoriteStoreIds ?: return)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    responseTotalPages.onNext(it.body()?.results?.totalPages)
                    favoriteStoreList.onNext(it.body())
                },
                onError = {
                    val f = Failure(getMessage(it)) {
                        fetchFavoriteStores(storeIdList)
                    }
                    errorStream.onNext(f)
                }
            )
    }

    override fun fetchLocalStoreIds() {
        dataManager.fetchLocalStoreIds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { localStoreIdList.onNext(StoreMapper.transform(it)) }
    }

    override fun getLocalStoresIdsStream(): Observable<List<StoreModel>> = localStoreIdList.hide()

    override fun getFavoriteStoresStream(): Observable<StoresResponse> = favoriteStoreList.hide()

    override fun getErrorStream(): Observable<Failure> = errorStream.hide()
}
