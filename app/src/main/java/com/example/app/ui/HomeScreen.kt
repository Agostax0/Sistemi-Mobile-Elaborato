package com.example.app.ui

import android.app.Activity
import android.util.Log
import android.view.Gravity
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app.R
import com.example.app.data.LocationDetails
import com.example.app.data.entity.FiltroConsegna
import com.example.app.data.entity.Ristorante
import com.example.app.data.entity.TipoRistorante
import com.example.app.data.relation.RistoranteFiltroConsegna
import com.example.app.data.relation.RistoranteTipoRistorante
import com.example.app.showToast
import com.example.app.ui.theme.Green
import com.example.app.viewModel.FiltroConsegnaViewModel
import com.example.app.viewModel.LocationViewModel
import com.example.app.viewModel.RistoranteFiltroConsegnaViewModel
import com.example.app.viewModel.RistoranteTipoRistoranteViewModel
import com.example.app.viewModel.RistoranteViewModel
import com.example.app.viewModel.TipoRistoranteViewModel
import com.example.app.viewModel.UtenteScansionaRistoranteViewModel
import com.example.app.viewModel.UtenteViewModel
import com.yagmurerdogan.toasticlib.Toastic
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onMapButtonClicked: ()-> Unit,
               onRestaurantClicked: ()->Unit,
               onFiltersClicked: ()->Unit,
               ristoranteViewModel: RistoranteViewModel,
               filtroConsegnaViewModel: FiltroConsegnaViewModel,
               ristoranteFiltroConsegnaViewModel: RistoranteFiltroConsegnaViewModel,
               tipoRistoranteViewModel: TipoRistoranteViewModel,
               ristoranteTipoRistoranteViewModel: RistoranteTipoRistoranteViewModel,
               utenteScansionaRistoranteViewModel: UtenteScansionaRistoranteViewModel,
               utenteViewModel: UtenteViewModel,
               locationViewModel: LocationViewModel,
               session: String,
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
            RistorantiList(onRestaurantClicked,
                onFiltersClicked,
                ristoranteViewModel,
                ristoranteFiltroConsegnaViewModel,
                filtroConsegnaViewModel,
                tipoRistoranteViewModel,
                ristoranteTipoRistoranteViewModel,
                utenteScansionaRistoranteViewModel,
                utenteViewModel,
                locationViewModel,
                session
            )
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
    filtroConsegnaViewModel: FiltroConsegnaViewModel,
    tipoRistoranteViewModel: TipoRistoranteViewModel,
    ristoranteTipoRistoranteViewModel: RistoranteTipoRistoranteViewModel,
    utenteScansionaRistoranteViewModel: UtenteScansionaRistoranteViewModel,
    utenteViewModel: UtenteViewModel,
    locationViewModel: LocationViewModel,
    session: String
) {
    val utenti by utenteViewModel.utenti.collectAsState(initial = listOf())
    val context = LocalContext.current

    val filtriRistorante = filtroConsegnaViewModel.filtriConsegna.collectAsState(initial = listOf()).value
    val filtriRistoranti = ristoranteFiltroConsegnaViewModel.filtriRistoranti.collectAsState(initial = listOf()).value
    val filtriSelezionati by filtroConsegnaViewModel.filtriSelezionati.collectAsState(initial = "")

    val tipiRistorante = tipoRistoranteViewModel.tipiRistorante.collectAsState(initial = listOf()).value
    val tipiRistoranti = ristoranteTipoRistoranteViewModel.tipiRistoranti.collectAsState(initial = listOf()).value
    val tipiSelezionati by tipoRistoranteViewModel.tipiSelezionati.collectAsState(initial = "")

    val ordineSelezionato by filtroConsegnaViewModel.ordineSelezionato.collectAsState(initial = "")

    if(utenti.isNotEmpty() || utenteViewModel.utenteLoggato != null) {
        val utenteLoggato = if(utenteViewModel.utenteLoggato == null)
            utenti.find { it.username == session }!!
        else utenteViewModel.utenteLoggato!!
        val ristorantiPreferiti = utenteScansionaRistoranteViewModel.getRistorantiPreferitiPerUtente(utenteLoggato.ID.toString()).collectAsState(
            initial = listOf()
        ).value

        var ricerca by rememberSaveable { mutableStateOf("") }
        val posUtente = locationViewModel.location
        val distanza by filtroConsegnaViewModel.distanza.collectAsState(initial = "4000f")

        val ristorantiTipati = tipoRistorante(tipiRistoranti, tipiSelezionati, tipiRistorante, posUtente, distanza.toFloat())
        val ristorantiFiltrati = filtroRistoranti(ristorantiTipati, filtriRistoranti, filtriSelezionati, ristoranteViewModel, filtriRistorante)
        val ristorantiOrdinati = sortRistoranti(ristorantiFiltrati, ordineSelezionato, ristorantiPreferiti, posUtente)
        val ristorantiCercati = ristorantiOrdinati.filter {ristorante -> ristorante.nome.lowercase().contains(ricerca.lowercase()) }

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
                            for(index in tipiRistorante.indices) {
                                AsyncImage(
                                    model = tipiRistorante[index].icona,
                                    contentDescription = tipiRistorante[index].nomeTipo,
                                    alpha = if(tipiSelezionati[index] == '1') 1f else 0.3f,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clickable(
                                            onClick = {
                                                val newChar =
                                                    if (tipiSelezionati[index] == '0') "1" else "0"
                                                val newTipi = tipiSelezionati.substring(
                                                    0,
                                                    index
                                                ) + newChar + tipiSelezionati.substring(index + 1)
                                                tipoRistoranteViewModel.saveTipi(newTipi)
                                            },
                                            enabled = true
                                        )
                                        .weight(1f)
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
                            containerColor =  if(ristorantiPreferiti.contains(ristorante.COD_RIS)) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(all = 2.dp)
                                .fillMaxSize()
                                .align(CenterHorizontally),
                            verticalAlignment = CenterVertically
                        ) {
                            AsyncImage(model = ristorante.icona,
                                contentDescription = "immagine ristorante",
                                modifier = Modifier
                                    .size(size = 35.dp)
                                    .padding(horizontal = 5.dp)
                                    .clip(shape = CircleShape))

                            val scroll = rememberScrollState(0)
                            Text(
                                text = ristorante.nome,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
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
}

fun sortRistoranti(
    ristorantiFiltrati: List<Ristorante>,
    ordineSelezionato: String,
    ristorantiPreferiti: List<Int>,
    posUtente: MutableState<LocationDetails>
): List<Ristorante> {
    var ristorantiOrdinati = listOf<Ristorante>()
    val posizioneUtente = posUtente.value

    when(ordineSelezionato) {
        "Più vicini" -> ristorantiOrdinati = ristorantiFiltrati.sortedBy {
            getDistanceFromLatLonInKm(it.posizione.split(";")[0].toDouble(), it.posizione.split(";")[1].toDouble(), posizioneUtente.latitude, posizioneUtente.longitude)
        }
        "Più amati" -> ristorantiOrdinati = ristorantiFiltrati.sortedBy { it.numeroPreferiti }.asReversed()
        "I tuoi preferiti" -> ristorantiOrdinati = ristorantiFiltrati.sortedBy { ristorantiPreferiti.contains(it.COD_RIS) }.asReversed()
    }
    return ristorantiOrdinati
}

fun tipoRistorante(
    tipiRistoranti: List<RistoranteTipoRistorante>,
    filtriSelezionati: String,
    tipiRistorante: List<TipoRistorante>,
    posUtente: MutableState<LocationDetails>,
    distanzaFiltro: Float
): List<Ristorante> {
    val ristorantiFiltrati = mutableListOf<Ristorante>()
    val posizioneUtente = posUtente.value

    tipiRistoranti.forEach{ pair ->
        val posizioneRistorante = pair.ristorante.posizione.split(";")
        val latR = posizioneRistorante[0].toDouble()
        val longR = posizioneRistorante[1].toDouble()

        val distanzaRistoranteUtente = getDistanceFromLatLonInKm(latR, longR, posizioneUtente.latitude, posizioneUtente.longitude)
        if(distanzaRistoranteUtente <= (distanzaFiltro/1000)) {
            if(checkTipi(filtriSelezionati, pair.tipi[0], tipiRistorante)) {
                ristorantiFiltrati.add(pair.ristorante)
            }
        }
    }
    return ristorantiFiltrati
}

fun checkTipi(
    filtriSelezionati: String,
    tipoRistorante: TipoRistorante,
    tipiRistorante: List<TipoRistorante>
): Boolean {
    var flag = true
    for(index in tipiRistorante.indices) {
        if(filtriSelezionati[index] == '0') {
            Log.d("TIPI", "RISTORANTE= " + tipoRistorante.nomeTipo + " / FILTRI= " + filtriSelezionati + " / FILTRO= " + tipiRistorante[index].nomeTipo)
            if (tipoRistorante.nomeTipo == tipiRistorante[index].nomeTipo) {
                flag = false
            }
        }
    }
    return flag
}

fun filtroRistoranti(
    ristorantiTipati: List<Ristorante>,
    filtriPerRistorante: List<RistoranteFiltroConsegna>,
    filtriSelezionati: String,
    ristoranteViewModel: RistoranteViewModel,
    filtriRistorante: List<FiltroConsegna>
): List<Ristorante> {
    val ristorantiFiltrati = mutableListOf<Ristorante>()
    ristorantiTipati.forEach { ristorante ->
        val ristoranteTrovato = filtriPerRistorante.find { ristorante == it.ristorante }
        if(ristoranteTrovato != null) {
            if(checkFiltri(filtriSelezionati, ristoranteTrovato.ristorante, ristoranteTrovato.filtri, ristoranteViewModel, filtriRistorante)) {
                ristorantiFiltrati.add(ristoranteTrovato.ristorante)
            }
        }
    }
    return ristorantiFiltrati
}

fun checkFiltri(
    filtriSelezionati: String,
    ristorante: Ristorante,
    filtriConsegna: List<FiltroConsegna>,
    ristoranteViewModel: RistoranteViewModel,
    filtriRistorante: List<FiltroConsegna>
): Boolean {
    var flag = true
    for(index in 0 until 2) {
        if(filtriSelezionati[0] == '1') {
            if (!filtriConsegna.contains(filtriRistorante[index])) {
                flag = false
            }
        }
    }
    if(filtriSelezionati[3] == '1') {
        if(!ristoranteViewModel.isRistoranteAperto(ristorante)){
            flag = false
        }
    }
    return flag
}

fun getDistanceFromLatLonInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val raggio = 6371 // Radius of the earth in km
    val dLat = deg2rad(lat2 - lat1)  // deg2rad below
    val dLon = deg2rad(lon2 - lon1)
    val a =
        sin(dLat / 2) * sin(dLat / 2) +
                cos(deg2rad(lat1)) * cos(deg2rad(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return raggio * c
}

fun deg2rad(deg: Double): Double {
    return deg * (PI/180)
}
