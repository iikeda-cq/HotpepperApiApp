package com.google.codelab.hotpepperapiapp.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.codelab.hotpepperapiapp.usecase.MapsUseCase
import com.google.codelab.hotpepperapiapp.usecase.MapsUseCaseImpl
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse

class MapsViewModel {
    private val usecase: MapsUseCase = MapsUseCaseImpl()
    private val _storeRepos: MutableLiveData<StoresResponse> = MutableLiveData()
    val storeRepos: LiveData<StoresResponse> = _storeRepos

    @SuppressLint("CheckResult")
    fun fetchStores(lat: Double, lng: Double) {
        usecase.fetchStores(lat, lng)
            .subscribe { storeRepos: StoresResponse ->
                _storeRepos.postValue(storeRepos)
            }
    }
}
