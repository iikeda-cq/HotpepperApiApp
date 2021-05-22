package com.google.codelab.hotpepperapiapp

import androidx.databinding.ObservableArrayList
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.ArrayList

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
