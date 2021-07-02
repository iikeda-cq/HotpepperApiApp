package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.model.StoreModel
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.core.Single

interface FavoriteStoreUseCase {
    fun fetchFavoriteStores(storeId: String): Single<StoresResponse>

    fun fetchLocalStoreIds(): Single<MutableList<StoreModel>>
}
