package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.core.Single

interface MapsUseCase {
    fun fetchStores(): Single<StoresResponse>

    fun saveLocation(lat :Double, lng: Double)
}
