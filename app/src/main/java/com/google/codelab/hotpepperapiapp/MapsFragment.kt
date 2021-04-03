package com.google.codelab.hotpepperapiapp

import android.Manifest
import android.app.Activity
import android.content.Context
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
import com.google.codelab.hotpepperapiapp.FragmentExt.showFragment
import com.google.codelab.hotpepperapiapp.databinding.FragmentMapsBinding

class MapsFragment : Fragment(), OnMapReadyCallback {
    private val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1
    private var locationCallback: LocationCallback? = null
    private val dataSet: List<Store> = StoreListFragment().createTestData()

    // マーカーとViewPagerを紐づけるための変数
    private var mapMarkerPosition = 0

    private lateinit var binding: FragmentMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    val testData = StoreListFragment().createTestData()

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
            childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        binding.storePager.adapter =
            PagerStoreAdapter(dataSet, object : PagerStoreAdapter.ListListener {
                override fun onClickRow(tappedView: View, selectedBook: Store) {
                    val position = binding.storePager.currentItem

                    showFragment(
                        parentFragmentManager, StoreWebViewFragment.newInstance(
                            dataSet[position].name,
                            dataSet[position].url,
                            dataSet[position].price,
                            true
                        )
                    )
                }
            })

        binding.storePager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        Toast.makeText(requireContext(), "現在地周辺のお店${dataSet.size}件", Toast.LENGTH_SHORT).show()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkPermission()

        // マーカーのタップで一致するstoreデータを表示する
        mMap.setOnMarkerClickListener {
            binding.storePager.setCurrentItem(it.tag as Int, true)
            true
        }

        // ViewPagerのスクロールに合わせてマップ上のピンにフォーカスを合わせる
        binding.storePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val selectedStoreLatLng = LatLng(testData[position].lat, testData[position].lng)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedStoreLatLng, 14.0f))
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
                    myLocationEnable()
                } else {
                    Toast.makeText(context, "現在地は表示できません", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            myLocationEnable()
        } else {
            requestLocationPermission(requireContext(), requireActivity())
        }
    }

    private fun requestLocationPermission(context: Context, activity: Activity) {
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
            // まだ許可を求めていない
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    private fun myLocationEnable() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            val locationRequest = LocationRequest().apply {
                interval = 10000
                fastestInterval = 60000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    if (locationResult?.lastLocation != null) {
                        lastLocation = locationResult.lastLocation
                        val currentLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.0f))
                        storeMapping()
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

    private fun storeMapping() {
        mMap.clear()
        testData.mapIndexed { index, store ->
            store.lat = lastLocation.latitude.plus((index.toDouble() / 800 * index))
            store.lng = lastLocation.longitude.plus((index.toDouble() / 600 * index))
            addMarker(store)
        }
    }

    private fun addMarker(store: Store) {
        val pin: Marker = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(store.lat, store.lng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )

        pin.tag = mapMarkerPosition
        pin.showInfoWindow()
        mapMarkerPosition += 1
    }
}
