package com.google.codelab.hotpepperapiapp.viewModel

import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.Signal
import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.StoreModel
import com.google.codelab.hotpepperapiapp.model.getMessage
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class FavoriteStoreViewModel @Inject constructor(
    private val usecase: FavoriteStoreUseCase
) : ViewModel() {
    val favoriteStoresList: PublishSubject<StoresResponse> = PublishSubject.create()
    val hasNoFavoriteStores: PublishSubject<Signal> = PublishSubject.create()
    val errorStream: PublishSubject<Failure> = PublishSubject.create()

    private val localStoreIdList: MutableList<StoreModel> = ArrayList()

    fun setup() {
        // Realmに登録されているお気に入りストアのIDを取得する
        usecase.fetchLocalStoreIds()

        usecase.getLocalStoresIdsStream()
            .subscribeBy {
                if (it.isEmpty()) {
                    hasNoFavoriteStores.onNext(Signal)
                } else {
                    localStoreIdList.addAll(it)
                    usecase.fetchFavoriteStores(it)
                }
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

        usecase.getErrorStream()
            .subscribeBy { errorStream.onNext(it) }
    }

    fun fetchFavoriteStores() {
        usecase.fetchFavoriteStores(localStoreIdList)
    }

    fun resetHasFavoriteIds() {
        localStoreIdList.clear()
        usecase.resetCurrentCount()
    }
}
