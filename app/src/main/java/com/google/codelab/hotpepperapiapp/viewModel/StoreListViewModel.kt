package com.google.codelab.hotpepperapiapp.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.codelab.hotpepperapiapp.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.StoreListUseCase
import com.google.codelab.hotpepperapiapp.usecase.StoreListUseCaseImpl

class StoreListViewModel {
    private val usecase: StoreListUseCase = StoreListUseCaseImpl()
    private val _storeRepos: MutableLiveData<StoresResponse> = MutableLiveData()
    val storeRepos: LiveData<StoresResponse> = _storeRepos

    @SuppressLint("CheckResult")
    fun fetchStores(lat: Double, lng: Double) {
        usecase.fetchStores(lat, lng)
            .subscribe { storeListRepos: StoresResponse ->
                _storeRepos.postValue(storeListRepos)
            }
    }
}
