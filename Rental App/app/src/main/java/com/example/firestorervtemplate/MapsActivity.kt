package com.example.firestorervtemplate

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {




    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Setup the map zoom controls
        mMap.uiSettings.isZoomControlsEnabled = true

        // Add a marker in Downtown Toronto
        val downtownToronto = LatLng(43.651070, -79.347015)
        mMap.addMarker(
            MarkerOptions()
                .position(downtownToronto)
                .title("Downtown Toronto")
                // Assuming you have a custom icon for Downtown Toronto
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_brown_bear))
        )

        // Add more markers to the map
        val points = listOf(
            LatLng(43.642567, -79.387054), // CN Tower
            LatLng(43.653908, -79.384293), // Toronto City Hall
            LatLng(43.667710, -79.394777), // Royal Ontario Museum
            LatLng(43.648568, -79.381996), // Ripley's Aquarium of Canada
            LatLng(43.646547, -79.463690)  // High Park
        )

        // Iterate over the list of points to add markers for each location
        points.forEach { point ->
            mMap.addMarker(MarkerOptions().position(point).title("Marker at ${point.latitude},${point.longitude}"))
        }

        // Adjust the camera to show all markers
        val bounds = LatLngBounds.Builder().apply {
            include(downtownToronto)
            points.forEach { include(it) }
        }.build()

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

        // Enable My Location layer and related controls on the map
        updateLocationUI()
    }

    private fun updateLocationUI() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
        } else {
            mMap.isMyLocationEnabled = false
            mMap.uiSettings.isMyLocationButtonEnabled = false
        }
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        if (requestCode == LOCATION_PERMISSION_REQUEST) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission was granted. Update the location UI
//                updateLocationUI()
//            } else {
//                // Permission denied. Update the location UI accordingly
//                updateLocationUI()
//            }
//        }
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted. Update the location UI
                updateLocationUI()
            } else {
                // Permission denied. Update the location UI accordingly
                updateLocationUI()
            }
        }
    }
}