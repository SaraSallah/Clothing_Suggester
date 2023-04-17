package com.example.clothing_suggester.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationResult
class LocationManager(private val context: Context, private val locationCallback: LocationCallback) {

    private var fusedLocationClient: FusedLocationProviderClient? = null

    fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 50000 // Update interval in milliseconds

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    fun stopLocationUpdates() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }
}