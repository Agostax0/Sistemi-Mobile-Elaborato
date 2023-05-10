package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.RistoranteMenuRistoranteRepository
import javax.inject.Inject

class RistoranteMenuRistoranteViewModel @Inject constructor(
    private val repository: RistoranteMenuRistoranteRepository
) : ViewModel() {

    val menuRistoranti = repository.menuRistoranti
}