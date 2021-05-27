package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single

class MapsUseCaseImpl : MapsUseCase {
    private val dataManager: SearchDataManagerImpl = SearchDataManagerImpl()

    override fun fetchStores(
        lat: Double,
        lng: Double
    ): Single<StoresResponse> {
        return dataManager.fetchStores(lat, lng)
    }
}
