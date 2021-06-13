package com.google.codelab.hotpepperapiapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.databinding.FragmentMapsBinding
import com.google.codelab.hotpepperapiapp.ext.FragmentExt.showFragment
import com.google.codelab.hotpepperapiapp.ext.MapExt
import com.google.codelab.hotpepperapiapp.ext.MapExt.addMarker
import com.google.codelab.hotpepperapiapp.model.response.NearStore
import com.google.codelab.hotpepperapiapp.viewModel.MapsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

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
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        viewModel = ViewModelProviders.of(this).get(MapsViewModel::class.java)

        binding.isLoading = false

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
            PagerStoreAdapter(storeList, requireContext()){
                    val position = binding.storePager.currentItem

                    StoreWebViewFragment.newInstance(
                        storeList[position].id,
                        storeList[position].urls.url
                    ).showFragment(parentFragmentManager)
                }

        // APIから店舗情報を取得したら地図にマッピングする
        viewModel.storesList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                it.results.store.map { store ->
                    mapMarkerPosition = addMarker(map, store, mapMarkerPosition)
                    storeList.add(store)
                }
                binding.storePager.adapter?.notifyDataSetChanged()
                binding.isLoading = false

                Toast.makeText(
                    requireContext(),
                    "周辺の店舗が${it.results.store.size}件見つかりました",
                    Toast.LENGTH_SHORT
                ).show()
            }

        viewModel.errorStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { failure ->
                binding.isLoading = false
                Snackbar.make(view, failure.message, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry) {
                        viewModel.fetchStores(
                            MainActivity.lat!!,
                            MainActivity.lng!!
                        )
                    }.show()
            }

        binding.storePager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        if (MapExt.hasLocationPermission(requireContext())) {
            enableMyLocation()
        } else {
            MapExt.requestLocationPermission(requireContext(), requireActivity())
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
        binding.isLoading = true
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
                        binding.isLoading = true
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
