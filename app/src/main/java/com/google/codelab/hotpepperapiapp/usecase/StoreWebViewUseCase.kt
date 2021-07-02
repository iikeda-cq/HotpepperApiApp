package com.google.codelab.hotpepperapiapp.usecase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface StoreWebViewUseCase {
    fun addFavoriteStore(storeId: String): Completable

    fun deleteFavoriteStore(storeId: String): Completable

    fun fetchFavoriteStore(storeId: String): Single<Boolean>
}
