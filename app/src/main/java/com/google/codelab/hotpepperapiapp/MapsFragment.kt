package com.google.codelab.hotpepperapiapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
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
import com.google.codelab.hotpepperapiapp.ApiClient.retrofit
import com.google.codelab.hotpepperapiapp.FragmentExt.showFragmentBackStack
import com.google.codelab.hotpepperapiapp.MapExt.checkPermission
import com.google.codelab.hotpepperapiapp.MapExt.requestLocationPermission
import com.google.codelab.hotpepperapiapp.databinding.FragmentMapsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsFragment : Fragment(), OnMapReadyCallback {
    private val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1
    private var locationCallback: LocationCallback? = null
    private val storeList: MutableList<Shop> = ArrayList()
    private val TAG = MapsFragment::class.java.simpleName

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
                override fun onClickRow(tappedView: View, selectedStore: Shop) {
                    val position = binding.storePager.currentItem

                    showFragmentBackStack(
                        parentFragmentManager, StoreWebViewFragment.newInstance(
                            storeList[position].id,
                            storeList[position].urls.url
                        )
                    )
                }
            }, requireContext())

        binding.storePager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        if (checkPermission(requireContext())) {
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
                    locationResult?.lastLocation?.let { lastLocation ->
                        currentLocation = lastLocation
                        val currentLatLng =
                            LatLng(currentLocation.latitude, currentLocation.longitude)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.0f))
                        fetchStores()
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

    private fun fetchStores() {
        retrofit.create(ApiRequest::class.java).fetchNearStores(
            "970479567de67028",
            20,
            currentLocation.latitude,
            currentLocation.longitude,
            3,
            "json"
        ).enqueue(object :
            Callback<StoresResponse> {
            override fun onFailure(call: Call<StoresResponse>, t: Throwable?) {
                Toast.makeText(requireContext(), R.string.offline_error, Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Something wrong On API: offline")
            }

            override fun onResponse(
                call: Call<StoresResponse>,
                response: Response<StoresResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.results?.store?.map { store ->
                            addMarker(store)
                            storeList.add(store)
                        }

                        binding.storePager.adapter?.notifyDataSetChanged()

                        Toast.makeText(
                            requireContext(),
                            "現在地周辺のお店${storeList.size}件",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d(TAG, "success: $response")
                    }
                    else -> {
                        Log.w(TAG, "Something wrong On API: $response")
                    }
                }
            }
        })
    }

    private fun addMarker(store: Shop) {
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
