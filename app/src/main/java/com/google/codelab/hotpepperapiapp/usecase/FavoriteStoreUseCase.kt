package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.model.Store
import com.google.codelab.hotpepperapiapp.model.StoreModel
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.RealmResults

interface FavoriteStoreUseCase {
    fun fetchFavoriteStores(storeId: String): Single<StoresResponse>

    fun fetchLocalStoreIds(): Single<MutableList<StoreModel>>
}
