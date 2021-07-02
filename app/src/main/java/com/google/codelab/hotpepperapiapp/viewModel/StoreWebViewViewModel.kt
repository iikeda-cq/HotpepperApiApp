package com.google.codelab.hotpepperapiapp.viewModel

import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.Signal
import com.google.codelab.hotpepperapiapp.usecase.StoreWebViewUseCaseImpl
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject

class StoreWebViewViewModel : ViewModel() {
    private val usecase = StoreWebViewUseCaseImpl()
    val addFavoriteStore: PublishSubject<Signal> = PublishSubject.create()
    val deleteFavoriteStore: PublishSubject<Signal> = PublishSubject.create()
    val hasFavoriteStore: PublishSubject<Boolean> = PublishSubject.create()
    val errorStream: PublishSubject<Signal> = PublishSubject.create()
    val onClickFab: PublishSubject<Signal> = PublishSubject.create()

    fun addFavoriteStore(storeId: String) {
        usecase.addFavoriteStore(storeId)
            .subscribeBy(
                onComplete = {
                    addFavoriteStore.onNext(Signal)
                },
                onError = {
                    errorStream.onNext(Signal)
                }
            )
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
            )
    }

    fun fetchFavoriteStore(storeId: String) {
        usecase.fetchFavoriteStore(storeId)
            .subscribeBy(
                onSuccess = { isFavorite ->
                    hasFavoriteStore.onNext(isFavorite)
                },
                onError = {
                    errorStream.onNext(Signal)
                }
            )
    }

    fun onClickFab() {
        onClickFab.onNext(Signal)
    }
}
