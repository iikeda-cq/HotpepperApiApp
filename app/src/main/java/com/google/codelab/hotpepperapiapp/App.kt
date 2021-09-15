package com.google.codelab.hotpepperapiapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(applicationContext)
        val realm = RealmConfiguration
            .Builder()
            .build()
        Realm.setDefaultConfiguration(realm)
        Realm.getDefaultInstance()
    }
}
