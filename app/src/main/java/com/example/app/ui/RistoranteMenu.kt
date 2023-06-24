package com.example.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.data.entity.Ristorante
import com.example.app.viewModel.RistoranteMenuRistoranteViewModel
import com.example.app.viewModel.RistoranteViewModel

@Composable
fun RistoranteMenuScreen(
    ristoranteViewModel: RistoranteViewModel,
    modifier: Modifier = Modifier,
    ristoranteMenuRistoranteViewModel: RistoranteMenuRistoranteViewModel
) {
    val selectedRistorante: Ristorante? = ristoranteViewModel.ristoranteSelected
    val menuRistoranti = ristoranteMenuRistoranteViewModel.menuRistoranti.collectAsState(initial = listOf()).value
    val menuSelectedRistorante = menuRistoranti.find { it.ristorante == selectedRistorante }

    if (!menuSelectedRistorante?.menu.isNullOrEmpty()) {
        val menu = menuSelectedRistorante!!.menu.groupBy { it.gruppo }
        val lista = mutableListOf<CollapsableSection>()
        menu.keys.forEach { gruppo ->
            lista.add(CollapsableSection(gruppo, menu[gruppo]!!.map { cibo -> cibo.nomeCibo }))
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
                        .padding(5.dp)
                ) {
                    Text(
                        dataItem.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = if(collapsed) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 7.dp)
                            .weight(1f)
                    )
                    Icon(
                        Icons.Default.run {
                            if (collapsed)
                                ArrowDropDown
                            else
                                ArrowDropUp
                        },
                        contentDescription = "",
                        tint = if(collapsed) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .weight(.2f)
                            .size(35.dp)
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
                            fontSize = 16.sp,
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