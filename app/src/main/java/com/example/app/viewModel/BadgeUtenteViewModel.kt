package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.BadgeUtenteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BadgeUtenteViewModel @Inject constructor(
    private val repository: BadgeUtenteRepository
) : ViewModel() {

    val badgeUtenti = repository.badgeUtenti
}