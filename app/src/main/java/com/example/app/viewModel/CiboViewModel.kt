package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.CiboRepository
import javax.inject.Inject

class CiboViewModel @Inject constructor(
    private val repository: CiboRepository
) : ViewModel() {

    val cibi = repository.cibi
}