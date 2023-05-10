package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.UtentePossiedBadgeRistoranteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UtentePossiedeBadgeRistoranteViewModel @Inject constructor(
    private val repository: UtentePossiedBadgeRistoranteRepository
) : ViewModel() {

    val utentiBadgeRistorante = repository.utentiBadgeRistorante
}