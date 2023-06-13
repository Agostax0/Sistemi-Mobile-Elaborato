package com.example.app.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app.data.entity.Cibo
import com.example.app.data.entity.Ristorante
import com.example.app.data.relation.RistoranteMenuRistorante
import com.example.app.viewModel.RistoranteMenuRistoranteViewModel
import com.example.app.viewModel.RistoranteViewModel

@Composable
fun RistoranteMenuScreen(
    ristoranteViewModel: RistoranteViewModel,
    modifier: Modifier = Modifier,
    ristoranteMenuRistoranteViewModel: RistoranteMenuRistoranteViewModel
) {
    val context = LocalContext.current
    val selectedRistorante: Ristorante? = ristoranteViewModel.ristoranteSelected
    val menuRistoranti = ristoranteMenuRistoranteViewModel.menuRistoranti.collectAsState(initial = listOf()).value
    val menuSelectedRistorante = menuRistoranti.find { it.ristorante == selectedRistorante }

    if (!menuSelectedRistorante?.menu.isNullOrEmpty()) {
        var menu = menuSelectedRistorante!!.menu.groupBy { it.gruppo }
        var lista = mutableListOf<CollapsableSection>()
        menu.keys.forEach { gruppo ->
            lista.add(CollapsableSection(gruppo, menu.get(gruppo)!!.map { cibo -> cibo.nomeCibo }))
        }
        CollapsableLazyColumn(
            sections = lista
        )
    }
}

@Composable
fun CollapsableLazyColumn(
    sections: List<CollapsableSection>,
    modifier: Modifier = Modifier
) {
    val collapsedState = remember(sections) { sections.map { true }.toMutableStateList() }
    LazyColumn(modifier) {
        sections.forEachIndexed { i, dataItem ->
            val collapsed = collapsedState[i]
            item(key = "header_$i") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            collapsedState[i] = !collapsed
                        }
                ) {
                    Icon(
                        Icons.Default.run {
                            if (collapsed)
                                KeyboardArrowDown
                            else
                                KeyboardArrowUp
                        },
                        contentDescription = "",
                        tint = Color.LightGray,
                    )
                    Text(
                        dataItem.title,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .weight(1f)
                    )
                }
                Divider()
            }
            if (!collapsed) {
                items(dataItem.rows) { row ->
                    Row {
                        Spacer(modifier = Modifier.size(MaterialIconDimension.dp))
                        Text(
                            row,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                        )
                    }
                    Divider()
                }
            }
        }
    }
}

data class CollapsableSection(val title: String, val rows: List<String>)

const val MaterialIconDimension = 24f