package com.google.codelab.hotpepperapiapp.viewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.google.codelab.hotpepperapiapp.usecase.MapsUseCase
import com.google.codelab.hotpepperapiapp.usecase.MapsUseCaseImpl
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

class MapsViewModel: ViewModel() {
    private val usecase: MapsUseCase = MapsUseCaseImpl()
    val storesList: PublishSubject<StoresResponse> = PublishSubject.create()

    fun fetchStores(lat: Double, lng: Double) {
        usecase.fetchStores(lat, lng)
            .subscribeBy { stores ->
                storesList.onNext(stores)
            }
    }
}
