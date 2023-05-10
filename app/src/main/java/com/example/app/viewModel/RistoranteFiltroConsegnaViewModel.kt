package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.RistoranteFiltroConsegnaRepository
import javax.inject.Inject

class RistoranteFiltroConsegnaViewModel @Inject constructor(
    private val repository: RistoranteFiltroConsegnaRepository
) : ViewModel() {

    val filtriRistoranti = repository.filtriRistoranti
}