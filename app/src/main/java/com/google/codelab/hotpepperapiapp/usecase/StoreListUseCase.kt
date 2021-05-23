package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single

interface StoreListUseCase {
    fun fetchStores(
        lat: Double,
        lng: Double,
        start: Int
    ): Single<StoresResponse>
}
