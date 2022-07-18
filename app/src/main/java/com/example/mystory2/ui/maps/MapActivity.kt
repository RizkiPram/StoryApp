package com.example.mystory2.ui.maps


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mystory2.R
import com.example.mystory2.databinding.ActivityMapBinding
import com.example.mystory2.ui.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding:ActivityMapBinding
    private lateinit var mMap:GoogleMap
    private lateinit var viewModel:MapViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
        viewModel.getMap()

    }
    private fun setupViewModel(){
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[MapViewModel::class.java]
        viewModel.getMap()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_map_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.back ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        viewModel.userMap.observe(this){
            for (map in it){
                val getMap=LatLng(map.lat,map.lon)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getMap, 15f))
                mMap.addMarker(
                        MarkerOptions()
                            .position(getMap)
                            .title(map.name)
                            .snippet(map.description)
                )
            }
        }
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }
}