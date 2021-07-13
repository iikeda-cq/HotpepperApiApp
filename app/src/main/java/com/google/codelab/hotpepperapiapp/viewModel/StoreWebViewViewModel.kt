package com.google.codelab.hotpepperapiapp.viewModel

import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.Signal
import com.google.codelab.hotpepperapiapp.usecase.StoreWebViewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class StoreWebViewViewModel @Inject constructor(private val usecase: StoreWebViewUseCase) :
    ViewModel() {
    val addFavoriteStore: PublishSubject<Signal> = PublishSubject.create()
    val deleteFavoriteStore: PublishSubject<Signal> = PublishSubject.create()
    val hasFavoriteStore: PublishSubject<Boolean> = PublishSubject.create()
    val errorStream: PublishSubject<Signal> = PublishSubject.create()
    val onClickFab: PublishSubject<Signal> = PublishSubject.create()

    private val disposables = CompositeDisposable()

    fun addFavoriteStore(storeId: String) {
        usecase.addFavoriteStore(storeId)
            .subscribeBy(
                onComplete = {
                    addFavoriteStore.onNext(Signal)
                },
                onError = {
                    errorStream.onNext(Signal)
                }
            ).addTo(disposables)
    }

    fun deleteFavoriteStore(storeId: String) {
        usecase.deleteFavoriteStore(storeId)
            .subscribeBy(
                onComplete = {
                    deleteFavoriteStore.onNext(Signal)
                },
                onError = {
                    errorStream.onNext(Signal)
                }
            ).addTo(disposables)
    }

    fun fetchFavoriteStore(storeId: String) {
        usecase.hasFavoriteStore(storeId)
            .subscribeBy(
                onSuccess = { isFavorite ->
                    hasFavoriteStore.onNext(isFavorite)
                },
                onError = {
                    errorStream.onNext(Signal)
                }
            ).addTo(disposables)
    }

    fun onClickFab() {
        onClickFab.onNext(Signal)
    }
}
