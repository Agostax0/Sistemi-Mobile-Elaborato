package com.example.app.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.app.viewModel.UtenteViewModel

@Composable
fun ProfileScreen(
    utenteViewModel: UtenteViewModel
){
    Row() {
        Text(text = utenteViewModel.session.collectAsState(initial = "").value)
    }
}