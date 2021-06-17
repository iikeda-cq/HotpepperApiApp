package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.StoreMapper
import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import com.google.codelab.hotpepperapiapp.model.Store
import com.google.codelab.hotpepperapiapp.model.StoreModel
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
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

    override fun fetchLocalStoreIds(): Single<MutableList<StoreModel>> {
        return dataManager.fetchLocalStoreIds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { StoreMapper.transform(it) }
    }
}
