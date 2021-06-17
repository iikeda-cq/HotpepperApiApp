package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.model.Store
import io.reactivex.Completable
import io.reactivex.Single

interface StoreWebViewUseCase {
    fun addFavoriteStore(storeId: String): Completable

    fun deleteFavoriteStore(storeId: String): Completable

    fun fetchFavoriteStore(storeId: String): Single<Boolean>
}
