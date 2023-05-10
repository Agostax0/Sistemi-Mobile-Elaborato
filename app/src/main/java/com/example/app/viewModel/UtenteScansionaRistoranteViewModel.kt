package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.UtenteScansionaRistoranteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UtenteScansionaRistoranteViewModel @Inject constructor(
    private val repository: UtenteScansionaRistoranteRepository
) : ViewModel() {

    val scansioniUtenti = repository.scansioniUtenti
}