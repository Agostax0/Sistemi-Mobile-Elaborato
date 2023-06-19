package com.example.app.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.data.relation.UtenteBadgeRistoranteCrossRef
import com.example.app.viewModel.BadgeUtenteViewModel
import com.example.app.viewModel.RistoranteViewModel
import com.example.app.viewModel.UtentePossiedeBadgeRistoranteViewModel
import com.example.app.viewModel.UtenteViewModel
import kotlinx.coroutines.flow.filter

@Composable
fun RistoranteScoreboardScreen(
    ristoranteViewModel: RistoranteViewModel,
    utentePossiedeBadgeRistoranteViewModel: UtentePossiedeBadgeRistoranteViewModel,
    utenteViewModel: UtenteViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedRistorante = ristoranteViewModel.ristoranteSelected
    val utentiBadgeRistorante = utentePossiedeBadgeRistoranteViewModel.utentiBadgeRistorante.collectAsState(initial = listOf()).value

    val utentiScelti = utentiBadgeRistorante.filter {
        it.COD_BR == selectedRistorante!!.COD_BR
    }.sortedBy { it.esperienzaBadge }.asReversed()

    Column(
        Modifier
            .fillMaxSize()
    ) {
        PrimaRigaClassifica()
        Divider(
            thickness = 1.dp
        )
        if(!utentiScelti.isNullOrEmpty()) {
            for (index in utentiScelti.indices) {
                RigaClassifica(utente = utentiScelti[index], index = index, utenteViewModel)
                Divider(
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun PrimaRigaClassifica(){
    Row(
        Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(
                horizontal = 5.dp,
                vertical = 3.dp
            )
    ) {
        Text(
            text = "Pos",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .weight(.3f)
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            text = "Utente",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp)
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            text = "Livello ristorante",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .weight(.55f)
                .padding(start = 5.dp)
        )
    }
}

@Composable
fun RigaClassifica(utente: UtenteBadgeRistoranteCrossRef, index: Int, utenteViewModel: UtenteViewModel) {
    var pos = index + 1
    val utenteScelto = utenteViewModel.utenti.collectAsState(initial = listOf()).value
    if(!utenteScelto.isNullOrEmpty()) {
        val infoUtente = utenteScelto.find { it.ID == utente.ID }
        Log.d("UTENTE", infoUtente.toString())
        Row(
            Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .padding(
                    horizontal = 5.dp,
                    vertical = 3.dp
                )
        ) {
            Text(
                text = pos.toString(),
                fontSize = 17.sp,
                modifier = Modifier
                    .weight(.3f)
            )
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp)
            ) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = stringResource(id = R.string.profile)
                )
                Text(
                    text = infoUtente!!.username,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(start = 28.dp)
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Text(
                text = utente.esperienzaBadge.toString(),
                fontSize = 17.sp,
                modifier = Modifier
                    .weight(.55f)
                    .padding(start = 5.dp)
            )
        }
    }

}