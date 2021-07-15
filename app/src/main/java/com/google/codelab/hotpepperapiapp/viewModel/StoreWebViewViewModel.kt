package com.google.codelab.hotpepperapiapp.viewModel

import androidx.databinding.ObservableBoolean
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
    val errorStream: PublishSubject<Signal> = PublishSubject.create()
    val hasFavoriteStore = ObservableBoolean()

    private val disposables = CompositeDisposable()

    // すでにお気に入りに登録済みかどうかをチェックする
    fun fetchFavoriteStore(storeId: String) {
        usecase.hasFavoriteStore(storeId)
            .subscribeBy(
                onSuccess = { isFavorite ->
                    hasFavoriteStore.set(isFavorite)
                },
                onError = {
                    errorStream.onNext(Signal)
                }
            ).addTo(disposables)
    }

    fun onClickFab(storeId: String) {
        if (hasFavoriteStore.get()) {
            deleteFavoriteStore(storeId)
        } else {
            addFavoriteStore(storeId)
        }
    }

    private fun addFavoriteStore(storeId: String) {
        usecase.addFavoriteStore(storeId)
            .subscribeBy(
                onComplete = {
                    addFavoriteStore.onNext(Signal)
                    toggleFavorite(hasFavoriteStore.get())
                },
                onError = {
                    errorStream.onNext(Signal)
                }
            ).addTo(disposables)
    }

    private fun deleteFavoriteStore(storeId: String) {
        usecase.deleteFavoriteStore(storeId)
            .subscribeBy(
                onComplete = {
                    deleteFavoriteStore.onNext(Signal)
                    toggleFavorite(hasFavoriteStore.get())
                },
                onError = {
                    errorStream.onNext(Signal)
                }
            ).addTo(disposables)
    }

    private fun toggleFavorite(isFavorite: Boolean) {
        hasFavoriteStore.set(!isFavorite)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
