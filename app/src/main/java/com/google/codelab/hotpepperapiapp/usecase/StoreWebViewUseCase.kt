package com.google.codelab.hotpepperapiapp.usecase

import io.reactivex.Completable

interface StoreWebViewUseCase {
    fun addFavoriteStore(storeId: String): Completable

    fun deleteFavoriteStore(storeId: String): Completable
}
