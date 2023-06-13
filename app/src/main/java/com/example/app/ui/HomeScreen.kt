package com.example.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.data.entity.FiltroConsegna
import com.example.app.data.entity.Ristorante
import com.example.app.data.relation.RistoranteFiltroConsegna
import com.example.app.ui.theme.Green
import com.example.app.viewModel.FiltroConsegnaViewModel
import com.example.app.viewModel.RistoranteFiltroConsegnaViewModel
import com.example.app.viewModel.RistoranteViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onMapButtonClicked: ()-> Unit,
               onRestaurantClicked: ()->Unit,
               onFiltersClicked: ()->Unit,
               ristoranteViewModel: RistoranteViewModel,
               filtroConsegnaViewModel: FiltroConsegnaViewModel,
               ristoranteFiltroConsegnaViewModel: RistoranteFiltroConsegnaViewModel,
               modifier: Modifier = Modifier
){
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick =  onMapButtonClicked ) {
                Icon(
                    Icons.Filled.Map,
                    contentDescription = "Map",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
    ) { innerPadding ->
        Column (modifier.padding(innerPadding)) {
            RistorantiList(onRestaurantClicked, onFiltersClicked, ristoranteViewModel, ristoranteFiltroConsegnaViewModel, filtroConsegnaViewModel)
        }
    }
}

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RistorantiList(
    onRistoranteClicked: () -> Unit,
    onFiltersClicked: ()->Unit,
    ristoranteViewModel: RistoranteViewModel,
    ristoranteFiltroConsegnaViewModel: RistoranteFiltroConsegnaViewModel,
    filtroConsegnaViewModel: FiltroConsegnaViewModel
) {
    val filtriSelezionati = filtroConsegnaViewModel.filtriSelezionati.collectAsState(initial = "").value
    val filtriPerRistorante = ristoranteFiltroConsegnaViewModel.filtriRistoranti.collectAsState(initial = listOf()).value
    val ristoranti = ristoranteViewModel.ristoranti.collectAsState(initial = listOf()).value
    var ricerca by rememberSaveable { mutableStateOf("") }
    val ristorantiFiltrati = filtroRistoranti(filtriPerRistorante,filtriSelezionati, ristoranteViewModel)
    val ristorantiCercati = ristorantiFiltrati.filter {ristorante -> ristorante.nome.lowercase().contains(ricerca.lowercase()) } //da cambiare per i filtri

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        content = {
            header {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = CenterVertically

                    ) {
                        TextField(
                            value = ricerca,
                            onValueChange = {
                                ricerca = it
                            },
                            placeholder = { Text(stringResource(id = R.string.ristorante_ricerca)) },
                            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "searchIcon") },
                            modifier = Modifier.weight(5.5f),
                            shape = CircleShape,
                            colors =  TextFieldDefaults.textFieldColors(
                                disabledTextColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                        IconButton(
                            onClick = onFiltersClicked,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Filled.FilterList,
                                contentDescription = stringResource(id = R.string.filters),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(35.dp)
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = CenterVertically,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        IconButton(
                            onClick = onFiltersClicked,
                            modifier = Modifier.weight(1.1f)
                        ) {
                            Icon(
                                Icons.Filled.Filter1,
                                contentDescription = stringResource(id = R.string.filters),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        IconButton(
                            onClick = onFiltersClicked,
                            modifier = Modifier.weight(1.1f)
                        ) {
                            Icon(
                                Icons.Filled.Filter2,
                                contentDescription = stringResource(id = R.string.filters),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        IconButton(
                            onClick = onFiltersClicked,
                            modifier = Modifier.weight(1.1f)
                        ) {
                            Icon(
                                Icons.Filled.Filter3,
                                contentDescription = stringResource(id = R.string.filters),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        IconButton(
                            onClick = onFiltersClicked,
                            modifier = Modifier.weight(1.1f)
                        ) {
                            Icon(
                                Icons.Filled.Filter4,
                                contentDescription = stringResource(id = R.string.filters),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        IconButton(
                            onClick = onFiltersClicked,
                            modifier = Modifier.weight(1.1f)
                        ) {
                            Icon(
                                Icons.Filled.Filter5,
                                contentDescription = stringResource(id = R.string.filters),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        IconButton(
                            onClick = onFiltersClicked,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = stringResource(id = R.string.filters),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }

            items(items= ristorantiCercati) { ristorante ->
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

                        if(ristoranteViewModel.isRistoranteAperto(ristorante)) {
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

fun filtroRistoranti(filtriPerRistorante: List<RistoranteFiltroConsegna>, filtriSelezionati: String, ristoranteViewModel: RistoranteViewModel): List<Ristorante> {
    val ristorantiFiltrati = mutableListOf<Ristorante>()
    filtriPerRistorante.forEach{ pair ->
        if(checkFiltri(filtriSelezionati, pair.ristorante, pair.filtri, ristoranteViewModel)) {
            ristorantiFiltrati.add(pair.ristorante)
        }
    }
    return ristorantiFiltrati
}

fun checkFiltri(filtriSelezionati: String, ristorante: Ristorante, filtriConsegna: List<FiltroConsegna>, ristoranteViewModel: RistoranteViewModel): Boolean {
    var flag = true
    if(filtriSelezionati[0] == '1') {
        if (!filtriConsegna.contains(FiltroConsegna("Consumazione sul posto"))) {
            flag = false
        }
    }
    if(filtriSelezionati[1] == '1') {
        if (!filtriConsegna.contains(FiltroConsegna("Consegna a domicilio"))) {
            flag = false
        }
    }
    if(filtriSelezionati[2] == '1') {
        if (!filtriConsegna.contains(FiltroConsegna("Asporto"))) {
            flag = false
        }
    }
    if(filtriSelezionati[3] == '1') {
        if(!ristoranteViewModel.isRistoranteAperto(ristorante)){
            flag = false
        }
    }
    return flag
}
