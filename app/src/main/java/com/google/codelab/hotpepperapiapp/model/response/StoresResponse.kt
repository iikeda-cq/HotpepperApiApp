package com.google.codelab.hotpepperapiapp

import com.google.gson.annotations.SerializedName

data class StoresResponse(
    val results: Results,
)

data class Results(
    @SerializedName("api_version")
    val apiVersion: String,
    @SerializedName("results_available")
    val available: String,
    @SerializedName("shop")
    val store: List<Shop>
)

data class Shop(
    val id: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    val genre: Genre,
    val budget: Budget,
    val urls: Urls,
    val photo: Photos
)

data class Genre(
    val name : String
)

data class Budget(
    val name: String,
    val average: String
)

data class Urls(
    @SerializedName("pc")
    val url: String
)

data class Photos(
    @SerializedName("pc")
    val photo: Photo
)

data class Photo(
    @SerializedName("m")
    val logo: String
)
