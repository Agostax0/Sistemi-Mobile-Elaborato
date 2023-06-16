package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.repository.TipoRistoranteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TipoRistoranteViewModel @Inject constructor(
    private val repository: TipoRistoranteRepository
) : ViewModel() {

    val tipiRistorante = repository.tipiRistorante

    val tipiSelezionati = repository.preferenceFlow

    fun saveTipi(tipi:String) {
        viewModelScope.launch {
            repository.saveToDataStore(tipi)
        }
    }
}