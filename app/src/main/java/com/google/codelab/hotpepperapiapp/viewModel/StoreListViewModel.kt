package com.google.codelab.hotpepperapiapp.viewModel

import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.Failure
import com.google.codelab.hotpepperapiapp.FailureType
import com.google.codelab.hotpepperapiapp.getMessage
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.StoreListUseCase
import com.google.codelab.hotpepperapiapp.usecase.StoreListUseCaseImpl
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject

class StoreListViewModel : ViewModel() {
    private val usecase: StoreListUseCase = StoreListUseCaseImpl()
    val storesList: PublishSubject<StoresResponse> = PublishSubject.create()
    val errorStream: PublishSubject<Failure> = PublishSubject.create()

    fun fetchStores(lat: Double, lng: Double, start: Int = 1) {
        usecase.fetchStores(lat, lng, start)
            .subscribeBy(
                onSuccess = { stores ->
                    storesList.onNext(stores)
                },
                onError = {
                    val f = Failure(getMessage(it)) {
                        fetchStores(lat, lng, start)
                    }
                    errorStream.onNext(f)
                }
            )
    }
}
