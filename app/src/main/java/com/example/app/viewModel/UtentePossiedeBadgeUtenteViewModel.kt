package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.relation.UtenteBadgeRistoranteCrossRef
import com.example.app.data.relation.UtenteBadgeUtenteCrossRef
import com.example.app.data.repository.UtentePossiedeBadgeUtenteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UtentePossiedeBadgeUtenteViewModel @Inject constructor(
    private val repository: UtentePossiedeBadgeUtenteRepository
) : ViewModel() {

    val utentiBadgeUtente = repository.utentiBadgeUtente

    val utentiBadgeUtenteRef = repository.utenteBadgeUtenteRef

    fun newBadgeUtente(newBadge: UtenteBadgeUtenteCrossRef) {
        viewModelScope.launch {
            repository.newBadgeUtenteForUtente(newBadge)
        }
    }
}