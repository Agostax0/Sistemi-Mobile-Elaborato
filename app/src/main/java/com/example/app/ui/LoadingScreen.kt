package com.example.app.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.app.data.entity.Utente
import com.example.app.viewModel.UtenteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun LoadingScreen(
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
    session: String
) {

    if (session != "default") {
        if (session == "") {
            navigateToLogin()
        } else {
            navigateToHome()

        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = "https://cdn.discordapp.com/attachments/336170905912737792/1119636309506535454/iu.png",
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(size = 50.dp)
                    .padding(horizontal = 5.dp)
                    .clip(shape = CircleShape)
            )
        }
    }
}