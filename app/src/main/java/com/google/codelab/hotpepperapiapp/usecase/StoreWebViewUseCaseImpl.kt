package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class StoreWebViewUseCaseImpl : StoreWebViewUseCase {
    private val dataManager = SearchDataManagerImpl()

    override fun addFavoriteStore(storeId: String): Completable {
        return dataManager.addFavoriteStore(storeId)
    }

    override fun deleteFavoriteStore(storeId: String): Completable {
        return dataManager.deleteFavoriteStore(storeId)
    }

    override fun fetchFavoriteStore(storeId: String): Single<Boolean> {
        return dataManager.fetchFavoriteStore(storeId)
    }
}
