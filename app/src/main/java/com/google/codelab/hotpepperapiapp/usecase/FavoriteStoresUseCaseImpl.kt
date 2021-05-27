package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single

class FavoriteStoresUseCaseImpl : FavoriteStoreUseCase {
    private val dataManager: SearchDataManagerImpl = SearchDataManagerImpl()

    override fun fetchFavoriteStores(storeId: String): Single<StoresResponse> {
        return dataManager.fetchFavoriteStores(storeId)
    }
}
