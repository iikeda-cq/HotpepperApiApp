package com.google.codelab.hotpepperapiapp

import com.google.codelab.hotpepperapiapp.ApiClient.retrofit
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException

class RemoteData {
    // 参考：https://tech.mti.co.jp/entry/2020/03/31/163321
    fun fetchStores(
        key: String,
        count: Int,
        lat: Double,
        lng: Double,
        range: Int,
        format: String
    ): Single<StoresResponse> {
        return retrofit.create(ApiRequest::class.java).fetchNearStores(
            key, count, lat, lng, range, format
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                return@map it.body()
                    ?: throw IOException("failed to fetch")
            }
    }
}
