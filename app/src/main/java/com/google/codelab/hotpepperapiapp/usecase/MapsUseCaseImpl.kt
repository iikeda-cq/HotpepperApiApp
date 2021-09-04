package com.google.codelab.hotpepperapiapp.usecase

import com.google.codelab.hotpepperapiapp.data.SearchDataManager
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import com.google.codelab.hotpepperapiapp.model.businessmodel.StoreListBusinessModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MapsUseCaseImpl @Inject constructor(
    private val dataManager: SearchDataManager
) : MapsUseCase {
    override fun fetchStores(): Single<StoreListBusinessModel> {
        return dataManager.fetchStores()

    }

    override fun saveLocation(lat: Double, lng: Double) {
        dataManager.saveLocation(lat, lng)
    }
}
