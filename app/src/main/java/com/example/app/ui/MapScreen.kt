package com.example.app.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.app.R
import com.example.app.data.entity.Ristorante
import com.example.app.viewModel.LocationViewModel
import com.example.app.viewModel.RistoranteTipoRistoranteViewModel
import com.example.app.viewModel.RistoranteViewModel
import com.example.app.viewModel.UtenteScansionaRistoranteViewModel
import com.example.app.viewModel.UtenteViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen(
    locationViewModel: LocationViewModel,
    ristoranteViewModel: RistoranteViewModel,
    tipoRistoranteViewModel: RistoranteTipoRistoranteViewModel,
    utenteViewModel: UtenteViewModel,
    utenteScansionaRistoranteViewModel: UtenteScansionaRistoranteViewModel,
    session: String,
    onRestaurantClicked: () -> Unit
){
    val utenti by utenteViewModel.utenti.collectAsState(initial = listOf())
    if(utenti.isNotEmpty() || utenteViewModel.utenteLoggato != null) {
        val utenteLoggato = if (utenteViewModel.utenteLoggato == null)
            utenti.find { it.username == session }!!
        else utenteViewModel.utenteLoggato!!
        val ristoranti = tipoRistoranteViewModel.tipiRistoranti.collectAsState(initial = listOf()).value
        val listaPreferiti = utenteScansionaRistoranteViewModel.getRistorantiPreferitiPerUtente(utenteLoggato.ID.toString()).collectAsState(
            initial = listOf()
        ).value

        val location = locationViewModel.location.value
        Log.d("LOCATION", location.toString())
        val currentPosition = LatLng(location.latitude, location.longitude)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(currentPosition, 10f)
        }
        val icon = bitMapFromVector(LocalContext.current, R.drawable.baseline_person_pin_24)

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(currentPosition),
                title = "TU",
                icon = icon
            )
            ristoranti.forEach { pair ->
                val ristorante = pair.ristorante
                val tipoRistorante = pair.tipi[0]
                val ristorantePosition = ristorante.posizione.split(";")
                val customIcon = BitmapDescriptorFactory.defaultMarker(
                    if(listaPreferiti.contains(ristorante.COD_RIS)) BitmapDescriptorFactory.HUE_YELLOW
                            else BitmapDescriptorFactory.HUE_RED)

                CustomMarker(
                    position = LatLng(ristorantePosition[0].toDouble(), ristorantePosition[1].toDouble()),
                    ristorante = ristorante,
                    snippet = tipoRistorante.nomeTipo,
                    ristoranteViewModel = ristoranteViewModel,
                    onRestaurantClicked = onRestaurantClicked,
                    icon = customIcon,
                    isAperto = ristoranteViewModel.isRistoranteAperto(ristorante)
                )
            }
        }
    }
}

@Composable
fun CustomMarker(
    position: LatLng,
    ristorante: Ristorante,
    snippet: String,
    ristoranteViewModel: RistoranteViewModel,
    onRestaurantClicked: () -> Unit,
    icon: BitmapDescriptor,
    isAperto: Boolean
) {
    val markerState = MarkerState(position)
    Marker(
        state = markerState,
        title = ristorante.nome,
        snippet = snippet + " - " + if(isAperto) "Aperto" else "Chiuso",
        icon = icon,
        onClick = {
            ristoranteViewModel.selectRistorante(null)
            false
        },
        onInfoWindowClick = {
            ristoranteViewModel.selectRistorante(ristorante)
        },
        onInfoWindowClose = {
            if(ristoranteViewModel.ristoranteSelected != null) onRestaurantClicked()
        }
    )
}
private fun bitMapFromVector(context: Context,vectorResID:Int):BitmapDescriptor {
    val vectorDrawable=ContextCompat.getDrawable(context,vectorResID)
    vectorDrawable!!.setBounds(0,0,vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight)
    val bitmap=Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
    val canvas=Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return fromBitmap(bitmap)
}
