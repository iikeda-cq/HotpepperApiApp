package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.data.SearchDataManager
import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class StoreWebViewUseCaseImpl @Inject constructor(
    private val dataManager: SearchDataManager
) : StoreWebViewUseCase {
    override fun addFavoriteStore(storeId: String): Completable {
        return dataManager.addFavoriteStore(storeId)
    }

    override fun deleteFavoriteStore(storeId: String): Completable {
        return dataManager.deleteFavoriteStore(storeId)
    }

    override fun hasFavoriteStore(storeId: String): Single<Boolean> {
        return dataManager.hasFavoriteStore(storeId)
    }
}
