package com.google.codelab.hotpepperapiapp.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.codelab.hotpepperapiapp.model.response.NearStore

object MapUtils {
    private const val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1

    fun hasLocationPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(context: Context, activity: Activity) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 許可を求め、拒否されていた場合
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    fun addMarker(map: GoogleMap, store: NearStore, position: Int): Int {
        val pin: Marker = map.addMarker(
            MarkerOptions()
                .position(LatLng(store.lat, store.lng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
        pin.tag = position
        return position + 1
    }
}
