package com.google.codelab.hotpepperapiapp.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoreUseCase
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoresUseCaseImpl

class FavoriteStoreViewModel {
    private val usecase: FavoriteStoreUseCase = FavoriteStoresUseCaseImpl()
    private val _storeRepos: MutableLiveData<StoresResponse> = MutableLiveData()
    val storeRepos: LiveData<StoresResponse> = _storeRepos

    @SuppressLint("CheckResult")
    fun fetchFavoriteStores(storeId: String) {
        usecase.fetchFavoriteStores(storeId)
            .subscribe { storeRepos: StoresResponse ->
                _storeRepos.postValue(storeRepos)
            }
    }
}
