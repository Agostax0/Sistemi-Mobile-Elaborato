package com.example.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.app.viewModel.RistoranteViewModel
import com.example.app.viewModel.UtentePossiedeBadgeRistoranteViewModel
import com.example.app.viewModel.UtenteViewModel
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation

@Composable
fun RistoranteScoreboardScreen(
    ristoranteViewModel: RistoranteViewModel,
    utentePossiedeBadgeRistoranteViewModel: UtentePossiedeBadgeRistoranteViewModel,
    utenteViewModel: UtenteViewModel,
    modifier: Modifier = Modifier
) {
    val selectedRistorante = ristoranteViewModel.ristoranteSelected
    val utentiBadgeRistorante = utentePossiedeBadgeRistoranteViewModel.utentiBadgeRistorante.collectAsState(initial = listOf()).value
    val utenti = utenteViewModel.utenti.collectAsState(initial = listOf()).value
    val utentiScelti = utentiBadgeRistorante.filter {
        it.COD_BR == selectedRistorante!!.COD_BR
    }.sortedBy { it.esperienzaBadge }.asReversed()

    val colors = listOf(Color(0xFFFFD700), Color(0xffc0c0c0), Color(0xffcd7f32), Color.Cyan, Color.Cyan)
    val barList = mutableListOf<BarChartData.Bar>()
    if(utentiScelti.isNotEmpty()) {
        for (index in utentiScelti.indices) {
            if(index < 6) {
                if(utenti.isNotEmpty()) {
                    val infoUtente = utenti.find { it.ID == utentiScelti[index].ID }
                    barList.add(
                        index, BarChartData.Bar(
                            label = infoUtente!!.username,
                            value = utentiScelti[index].esperienzaBadge.toFloat(),
                            color = colors[index]
                        )
                    )
                }
            }
        }
        BarChart(
            barChartData = BarChartData(bars = barList),
            modifier = Modifier.padding(10.dp).fillMaxSize(),
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
                drawLocation = SimpleValueDrawer.DrawLocation.Outside,
                labelTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}