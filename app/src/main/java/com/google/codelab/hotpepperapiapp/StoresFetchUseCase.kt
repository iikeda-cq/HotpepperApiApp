package com.google.codelab.hotpepperapiapp

import io.reactivex.Observable
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
