package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.core.Single

interface MapsUseCase {
    fun fetchStores(
        lat: Double,
        lng: Double
    ): Single<StoresResponse>
}
