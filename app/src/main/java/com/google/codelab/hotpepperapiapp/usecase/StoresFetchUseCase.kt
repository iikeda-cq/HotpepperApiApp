package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.StoresResponse
import io.reactivex.Single

interface StoresFetchUseCase {
    fun fetchStores(
        key: String,
        count: Int,
        lat: Double,
        lng: Double,
        range: Int,
        format: String
    ): Single<StoresResponse>
}
