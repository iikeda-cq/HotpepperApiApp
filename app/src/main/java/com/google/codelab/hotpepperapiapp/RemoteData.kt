package com.google.codelab.hotpepperapiapp

import com.google.codelab.hotpepperapiapp.ApiClient.retrofit
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException

class RemoteData {
    // 参考：https://tech.mti.co.jp/entry/2020/03/31/163321
    fun fetchStores(
        lat: Double,
        lng: Double,
    ): Single<StoresResponse> {
        return retrofit.create(ApiRequest::class.java).fetchNearStores(
            key, COUNT, lat, lng, RANGE, JSON
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                return@map it.body()
                    ?: throw IOException("failed to fetch")
            }
    }

    companion object {
        private const val key = "970479567de67028"
        private const val JSON = "json"
        private const val COUNT = 20
        private const val RANGE = 3
    }
}
