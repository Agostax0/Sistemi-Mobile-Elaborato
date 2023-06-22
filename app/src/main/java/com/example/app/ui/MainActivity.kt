package com.example.app.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.android.volley.RequestQueue
import com.example.app.NavigationApp
import com.example.app.R
import com.example.app.data.LocationDetails
import com.example.app.ui.theme.FoodAppTheme
import com.example.app.viewModel.LocationViewModel
import com.example.app.viewModel.SettingsViewModel
import com.example.app.viewModel.UtenteViewModel
import com.example.app.viewModel.WarningViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var requestingLocationUpdates = mutableStateOf(false)
    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>

    private val warningViewModel by viewModels<WarningViewModel>()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val utenteViewModel: UtenteViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            } else {
                warningViewModel.setPermissionSnackBarVisibility(true)
            }
        }

        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).apply {
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                locationViewModel.updateLocation(LocationDetails(
                    p0.locations.first().latitude,
                    p0.locations.first().longitude
                ))
                stopLocationUpdates()
            }
        }

        setContent {
            val theme by settingsViewModel.theme.collectAsState(initial = "")
            val session by utenteViewModel.session.collectAsState(initial = "default")
            FoodAppTheme (darkTheme = theme == getString(R.string.dark_theme)) {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavigationApp(session= session, startLocationUpdates = ::startLocationUpdates)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates.value) startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun startLocationUpdates() {
        requestingLocationUpdates.value = true

        val permission =   Manifest.permission.ACCESS_COARSE_LOCATION

        when {
            //permission already granted
            ContextCompat.checkSelfPermission (this, permission) == PackageManager.PERMISSION_GRANTED -> {
                locationRequest =
                    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).apply {
                        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                        setWaitForAccurateLocation(true)
                    }.build()

                val gpsEnabled = checkGPS()
                if (gpsEnabled) {
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                } else {
                    warningViewModel.setGPSAlertDialogVisibility(true)
                }
            }
            //permission already denied
            shouldShowRequestPermissionRationale(permission) -> {
                warningViewModel.setPermissionSnackBarVisibility(true)
            }
            else -> {
                //first time: ask for permissions
                locationPermissionRequest.launch(
                    permission
                )
            }
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun checkGPS(): Boolean {
        val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}