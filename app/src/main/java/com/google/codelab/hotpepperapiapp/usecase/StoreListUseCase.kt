package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.core.Single

interface StoreListUseCase {
    fun fetchStores(start: Int): Single<StoresResponse>

    fun checkLocationPermission(): Single<Boolean>
}
