package com.example.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import com.example.app.viewModel.RistoranteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(onConfirmClicked: ()-> Unit,
               ristoranteViewModel: RistoranteViewModel,
               modifier: Modifier = Modifier
){
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick =  onConfirmClicked
            ) {
                Text(text = "Conferma")
            }
        },floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Column (modifier.padding(innerPadding)) {

        }
    }
}