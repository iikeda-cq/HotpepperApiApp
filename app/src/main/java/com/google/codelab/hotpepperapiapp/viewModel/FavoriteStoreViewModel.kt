package com.google.codelab.hotpepperapiapp.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoreUseCase
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoresUseCaseImpl
import io.reactivex.subjects.PublishSubject

class FavoriteStoreViewModel {
    private val usecase: FavoriteStoreUseCase = FavoriteStoresUseCaseImpl()
    val favoriteStoresList: PublishSubject<StoresResponse> = PublishSubject.create()

    @SuppressLint("CheckResult")
    fun fetchFavoriteStores(storeId: String) {
        usecase.fetchFavoriteStores(storeId)
            .subscribe { stores ->
                favoriteStoresList.onNext(stores)
            }
    }
}
