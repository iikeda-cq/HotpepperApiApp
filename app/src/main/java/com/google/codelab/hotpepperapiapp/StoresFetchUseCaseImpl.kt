package com.google.codelab.hotpepperapiapp

import io.reactivex.Single

class StoresFetchUseCaseImpl() : StoresFetchUseCase {
    private val remote: RemoteData = RemoteData()

    override fun fetchStores(
        key: String,
        count: Int,
        lat: Double,
        lng: Double,
        range: Int,
        format: String
    ): Single<StoresResponse> {
        return remote.fetchStores(key, count, lat, lng, range, format)
    }
}
