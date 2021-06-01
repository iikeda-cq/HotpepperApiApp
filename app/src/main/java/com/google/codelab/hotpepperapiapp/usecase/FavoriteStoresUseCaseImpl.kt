package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException

class FavoriteStoresUseCaseImpl : FavoriteStoreUseCase {
    private val dataManager: SearchDataManagerImpl = SearchDataManagerImpl()

    override fun fetchFavoriteStores(storeId: String): Single<StoresResponse> {
        return dataManager.fetchFavoriteStores(storeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.body()
            }
    }
}
