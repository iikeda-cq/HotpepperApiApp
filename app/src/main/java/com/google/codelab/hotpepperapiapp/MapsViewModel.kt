package com.google.codelab.hotpepperapiapp

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MapsViewModel {
    private val usecase: StoresFetchUseCase = StoresFetchUseCaseImpl()
    private val _storeRepos: MutableLiveData<StoresResponse> = MutableLiveData()
    val storeRepos: LiveData<StoresResponse> = _storeRepos


    companion object {
        private const val key = "970479567de67028"
        private const val JSON = "json"
        private const val COUNT = 20
        private const val RANGE = 3
    }

    @SuppressLint("CheckResult")
    fun fetchStores(lat: Double, lng: Double) {
        usecase.fetchStores(key, COUNT, lat, lng, RANGE, JSON)
            .subscribe { userRepos: StoresResponse ->
                _storeRepos.postValue(userRepos)
            }
    }
}
