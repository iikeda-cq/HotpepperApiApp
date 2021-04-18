package com.google.codelab.hotpepperapiapp

class NearStoresResponse {

    lateinit var results: Array<Results>


    data class Results(
        val results_returned: Int,
        val shop: Array<Shop>
    )

    data class Shop(
        val id: Int,
        val name: String,
        val logo_image: String,
        val lat: Double,
        val lng: Double,
        val genre: Array<Genre>,
        val budget: Array<Budget>,
        val urls: Array<Urls>
    )

    data class Genre(
        val name: String,
        val catch: String
    )


    data class Budget(
        val average: String
    )

    data class Urls(
        val pc: String
    )
}
