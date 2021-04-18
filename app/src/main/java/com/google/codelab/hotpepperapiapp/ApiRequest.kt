package com.google.codelab.hotpepperapiapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiRequest {
    @GET(".")
    @Headers("Content-Type: application/json")
    fun fetchNearStores(
        @Query("key") key: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("range") range: Int,
        @Query("format") format: String
    ): Call<NearStoresResponse>
}
