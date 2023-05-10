package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.RistoranteTipoRistoranteRepository
import javax.inject.Inject

class RistoranteTipoRistoranteViewModel @Inject constructor(
    private val repository: RistoranteTipoRistoranteRepository
) : ViewModel() {

    val tipiRistoranti = repository.tipiRistoranti
}