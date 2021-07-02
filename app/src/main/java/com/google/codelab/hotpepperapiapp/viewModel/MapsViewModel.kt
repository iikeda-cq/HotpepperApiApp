package com.google.codelab.hotpepperapiapp.viewModel

import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.getMessage
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.MapsUseCase
import com.google.codelab.hotpepperapiapp.usecase.MapsUseCaseImpl
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject

class MapsViewModel : ViewModel() {
    private val usecase: MapsUseCase = MapsUseCaseImpl()
    val storesList: PublishSubject<StoresResponse> = PublishSubject.create()
    val errorStream: PublishSubject<Failure> = PublishSubject.create()

    fun fetchStores(lat: Double, lng: Double) {
        usecase.fetchStores(lat, lng)
            .subscribeBy(
                onSuccess = { stores ->
                    storesList.onNext(stores)
                },
                onError = {
                    val f = Failure(getMessage(it)) {
                        fetchStores(lat, lng)
                    }
                    errorStream.onNext(f)
                }
            )
    }
}
