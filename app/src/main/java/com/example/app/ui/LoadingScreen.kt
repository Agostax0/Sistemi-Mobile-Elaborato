package com.example.app.ui

import android.util.Log
import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.app.viewModel.UtenteViewModel
import okhttp3.internal.wait

@Composable
fun LoadingScreen(
    utenteViewModel: UtenteViewModel,
    navigateToLogin: ()->Unit,
    navigateToHome: ()->Unit
){


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(model = "https://cdn.discordapp.com/attachments/336170905912737792/1119636309506535454/iu.png",
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(size = 50.dp)
                        .padding(horizontal = 5.dp)
                        .clip(shape = CircleShape))




        var session_value:String = utenteViewModel.session.collectAsState(initial = "default").value

        if(session_value.isNullOrEmpty()){
            navigateToLogin()
        }
        else if(session_value == "default"){

        }
        else{
            navigateToHome()
        }

    }


}