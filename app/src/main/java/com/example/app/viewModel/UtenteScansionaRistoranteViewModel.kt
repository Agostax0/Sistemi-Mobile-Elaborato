package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.relation.UtenteRistoranteCrossRef
import com.example.app.data.repository.UtenteScansionaRistoranteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UtenteScansionaRistoranteViewModel @Inject constructor(
    private val repository: UtenteScansionaRistoranteRepository
) : ViewModel() {

    val scansioniUtenti = repository.scansioniUtenti

    fun getRistorantiPreferitiPerUtente(ID: String): Flow<List<Int>> {
        return repository.getRistorantiPreferitiPerUtente(ID)
    }

    fun updatePreferito(utenteRistoranteCrossRef: UtenteRistoranteCrossRef) {
        viewModelScope.launch {
            repository.updatePreferito(utenteRistoranteCrossRef)
        }
    }
}