package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.UtentePossiedeBadgeUtenteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UtentePossiedeBadgeUtenteViewModel @Inject constructor(
    private val repository: UtentePossiedeBadgeUtenteRepository
) : ViewModel() {

    val utentiBadgeUtente = repository.utentiBadgeUtente
}