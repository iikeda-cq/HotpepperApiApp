package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.model.Failure
import com.google.codelab.hotpepperapiapp.model.StoreModel
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface FavoriteStoreUseCase {
    fun fetchFavoriteStores(storeIdList: List<StoreModel>)

    fun fetchLocalStoreIds()

    fun getLocalStoresIdsStream(): Observable<List<StoreModel>>

    fun getFavoriteStoresStream(): Observable<StoresResponse>

    fun getErrorStream(): Observable<Failure>
}
