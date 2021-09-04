package com.google.codelab.hotpepperapiapp.viewModel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.Signal
import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.getMessage
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoreUseCase
import com.google.codelab.hotpepperapiapp.model.businessmodel.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class FavoriteStoreViewModel @Inject constructor(
    private val usecase: FavoriteStoreUseCase
) : ViewModel() {
    val favoriteStoresList: PublishSubject<List<Store>> =
        PublishSubject.create()
    val hasNoFavoriteStores: PublishSubject<Signal> = PublishSubject.create()
    val errorStream: PublishSubject<Failure> = PublishSubject.create()
    val showProgress = ObservableBoolean()
    val moreLoad = ObservableBoolean()

    private val disposables = CompositeDisposable()

    fun fetchFavoriteStores(forceRefresh: Boolean = false) {
        usecase.fetchFavoriteStores(forceRefresh)
        showProgress.set(true)
        moreLoad.set(true)

        usecase.getFavoriteStoreStream()
            .subscribeBy(
                onNext = {
                    if (it.size < 20) {
                        moreLoad.set(false)
                    }
                    favoriteStoresList.onNext(it)
                    showProgress.set(false)
                },
                onError = {
                    val f = Failure(getMessage(it)) {
                        fetchFavoriteStores(forceRefresh)
                    }
                    showProgress.set(false)
                    errorStream.onNext(f)}
            ).addTo(disposables)

        usecase.getHasNoFavoriteStream()
            .subscribeBy { hasNoFavoriteStores.onNext(Signal) }
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
        usecase.dispose()
    }
}
