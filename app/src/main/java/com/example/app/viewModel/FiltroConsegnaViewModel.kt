package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.repository.FiltroConsegnaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltroConsegnaViewModel @Inject constructor(
    private val repository: FiltroConsegnaRepository
) : ViewModel() {

    val filtriConsegna = repository.filtriConsegna

    val filtriSelezionati = repository.preferenceFlow

    fun saveFiltri(filtri:String) {
        viewModelScope.launch {
            repository.saveToDataStore(filtri)
        }
    }
}