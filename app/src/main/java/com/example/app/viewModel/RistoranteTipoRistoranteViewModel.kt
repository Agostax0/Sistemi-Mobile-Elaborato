package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.RistoranteTipoRistoranteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RistoranteTipoRistoranteViewModel @Inject constructor(
    private val repository: RistoranteTipoRistoranteRepository
) : ViewModel() {

    val tipiRistoranti = repository.tipiRistoranti
}