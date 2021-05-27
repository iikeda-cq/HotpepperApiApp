package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.ApiClient.retrofit
import com.google.codelab.hotpepperapiapp.ApiRequest
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single
import retrofit2.Response

class RemoteData {
    // 参考：https://tech.mti.co.jp/entry/2020/03/31/163321
    fun fetchStores(
        lat: Double,
        lng: Double,
        start: Int = 1
    ): Single<Response<StoresResponse>> {
        return retrofit.create(ApiRequest::class.java).fetchNearStores(
            KEY, COUNT, lat, lng, start, RANGE, JSON
        )
    }

    fun fetchFavoriteStores(
        storeId: String
    ): Single<Response<StoresResponse>> {
        return retrofit.create(ApiRequest::class.java).fetchFavoriteStores(
            KEY, COUNT, storeId, JSON
        )
    }

    companion object {
        private const val KEY = "970479567de67028"
        private const val JSON = "json"
        private const val COUNT = 20
        private const val RANGE = 3
    }
}
