package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.TipoRistoranteRepository
import javax.inject.Inject

class TipoRistoranteViewModel @Inject constructor(
    private val repository: TipoRistoranteRepository
) : ViewModel() {

    val tipiRistorante = repository.tipiRistorante
}