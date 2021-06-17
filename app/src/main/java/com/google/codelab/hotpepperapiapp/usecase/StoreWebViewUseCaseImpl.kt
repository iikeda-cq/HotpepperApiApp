package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import com.google.codelab.hotpepperapiapp.model.Store
import io.reactivex.Completable
import io.reactivex.Single

class StoreWebViewUseCaseImpl: StoreWebViewUseCase {
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
