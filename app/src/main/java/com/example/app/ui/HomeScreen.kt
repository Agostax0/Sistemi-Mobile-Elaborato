package com.example.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.app.R
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
                        .size(width = 150.dp, height = 150.dp)
                        .padding(8.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor =  MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    /*Column(
                        modifier = Modifier
                            .padding(all = 12.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (place.travelPhoto.isEmpty()) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_android_24),
                                contentDescription = "travel image",
                                modifier = Modifier
                                    .clip(shape = CircleShape)
                                    .size(size = 50.dp),
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSecondaryContainer)
                            )
                        } else {
                            AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                                .data(Uri.parse(place.travelPhoto))
                                .crossfade(true)
                                .build(),
                                contentDescription = "image of the place",
                                modifier = Modifier
                                    .clip(shape = CircleShape)
                                    .size(size = 50.dp))
                        }

                        val scroll = rememberScrollState(0)
                        Text(
                            text = place.placeName,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.verticalScroll(scroll)
                        )
                    }*/
                }
            }
        })
}