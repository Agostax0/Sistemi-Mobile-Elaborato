package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.RistoranteRepository
import javax.inject.Inject

class RistoranteViewModel @Inject constructor(
    private val repository: RistoranteRepository
) : ViewModel() {

    val ristoranti = repository.ristoranti
}