package com.example.app.ui

import android.app.Activity
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.android.volley.RequestQueue
import com.example.app.NavigationApp
import com.example.app.R
import com.example.app.data.LocationDetails
import com.example.app.ui.theme.FoodAppTheme
import com.example.app.viewModel.SettingsViewModel
import com.example.app.viewModel.UtenteViewModel
import com.example.app.viewModel.WarningViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var requestingLocationUpdates = mutableStateOf(false)
    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>
    val location =  mutableStateOf(LocationDetails(0.toDouble(), 0.toDouble()))

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private lateinit var connectivityManager : ConnectivityManager
    private var queue : RequestQueue? = null

    val warningViewModel by viewModels<WarningViewModel>()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val utenteViewModel: UtenteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val theme by settingsViewModel.theme.collectAsState(initial = "")
            val session by utenteViewModel.session.collectAsState(initial = "default")
            FoodAppTheme (darkTheme = theme == getString(R.string.dark_theme)) {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavigationApp(session)
                }
            }
        }
    }
}