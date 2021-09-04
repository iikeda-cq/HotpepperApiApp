package com.google.codelab.hotpepperapiapp.model.businessmodel

/**
 * アプリでデータを扱いやすくするために定義したBusinessModel
 * [StoreListMapper]を用いてAPIから取得したレスポンスをBusinessModelに変換する
 */
data class StoreListBusinessModel(
    val totalPages: Int,
    val store: List<Store>
)

data class Store(
    val id: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    val genre: String,
    val budget: String,
    val urls: String,
    val photo: String
)

