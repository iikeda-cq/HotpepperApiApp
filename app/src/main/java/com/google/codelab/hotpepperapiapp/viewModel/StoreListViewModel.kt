package com.google.codelab.hotpepperapiapp.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.StoreListUseCase
import com.google.codelab.hotpepperapiapp.usecase.StoreListUseCaseImpl
import io.reactivex.subjects.PublishSubject

class StoreListViewModel {
    private val usecase: StoreListUseCase = StoreListUseCaseImpl()
    val storesList: PublishSubject<StoresResponse> = PublishSubject.create()

    @SuppressLint("CheckResult")
    fun fetchStores(lat: Double, lng: Double, start: Int = 1) {
        usecase.fetchStores(lat, lng, start)
            .subscribe { stores->
                storesList.onNext(stores)
            }
    }
}
