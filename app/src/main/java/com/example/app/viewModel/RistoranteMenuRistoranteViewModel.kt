package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.RistoranteMenuRistoranteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RistoranteMenuRistoranteViewModel @Inject constructor(
    private val repository: RistoranteMenuRistoranteRepository
) : ViewModel() {

    val menuRistoranti = repository.menuRistoranti
}