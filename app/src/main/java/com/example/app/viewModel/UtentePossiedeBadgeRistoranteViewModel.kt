package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.relation.UtenteBadgeRistoranteCrossRef
import com.example.app.data.repository.UtentePossiedBadgeRistoranteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UtentePossiedeBadgeRistoranteViewModel @Inject constructor(
    private val repository: UtentePossiedBadgeRistoranteRepository
) : ViewModel() {

    val utentiBadgeRistorante = repository.utentiBadgeRistorante

    fun newScansione(newBadge: UtenteBadgeRistoranteCrossRef) {
        viewModelScope.launch {
            repository.newScansione(newBadge)
        }
    }
}