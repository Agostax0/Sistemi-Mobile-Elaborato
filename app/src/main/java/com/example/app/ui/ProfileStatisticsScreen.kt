package com.example.app.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app.data.relation.UtenteRistoranteCrossRef
import com.example.app.viewModel.RistoranteTipoRistoranteViewModel
import com.example.app.viewModel.RistoranteViewModel
import com.example.app.viewModel.UtentePossiedeBadgeRistoranteViewModel
import com.example.app.viewModel.UtenteScansionaRistoranteViewModel
import com.example.app.viewModel.UtenteViewModel
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation

@Composable
fun ProfileStatisticsScreen(
    utenteViewModel: UtenteViewModel,
    session: String,
    utenteScansionaRistoranteViewModel : UtenteScansionaRistoranteViewModel,
    utentePossiedeBadgeRistoranteViewModel: UtentePossiedeBadgeRistoranteViewModel,
    ristoranteViewModel: RistoranteViewModel,
    ristoranteTipoRistoranteViewModel: RistoranteTipoRistoranteViewModel,
    navigateToRestaurant: ()->Unit
){
    val context = LocalContext.current
    val utenti by utenteViewModel.utenti.collectAsState(initial = listOf())

    if(utenti.isNotEmpty() || utenteViewModel.utenteLoggato != null) {
        val utenteLoggato = if (utenteViewModel.utenteLoggato == null)
            utenti.find { it.username == session }!! else utenteViewModel.utenteLoggato!!

        /**
         * Lista COD_RIS Ristoranti preferiti
         */
        val ristorantiPreferitiCOD_RIS = utenteScansionaRistoranteViewModel.getRistorantiPreferitiPerUtente(utenteLoggato.ID.toString()).collectAsState(
            initial = listOf()
        ).value

        val ristorantiPreferiti = ristoranteViewModel.ristoranti.collectAsState(initial = listOf()).value.filter { ristorantiPreferitiCOD_RIS.contains(it.COD_RIS) }

        val ristorantiPreferitiTipi = ristoranteTipoRistoranteViewModel.tipiRistoranti.collectAsState(initial = listOf()).value.filter { ristorantiPreferitiCOD_RIS.contains(it.ristorante.COD_RIS) }

        val tipologieRistorante = arrayListOf<String>()

        val istanzeTipologieRistorante = arrayListOf<Int>()

        for(ristorantePreferitoTipi in ristorantiPreferitiTipi){

            ristorantePreferitoTipi.tipi.forEach{
                if(tipologieRistorante.contains(it.nomeTipo)){
                    istanzeTipologieRistorante[tipologieRistorante.indexOf(it.nomeTipo)] = istanzeTipologieRistorante[tipologieRistorante.indexOf(it.nomeTipo)].inc()
                }
                else{
                    tipologieRistorante.add(it.nomeTipo)
                    istanzeTipologieRistorante.add(1)
                }
            }

        }

        val infoTipoRistOrdinato = tipologieRistorante.map { Pair(it, istanzeTipologieRistorante.get(tipologieRistorante.indexOf(it))) }

        Log.d("STATS_TAG","$tipologieRistorante")
        Log.d("STATS_TAG","$istanzeTipologieRistorante")

        val barList = mutableListOf<BarChartData.Bar>()
        val colors = listOf(Color(0xFFBA68C8),Color(0xFF9575CD),Color(0xFFFFB74D),Color(0xFFFFF176), Color(0xFFAED581), Color(0xFFE57373),Color(0xFF4DD0E1),Color(0xFF9575CD))

        for(i in infoTipoRistOrdinato.indices) {
            barList.add(
                BarChartData.Bar(
                    label = infoTipoRistOrdinato[i].first,
                    value = infoTipoRistOrdinato[i].second.toFloat(),
                    color = colors[i]
                )
            )
        }

        Column(Modifier.fillMaxSize()) {

            if (barList.isNotEmpty()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 10.dp, bottom = 20.dp)) {
                    BarChart(
                        barChartData = BarChartData(bars = barList),
                        modifier = Modifier
                            .padding(10.dp),
                        animation = simpleChartAnimation(),
                        barDrawer = SimpleBarDrawer(),
                        xAxisDrawer = SimpleXAxisDrawer(
                            axisLineColor = MaterialTheme.colorScheme.onSurface
                        ),
                        yAxisDrawer = SimpleYAxisDrawer(
                            axisLineColor = MaterialTheme.colorScheme.onSurface,
                            labelTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        labelDrawer = SimpleValueDrawer(
                            drawLocation = SimpleValueDrawer.DrawLocation.XAxis,
                            labelTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                }
            }
            
            Header(mainText="Ristoranti Preferiti: ${ristorantiPreferiti.size}")

            LazyColumn(Modifier.fillMaxWidth().padding(vertical=10.dp)){
                items(ristorantiPreferiti){ristorante->
                    val isPreferito =
                        utenteScansionaRistoranteViewModel.getRistorantiPreferitiPerUtente(
                            utenteLoggato.ID.toString()
                        ).collectAsState(
                            initial = listOf()
                        ).value.filter { it == ristorante!!.COD_RIS }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ristorante.icona,
                            contentDescription = ristorante.nome,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(HexagonShape())
                                .clickable {
                                    ristoranteViewModel.selectRistorante(ristorante)
                                    navigateToRestaurant()
                                }
                        )

                        ClickableText(
                            text = AnnotatedString(text = ristorante.nome),
                            onClick = {
                            ristoranteViewModel.selectRistorante(ristorante)
                            navigateToRestaurant() },
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 21.sp, color = MaterialTheme.colorScheme.onSurface)
                        )

                        IconButton(
                            onClick = {
                                val newPreferito = isPreferito.isEmpty()
                                utenteScansionaRistoranteViewModel.updatePreferito(
                                    UtenteRistoranteCrossRef(
                                        ID = utenteLoggato.ID,
                                        COD_RIS = ristorante.COD_RIS,
                                        preferito = newPreferito
                                    )
                                )
                            },
                            modifier = Modifier
                                .size(50.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Preferiti",
                                tint = MaterialTheme.colorScheme.primary ,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}