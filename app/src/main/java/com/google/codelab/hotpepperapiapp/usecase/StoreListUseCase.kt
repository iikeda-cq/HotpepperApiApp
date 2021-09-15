package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.model.businessmodel.StoreListBusinessModel
import io.reactivex.rxjava3.core.Single

interface StoreListUseCase {
    fun fetchStores(start: Int): Single<StoreListBusinessModel>

    fun checkLocationPermission(): Single<Boolean>
}
