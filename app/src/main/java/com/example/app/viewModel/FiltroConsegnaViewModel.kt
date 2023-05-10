package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.FiltroConsegnaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FiltroConsegnaViewModel @Inject constructor(
    private val repository: FiltroConsegnaRepository
) : ViewModel() {

    val filtriConsegna = repository.filtriConsegna
}