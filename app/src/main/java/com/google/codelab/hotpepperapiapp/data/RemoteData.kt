package com.google.codelab.hotpepperapiapp.data

import com.google.codelab.hotpepperapiapp.ApiRequest
import com.google.codelab.hotpepperapiapp.BuildConfig
import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Inject

class RemoteData @Inject constructor(
    private val api: ApiRequest
) {
    companion object {
        private const val FORMAT = "json"
        private const val COUNT = 20
        private const val RANGE = 3
    }

    // 参考：https://tech.mti.co.jp/entry/2020/03/31/163321
    fun fetchStores(
        lat: Double,
        lng: Double,
        start: Int = 1
    ): Single<Response<StoresResponse>> {
        return api.fetchNearStores(
            BuildConfig.API_KEY, COUNT, lat, lng, start, RANGE, FORMAT
        )
    }

    fun fetchFavoriteStores(
        storeId: String
    ): Single<Response<StoresResponse>> {
        return api.fetchFavoriteStores(
            BuildConfig.API_KEY, COUNT, storeId, FORMAT
        )
    }
}
