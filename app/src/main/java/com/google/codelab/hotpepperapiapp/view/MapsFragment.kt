package com.google.codelab.hotpepperapiapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.codelab.hotpepperapiapp.ext.FragmentExt.showFragmentBackStack
import com.google.codelab.hotpepperapiapp.ext.MapExt.checkPermission
import com.google.codelab.hotpepperapiapp.ext.MapExt.requestLocationPermission
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.model.Store
import com.google.codelab.hotpepperapiapp.view.StoreListFragment.Companion.createTestData
import com.google.codelab.hotpepperapiapp.databinding.FragmentMapsBinding
import com.google.codelab.hotpepperapiapp.model.StoreModel
import kotlin.random.Random

class MapsFragment : Fragment(), OnMapReadyCallback {
    private val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1
    private var locationCallback: LocationCallback? = null
    private val storeList: List<StoreModel> = createTestData()

    // マーカーとViewPagerを紐づけるための変数
    private var mapMarkerPosition = 0

    private lateinit var binding: FragmentMapsBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater)
        requireActivity().setTitle(R.string.view_map)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fragment_map) as? SupportMapFragment?
        mapFragment?.getMapAsync(this)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        binding.storePager.adapter =
            PagerStoreAdapter(storeList, object : PagerStoreAdapter.ListListener {
                override fun onClickRow(tappedView: View, selectedStore: StoreModel) {
                    val position = binding.storePager.currentItem

                    showFragmentBackStack(
                        parentFragmentManager, StoreWebViewFragment.newInstance(
                            storeList[position].storeId,
                            storeList[position].name,
                            storeList[position].url,
                            storeList[position].price,
                        )
                    )
                }
            })

        binding.storePager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        Toast.makeText(requireContext(), "現在地周辺のお店${storeList.size}件", Toast.LENGTH_SHORT).show()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        if(checkPermission(requireContext())) {
            enableMyLocation()
        } else {
            requestLocationPermission(requireContext(), requireActivity())
        }

        // マーカーのタップで一致するstoreデータを表示する
        map.setOnMarkerClickListener {
            binding.storePager.setCurrentItem(it.tag as Int, true)
            true
        }

        // ViewPagerのスクロールに合わせてマップ上のピンにフォーカスを合わせる
        binding.storePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val selectedStoreLatLng = LatLng(storeList[position].lat, storeList[position].lng)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedStoreLatLng, 16.0f))
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION -> {
                if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 許可された
                    enableMyLocation()
                } else {
                    Toast.makeText(context, R.string.no_locations, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            val locationRequest = LocationRequest().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    locationResult?.lastLocation?.let {  lastLocation ->
                        currentLocation = lastLocation
                        val currentLatLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.0f))
                        mapStore()
                    }
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    private fun mapStore() {
        map.clear()

        // テストように適当に現在地付近にマーカーを設定
        storeList.forEach { store ->
            store.lat = currentLocation.latitude.plus(Random.nextDouble(-9.0, 9.0) / 1000)
            store.lng = currentLocation.longitude.plus(Random.nextDouble(-9.0, 9.0) / 1000)
            addMarker(store)
        }
    }

    private fun addMarker(store: StoreModel) {
        val pin: Marker = map.addMarker(
            MarkerOptions()
                .position(LatLng(store.lat, store.lng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )

        pin.tag = mapMarkerPosition
        pin.showInfoWindow()
        mapMarkerPosition += 1
    }
}
