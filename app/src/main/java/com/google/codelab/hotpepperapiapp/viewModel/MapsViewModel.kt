package com.google.codelab.hotpepperapiapp.viewModel

import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.getMessage
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.MapsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val usecase: MapsUseCase) : ViewModel() {
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
