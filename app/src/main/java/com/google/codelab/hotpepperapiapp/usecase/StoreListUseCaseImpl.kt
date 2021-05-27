package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single

class StoreListUseCaseImpl : StoreListUseCase {
    private val dataManager: SearchDataManagerImpl = SearchDataManagerImpl()

    override fun fetchStores(
        lat: Double,
        lng: Double,
        start: Int
    ): Single<StoresResponse> {
        return dataManager.fetchStores(lat, lng, start)
    }
}
