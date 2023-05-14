package com.example.app.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app.R
import com.example.app.data.entity.Ristorante
import com.example.app.ui.theme.Green
import com.example.app.viewModel.RistoranteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onMapButtonClicked: ()-> Unit,
               onRestaurantClicked: ()->Unit,
               ristoranteViewModel: RistoranteViewModel,
               modifier: Modifier = Modifier
){
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick =  onMapButtonClicked ) {
                Icon(Icons.Filled.Map, contentDescription = "Map")
            }
        },
    ) { innerPadding ->
        Column (modifier.padding(innerPadding)) {
            RistorantiList(onRestaurantClicked, ristoranteViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RistorantiList(
    onRistoranteClicked: () -> Unit,
    ristoranteViewModel: RistoranteViewModel) {

    val ristoranti = ristoranteViewModel.ristoranti.collectAsState(initial = listOf()).value
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        content = {
            items(items= ristoranti) { ristorante ->
                Card(
                    onClick =  {
                        ristoranteViewModel.selectRistorante(ristorante)
                        onRistoranteClicked()
                    },
                    modifier = Modifier
                        .size(width = 150.dp, height = 60.dp)
                        .padding(3.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor =  MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(all = 2.dp)
                            .fillMaxSize()
                            .align(CenterHorizontally),
                        verticalAlignment = CenterVertically
                    ) {
                        /*AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                            .data(Uri.parse(ristorante.icona))
                            .crossfade(true)
                            .build(),
                            contentDescription = "immagine ristorante",
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .size(size = 50.dp))
                        }*/
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "immagine ristorante",
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .size(size = 50.dp),
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSecondaryContainer)
                        )
                        val scroll = rememberScrollState(0)
                        Text(
                            text = ristorante.nome,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .verticalScroll(scroll)
                                .weight(1f)
                        )

                        if(isRistoranteAperto(ristorante)){
                            Text(
                                text = "Aperto",
                                fontSize = 15.sp,
                                color = Green,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .verticalScroll(scroll)
                                    .width(100.dp)
                            )
                        } else {
                            Text(
                                text = "Chiuso",
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .verticalScroll(scroll)
                                    .width(100.dp)
                            )
                        }

                    }
                }
            }
        })
}

private fun isRistoranteAperto(ristorante: Ristorante): Boolean {
    return false
}