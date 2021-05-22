package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.RemoteData
import com.google.codelab.hotpepperapiapp.StoresResponse
import io.reactivex.Single

class StoreListUseCaseImpl : StoreListUseCase {
    private val remote: RemoteData = RemoteData()

    override fun fetchStores(
        lat: Double,
        lng: Double
    ): Single<StoresResponse> {
        return remote.fetchStores(lat, lng)
    }
}
