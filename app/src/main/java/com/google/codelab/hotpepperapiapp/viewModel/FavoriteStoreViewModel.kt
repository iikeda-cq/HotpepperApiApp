package com.google.codelab.hotpepperapiapp.viewModel

import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.Failure
import com.google.codelab.hotpepperapiapp.FailureType
import com.google.codelab.hotpepperapiapp.StoreMapper
import com.google.codelab.hotpepperapiapp.getMessage
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoreUseCase
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoresUseCaseImpl
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

class FavoriteStoreViewModel : ViewModel() {
    private val usecase: FavoriteStoreUseCase = FavoriteStoresUseCaseImpl()
    val favoriteStoresList: PublishSubject<StoresResponse> = PublishSubject.create()
    val errorStream: PublishSubject<FailureType> = PublishSubject.create()

    var currentStoresCount: Int = 0
    var favoriteStoreIds: String? = null

    fun fetchLocalStoresId() {
        usecase.fetchLocalStoreIds()
            .subscribeBy { favoriteStoresList ->
                favoriteStoresList.drop(currentStoresCount)
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
                favoriteStoreIds?.let { fetchFavoriteStores(it) }
                favoriteStoreIds = null
            }
    }

    fun fetchFavoriteStores(storeId: String) {
        usecase.fetchFavoriteStores(storeId)
            .subscribeBy(
                onSuccess = { stores ->
                    currentStoresCount += stores.results.totalPages
                    favoriteStoresList.onNext(stores)
                },
                onError = {
                    val f = Failure(getMessage(it)) {
                        fetchFavoriteStores(storeId)
                    }
                    errorStream.onNext(f.message)
                }
            )
    }
}
