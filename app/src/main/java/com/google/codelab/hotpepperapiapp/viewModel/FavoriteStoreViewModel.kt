package com.google.codelab.hotpepperapiapp.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoreUseCase
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoresUseCaseImpl
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

class FavoriteStoreViewModel: ViewModel() {
    private val usecase: FavoriteStoreUseCase = FavoriteStoresUseCaseImpl()
    val favoriteStoresList: PublishSubject<StoresResponse> = PublishSubject.create()

    fun fetchFavoriteStores(storeId: String) {
        usecase.fetchFavoriteStores(storeId)
            .subscribeBy { stores ->
                favoriteStoresList.onNext(stores)
            }
    }
}
