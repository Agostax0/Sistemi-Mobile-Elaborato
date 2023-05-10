package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.CiboRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CiboViewModel @Inject constructor(
    private val repository: CiboRepository
) : ViewModel() {

    val cibi = repository.cibi
}