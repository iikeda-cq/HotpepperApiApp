package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.Signal
import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.StoreModel
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.model.businessmodel.Store
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface FavoriteStoreUseCase {
    fun fetchFavoriteStores(forceRefresh: Boolean)

    fun getFavoriteStoreStream(): Observable<List<Store>>

    fun getHasNoFavoriteStream(): Observable<Signal>

    fun getErrorStream(): Observable<Failure>

    fun dispose()
}
