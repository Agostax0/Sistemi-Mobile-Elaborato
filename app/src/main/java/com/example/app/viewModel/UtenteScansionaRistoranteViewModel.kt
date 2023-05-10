package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.UtenteScansionaRistoranteRepository
import javax.inject.Inject

class UtenteScansionaRistoranteViewModel @Inject constructor(
    private val repository: UtenteScansionaRistoranteRepository
) : ViewModel() {

    val scansioniUtenti = repository.scansioniUtenti
}