package com.google.codelab.hotpepperapiapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Store : RealmObject() {
    @PrimaryKey
    var id: Long = 0

    var storeId: String = ""
}
