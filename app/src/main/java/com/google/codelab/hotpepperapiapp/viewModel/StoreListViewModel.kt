package com.google.codelab.hotpepperapiapp.viewModel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.getMessage
import com.google.codelab.hotpepperapiapp.usecase.StoreListUseCaseImpl
import com.google.codelab.hotpepperapiapp.model.businessmodel.StoreListBusinessModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class StoreListViewModel @Inject constructor(
    private val usecase: StoreListUseCaseImpl
) : ViewModel() {
    val storesList: PublishSubject<StoreListBusinessModel> = PublishSubject.create()
    val errorStream: PublishSubject<Failure> = PublishSubject.create()
    val showProgress = ObservableBoolean()
    val moreLoad = ObservableBoolean()
    val hasLocation: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val disposables = CompositeDisposable()

    fun fetchStores(start: Int = 1) {
        usecase.fetchStores(start)
            .doOnSubscribe {
                showProgress.set(true)
                moreLoad.set(true)
            }
            .subscribeBy(
                onSuccess = { stores ->
                    if (stores.store.size < 20){
                        moreLoad.set(false)
                    }
                    storesList.onNext(stores)
                    showProgress.set(false)
                },
                onError = {
                    val f = Failure(getMessage(it)) {
                        fetchStores(start)
                    }
                    errorStream.onNext(f)
                    showProgress.set(false)
                }
            ).addTo(disposables)
    }

    fun checkLocationPermission() {
        usecase.checkLocationPermission()
            .subscribeBy { hasLocation.onNext(it) }
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
