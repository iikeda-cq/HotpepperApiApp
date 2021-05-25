package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single

interface FavoriteStoreUseCase {
    fun fetchFavoriteStores(storeId: String): Single<StoresResponse>
}
