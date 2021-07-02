package com.google.codelab.hotpepperapiapp.viewModel

import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.StoreModel
import com.google.codelab.hotpepperapiapp.model.getMessage
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoreUseCase
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoresUseCaseImpl
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject

class FavoriteStoreViewModel : ViewModel() {
    private val usecase: FavoriteStoreUseCase = FavoriteStoresUseCaseImpl()
    val favoriteStoresList: PublishSubject<StoresResponse> = PublishSubject.create()
    val errorStream: PublishSubject<Failure> = PublishSubject.create()

    private val localStoreIdList: MutableList<StoreModel> = ArrayList()

    fun setup() {
        // Realのお気に入りストアのIDを取得する
        usecase.fetchLocalStoreIds()

        usecase.getLocalStoresIdsStream()
            .subscribeBy {
                localStoreIdList.addAll(it)
                usecase.fetchFavoriteStores(it)
            }

        usecase.getFavoriteStoresStream()
            .subscribeBy(
                onNext = { stores ->
                    favoriteStoresList.onNext(stores)
                },
                onError = {
                    val f = Failure(getMessage(it)) {
                        usecase.getLocalStoresIdsStream()
                    }
                    errorStream.onNext(f)
                }
            )
    }

    fun fetchFavoriteStores() {
        usecase.fetchFavoriteStores(localStoreIdList)
    }
}
