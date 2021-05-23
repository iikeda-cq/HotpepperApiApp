package com.google.codelab.hotpepperapiapp

import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {
    @GET(".")
    fun fetchNearStores(
        @Query("key") key: String,
        @Query("count") count: Int,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("start") start: Int,
        @Query("range") range: Int,
        @Query("format") format: String
    ): Single<Response<StoresResponse>>
}
