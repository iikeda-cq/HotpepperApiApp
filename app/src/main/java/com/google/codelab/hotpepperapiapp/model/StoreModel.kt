package com.google.codelab.hotpepperapiapp.model

data class StoreModel (
    val storeId: String,
    val image: Int,
    val name: String,
    val price: String,
    var lat: Double,
    var lng: Double,
    val genre: String,
    val url: String
)
