package com.google.codelab.hotpepperapiapp.viewModel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.getMessage
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.StoreListUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class StoreListViewModel @Inject constructor(
    private val usecase: StoreListUseCaseImpl
) : ViewModel() {
    val storesList: PublishSubject<StoresResponse> = PublishSubject.create()
    val errorStream: PublishSubject<Failure> = PublishSubject.create()
    val showProgress = ObservableBoolean()
    val moreLoad = ObservableBoolean()
    private val disposables = CompositeDisposable()

    fun fetchStores(lat: Double, lng: Double, start: Int = 1) {
        usecase.fetchStores(lat, lng, start)
            .doOnSubscribe {
                showProgress.set(true)
                moreLoad.set(true)
            }
            .subscribeBy(
                onSuccess = { stores ->
                    if (stores.results.store.size < 20){
                        moreLoad.set(false)
                    }
                    storesList.onNext(stores)
                    showProgress.set(false)
                },
                onError = {
                    val f = Failure(getMessage(it)) {
                        fetchStores(lat, lng, start)
                    }
                    errorStream.onNext(f)
                    showProgress.set(false)
                }
            ).addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
