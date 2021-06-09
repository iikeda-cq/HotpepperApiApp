package com.google.codelab.hotpepperapiapp.viewModel

import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.Failure
import com.google.codelab.hotpepperapiapp.FailureType
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

    fun fetchFavoriteStores(storeId: String) {
        usecase.fetchFavoriteStores(storeId)
            .subscribeBy(
                onSuccess = { stores ->
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
