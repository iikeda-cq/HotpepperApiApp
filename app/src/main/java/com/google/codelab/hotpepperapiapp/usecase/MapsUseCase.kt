package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.model.businessmodel.StoreListBusinessModel
import io.reactivex.rxjava3.core.Single

interface MapsUseCase {
    fun fetchStores(): Single<StoreListBusinessModel>

    fun saveLocation(lat :Double, lng: Double)
}
