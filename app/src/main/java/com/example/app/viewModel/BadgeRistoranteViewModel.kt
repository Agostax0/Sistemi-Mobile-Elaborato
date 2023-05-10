package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.BadgeRistoranteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BadgeRistoranteViewModel @Inject constructor(
    private val repository: BadgeRistoranteRepository
) : ViewModel() {

    val badgeRistoranti = repository.badgeRistoranti
}