package com.example.app.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.AppScreen
import com.example.app.viewModel.FiltroConsegnaViewModel
import com.example.app.viewModel.RistoranteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(filtroConsegnaViewModel: FiltroConsegnaViewModel,
               modifier: Modifier = Modifier
){
    val filtri = filtroConsegnaViewModel.filtriConsegna.collectAsState(initial = listOf()).value
    val filtriSelezionati by filtroConsegnaViewModel.filtriSelezionati.collectAsState(initial = "")

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = { filtroConsegnaViewModel.saveFiltri("0000") }
            ) {
                Text(
                    text = "Reset",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Column (modifier.padding(innerPadding).padding(5.dp)) {
            Text(
                text = "Filtra:",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            if (!filtriSelezionati.isEmpty() && !filtri.isEmpty()) {
                for (i in 0 until 4) {
                    CheckBoxFiltro(
                        nomeFiltro = filtri[i].filtro,
                        i,
                        filtriSelezionati,
                        filtroConsegnaViewModel
                    )
                }
            }

            Divider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 1.dp,
                modifier = Modifier.padding(10.dp)
            )

            Text(
                text = "Ordina:",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun CheckBoxFiltro(
    nomeFiltro: String,
    index: Int,
    filtriSelezionati: String,
    filtroConsegnaViewModel: FiltroConsegnaViewModel
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = nomeFiltro,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .weight(1f)
        )
        Checkbox(
            checked = filtriSelezionati[index] == '1',
            onCheckedChange = {
                val newChar = if(filtriSelezionati[index] == '0') "1" else "0"
                val newFiltri = filtriSelezionati.substring(0,index) + newChar + filtriSelezionati.substring(index+1)
                filtroConsegnaViewModel.saveFiltri(newFiltri)
            },
            colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .weight(0.1f)
                .padding(end = 20.dp)
        )
    }
}