package com.google.codelab.hotpepperapiapp.view

import android.Manifest
import android.content.pm.PackageManager
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
import com.google.android.gms.maps.model.LatLng
import com.google.codelab.hotpepperapiapp.viewModel.MapsViewModel
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.model.response.NearStore
import com.google.codelab.hotpepperapiapp.databinding.FragmentMapsBinding
import com.google.codelab.hotpepperapiapp.ext.FragmentExt.showFragmentBackStack
import com.google.codelab.hotpepperapiapp.ext.MapExt.addMarker
import com.google.codelab.hotpepperapiapp.ext.MapExt.checkPermission
import com.google.codelab.hotpepperapiapp.ext.MapExt.requestLocationPermission

class MapsFragment : Fragment(), OnMapReadyCallback {
    private val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1
    private var locationCallback: LocationCallback? = null
    private val storeList: MutableList<NearStore> = ArrayList()

    // マーカーとViewPagerを紐づけるための変数
    private var mapMarkerPosition = 0

    private lateinit var binding: FragmentMapsBinding
    private lateinit var viewModel: MapsViewModel
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater)
        requireActivity().setTitle(R.string.view_map)

        viewModel = MapsViewModel()

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
                override fun onClickRow(tappedView: View, selectedStore: NearStore) {
                    val position = binding.storePager.currentItem

                    showFragmentBackStack(
                        parentFragmentManager, StoreWebViewFragment.newInstance(
                            storeList[position].id,
                            storeList[position].urls.url
                        )
                    )
                }
            }, requireContext())

        // APIから店舗情報を取得したら地図にマッピングする
        viewModel.storeRepos.observe(viewLifecycleOwner, {
            it.results.store.map { store ->
                mapMarkerPosition = addMarker(map, store, mapMarkerPosition)
                storeList.add(store)
            }
            binding.storePager.adapter?.notifyDataSetChanged()
            Toast.makeText(
                requireContext(),
                "周辺の店舗が${it.results.store.size}件見つかりました",
                Toast.LENGTH_SHORT
            ).show()
        })

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
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedStoreLatLng, 18.0f))
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
                        MainActivity.lat = lastLocation.latitude
                        MainActivity.lng = lastLocation.longitude
                        val currentLatLng =
                            LatLng(lastLocation.latitude, lastLocation.longitude)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18.0f))

                        // APIからお店の情報を取得する
                        viewModel.fetchStores(lastLocation.latitude, lastLocation.longitude)
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
}
