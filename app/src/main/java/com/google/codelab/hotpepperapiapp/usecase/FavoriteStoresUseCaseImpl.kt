package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.RemoteData
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single

class FavoriteStoresUseCaseImpl : FavoriteStoreUseCase {
    private val remote: RemoteData = RemoteData()

    override fun fetchFavoriteStores(storeId: String): Single<StoresResponse> {
        return remote.fetchFavoriteStores(storeId)
    }
}
