package com.example.app.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app.viewModel.UtenteViewModel

@Composable
fun ProfileScreen(
    utenteViewModel: UtenteViewModel,
    session: String
) {
    val context = LocalContext.current
    val utenti by utenteViewModel.utenti.collectAsState(initial = listOf())
    Column() {
        Text(text = utenteViewModel.session.collectAsState(initial = "default").value)
        if(utenti.isNotEmpty() || utenteViewModel.utenteLoggato != null) {
            val utenteLoggato = if (utenteViewModel.utenteLoggato == null)
                utenti.find { it.username == session }!! else utenteViewModel.utenteLoggato!!

            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data(utenteLoggato.icona)
                    .crossfade(true)
                    .build(),
                contentDescription = "foto profilo"
            )
        }
    }
}