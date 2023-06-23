package com.example.app.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.app.data.entity.Ristorante
import com.example.app.data.repository.RistoranteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class RistoranteViewModel @Inject constructor(
    private val repository: RistoranteRepository
) : ViewModel() {

    val ristoranti = repository.ristoranti

    private var _ristoranteSelected: Ristorante? = null
    val ristoranteSelected
        get() = _ristoranteSelected

    fun selectRistorante(ristorante: Ristorante?) {
        _ristoranteSelected = ristorante
    }

    fun getListaOrari(ristorante: Ristorante): List<String> {
        return ristorante.orari.split(";")
    }

    fun isRistoranteAperto(ristorante: Ristorante): Boolean {
        val listaOrari = getListaOrari(ristorante = ristorante)
        val date: Calendar = Calendar.getInstance()
        val day: Int = date.get(Calendar.DAY_OF_WEEK) - 1
        val currentHour: Int = date.get(Calendar.HOUR_OF_DAY)
        val orariOggi = listaOrari[day].split("-")
        return currentHour <= orariOggi[1].toInt() && currentHour >= orariOggi[0].toInt()
    }
}