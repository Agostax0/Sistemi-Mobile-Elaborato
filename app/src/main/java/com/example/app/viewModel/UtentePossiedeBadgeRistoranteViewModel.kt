package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.UtentePossiedBadgeRistoranteRepository
import javax.inject.Inject

class UtentePossiedeBadgeRistoranteViewModel @Inject constructor(
    private val repository: UtentePossiedBadgeRistoranteRepository
) : ViewModel() {

    val utentiBadgeRistorante = repository.utentiBadgeRistorante
}