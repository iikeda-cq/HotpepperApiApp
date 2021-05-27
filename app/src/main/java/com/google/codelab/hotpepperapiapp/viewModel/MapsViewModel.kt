package com.google.codelab.hotpepperapiapp.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.codelab.hotpepperapiapp.usecase.MapsUseCase
import com.google.codelab.hotpepperapiapp.usecase.MapsUseCaseImpl
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.subjects.PublishSubject

class MapsViewModel {
    private val usecase: MapsUseCase = MapsUseCaseImpl()
    val storesList: PublishSubject<StoresResponse> = PublishSubject.create()

    @SuppressLint("CheckResult")
    fun fetchStores(lat: Double, lng: Double) {
        usecase.fetchStores(lat, lng)
            .subscribe { stores ->
                storesList.onNext(stores)
            }
    }
}
