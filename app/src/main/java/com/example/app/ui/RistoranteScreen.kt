package com.example.app.ui


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app.R
import com.example.app.data.entity.Ristorante
import com.example.app.viewModel.RistoranteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RistoranteScreen(ristoranteViewModel: RistoranteViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val selectedRistorante = ristoranteViewModel.ristoranteSelected
    Scaffold() { innerPadding ->
        Column (modifier.padding(innerPadding)) {
            Text(text = selectedRistorante!!.nome)
        }
    }
}