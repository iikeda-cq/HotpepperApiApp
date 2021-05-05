package com.google.codelab.hotpepperapiapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Store : RealmObject() {
    @PrimaryKey
    var id: Long = 0

    var storeId: String = ""
    var image: Int = 0
    var name: String = ""
    var price: String = ""
    var lat: Double = 0.0
    var lng: Double = 0.0
    var genre: String = ""
    var url: String = ""
}
