package com.google.codelab.hotpepperapiapp.viewModel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.Signal
import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.StoreModel
import com.google.codelab.hotpepperapiapp.model.getMessage
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.usecase.FavoriteStoreUseCase
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
    val favoriteStoresList: PublishSubject<StoresResponse> = PublishSubject.create()
    val hasNoFavoriteStores: PublishSubject<Signal> = PublishSubject.create()
    val errorStream: PublishSubject<Failure> = PublishSubject.create()
    val showProgress = ObservableBoolean()

    private val localStoreIdList: MutableList<StoreModel> = ArrayList()
    private val disposables = CompositeDisposable()

    fun setup() {
        // Realmに登録されているお気に入りストアのIDを取得する
        usecase.fetchLocalStoreIds()

        usecase.getLocalStoresIdsStream()
            .doOnSubscribe { showProgress.set(true) }
            .subscribeBy {
                if (it.isEmpty()) {
                    hasNoFavoriteStores.onNext(Signal)
                    showProgress.set(false)
                } else {
                    localStoreIdList.addAll(it)
                    usecase.fetchFavoriteStores(it)
                }
            }.addTo(disposables)

        usecase.getFavoriteStoresStream()
            .subscribeBy(
                onNext = { stores ->
                    favoriteStoresList.onNext(stores)
                    showProgress.set(false)
                },
                onError = {
                    val f = Failure(getMessage(it)) {
                        usecase.getLocalStoresIdsStream()
                    }
                    errorStream.onNext(f)
                    showProgress.set(false)
                }
            ).addTo(disposables)

        usecase.getErrorStream()
            .subscribeBy { errorStream.onNext(it) }.addTo(disposables)
    }

    fun fetchFavoriteStores() {
        usecase.fetchFavoriteStores(localStoreIdList)
        showProgress.set(true)
    }

    fun resetHasFavoriteIds() {
        localStoreIdList.clear()
        usecase.resetCurrentCount()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
        usecase.dispose()
    }
}
