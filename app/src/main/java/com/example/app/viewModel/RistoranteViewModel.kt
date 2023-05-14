package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.entity.Ristorante
import com.example.app.data.repository.RistoranteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RistoranteViewModel @Inject constructor(
    private val repository: RistoranteRepository
) : ViewModel() {

    val ristoranti = repository.ristoranti

    private var _ristoranteSelected: Ristorante? = null
    val ristoranteSelected
        get() = _ristoranteSelected

    fun selectRistorante(ristorante: Ristorante) {
        _ristoranteSelected = ristorante
    }
}