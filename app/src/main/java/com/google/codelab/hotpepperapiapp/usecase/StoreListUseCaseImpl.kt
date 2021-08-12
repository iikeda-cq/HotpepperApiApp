package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.data.SearchDataManager
import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class StoreListUseCaseImpl @Inject constructor(
    private val dataManager: SearchDataManager
): StoreListUseCase {

    override fun fetchStores(
        start: Int
    ): Single<StoresResponse> {
        return dataManager.fetchStores(start)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.body()
            }
    }

    override fun checkLocationPermission(): Single<Boolean> {
        return dataManager.hasLocation()
    }
}
