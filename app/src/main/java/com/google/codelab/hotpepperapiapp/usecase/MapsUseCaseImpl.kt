package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.RemoteData
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single

class MapsUseCaseImpl() : MapsUseCase {
    private val remote: RemoteData = RemoteData()

    override fun fetchStores(
        lat: Double,
        lng: Double
    ): Single<StoresResponse> {
        return remote.fetchStores(lat, lng)
    }
}
