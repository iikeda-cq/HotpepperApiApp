package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.StoresResponse
import io.reactivex.Single

interface StoreListUseCase {
    fun fetchStores(
        lat: Double,
        lng: Double,
        start: Int
    ): Single<StoresResponse>
}
