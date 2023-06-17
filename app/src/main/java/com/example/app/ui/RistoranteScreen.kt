package com.example.app.ui


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.app.R
import com.example.app.data.entity.FiltroConsegna
import com.example.app.data.entity.Ristorante
import com.example.app.data.entity.TipoRistorante
import com.example.app.ui.theme.Green
import com.example.app.viewModel.RistoranteFiltroConsegnaViewModel
import com.example.app.viewModel.RistoranteTipoRistoranteViewModel
import com.example.app.viewModel.RistoranteViewModel
import com.example.app.viewModel.UtenteScansionaRistoranteViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import java.util.Calendar

@Composable
fun RistoranteMainScreen(ristoranteViewModel: RistoranteViewModel,
                         modifier: Modifier = Modifier,
                         ristoranteTipoRistoranteViewModel: RistoranteTipoRistoranteViewModel,
                         ristoranteFiltroConsegnaViewModel: RistoranteFiltroConsegnaViewModel,
                         utenteScansionaRistoranteViewModel: UtenteScansionaRistoranteViewModel
) {
    val context = LocalContext.current

    val selectedRistorante = ristoranteViewModel.ristoranteSelected
    val tipiRistoranti = ristoranteTipoRistoranteViewModel.tipiRistoranti.collectAsState(initial = listOf()).value
    val tipoRistorante = tipiRistoranti.find { it.ristorante == selectedRistorante }
    val filtriRistoranti = ristoranteFiltroConsegnaViewModel.filtriRistoranti.collectAsState(initial = listOf()).value
    val filtriSelectedRistorante = filtriRistoranti.find { it.ristorante == selectedRistorante }

    val openDialog = remember { mutableStateOf(false)  }
    val scroll = rememberScrollState(0)

    if (!tipoRistorante?.tipi.isNullOrEmpty() && !filtriSelectedRistorante?.filtri.isNullOrEmpty()) {
        var tipo: TipoRistorante = tipoRistorante!!.tipi.get(0)
        var filtriConsegna = filtriSelectedRistorante!!.filtri

        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .verticalScroll(scroll)
        ) {
            Header(ristorante = selectedRistorante!!, tipoRistorante = tipo, filtriConsegna, utenteScansionaRistoranteViewModel)
            AsyncImage(model = selectedRistorante.icona,
                contentDescription = "immagine ristorante",
                modifier = Modifier
                    .size(size = 300.dp)
                    .align(CenterHorizontally),
            )

            Text(
                text = selectedRistorante.nome,
                modifier = Modifier
                    .padding(bottom = 20.dp)
            )

            ZonaUtente(ristorante = selectedRistorante, utenteScansionaRistoranteViewModel, context)
            ZonaInfo(ristorante = selectedRistorante,
                ristoranteViewModel,
                { openDialog.value = !openDialog.value },
                context
            )
        }
    }

    if (openDialog.value) {
        val listaGiorni = listOf("Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica")
        var counter = 0
        val listaOrari = ristoranteViewModel.getListaOrari(selectedRistorante!!)
        val date: Calendar = Calendar.getInstance()
        val day: Int = date.get(Calendar.DAY_OF_WEEK) - 2

        AlertDialog(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            textContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Orari")
            },
            text = {
                Column {
                    listaGiorni.forEach{ giorno ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                        ) {
                            Text(
                                text = giorno,
                                modifier = modifier.weight(1f)
                            )
                            Text(
                                text = if(listaOrari[counter] == "0-0") "Chiuso" else listaOrari[counter],
                                modifier = modifier.weight(.2f)
                            )
                        }
                        Divider(
                            color = if(day == counter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                        counter++
                    }
                }
            },
            confirmButton = { }
        )
    }
}

@Composable
fun Header(ristorante: Ristorante,
           tipoRistorante: TipoRistorante,
           filtriConsegna: List<FiltroConsegna>,
           utenteScansionaRistoranteViewModel: UtenteScansionaRistoranteViewModel
){
    var filtriText = ""
    filtriConsegna.forEach{
        filtriText += it.filtro + " / "
    }
    filtriText = filtriText.removeSuffix(" / ")
    val scansioniUtenti = utenteScansionaRistoranteViewModel.scansioniUtenti.collectAsState(initial = listOf()).value
    //val scansioniUtente = scansioniUtenti.find { it.utente.ID == 1 }

    Row(
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth(),
    ) {
        AsyncImage(model = tipoRistorante.icona,
            contentDescription = "immagine tipo ristorante",
            modifier = Modifier
                .size(size = 40.dp)
                .padding(end = 5.dp))

        Box(modifier = Modifier.weight(1f)){
            Text(
                text = tipoRistorante.nomeTipo,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Start,
                modifier = Modifier
            )
            Text(
                text = filtriText,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 23.dp),
                lineHeight = 15.sp
            )
        }
        IconButton(
            onClick = { /*TODO aggiunge preferito*/ },
            modifier = Modifier
                .weight(.3f)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                    shape = CircleShape
                )
        ) {
            Icon(imageVector = Icons.Filled.Star,
                contentDescription = "Preferiti",
                modifier = Modifier.padding(end = 25.dp),
                tint = MaterialTheme.colorScheme.primary //if(isPreferito) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
            )
            Text(text = ristorante.numeroPreferiti.toString(),
                modifier = Modifier.padding(start = 25.dp)
            )
        }
    }
}

@Composable
fun ZonaUtente(
    ristorante: Ristorante,
    utenteScansionaRistoranteViewModel: UtenteScansionaRistoranteViewModel,
    context: Context
) {
    val options = ScanOptions()
    options.setOrientationLocked(false)

    val barcodeLauncher = rememberLauncherForActivityResult(contract = ScanContract()) { result ->
        if(result.getContents() == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            barcodeLauncher.launch(options)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Row(modifier = Modifier
        .fillMaxWidth()
    ) {
        Text(text = "Livello 3",
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    barcodeLauncher.launch(options)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
                      },
            modifier = Modifier
                .weight(.3f)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Filled.QrCode,
                contentDescription = "Apri camera per codice QR",
                tint = MaterialTheme.colorScheme.primary //if(isAbbastanzaVicino) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
            )
        }
    }
    LinearProgressIndicator(progress = 0.7f) //da regolare con l'esperienza
}

@Composable
fun ZonaInfo(
    ristorante: Ristorante,
    ristoranteViewModel: RistoranteViewModel,
    onOpenDialog: () -> Unit,
    context: Context
){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp)
    ) {
        Text(
            text = ristorante.numeroTelefono,
            modifier = Modifier
                .weight(.2f)
                .align(CenterVertically),
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
        Button(
            onClick = { 
                      makeCall(context, ristorante.numeroTelefono)
            },
            modifier = Modifier
                .weight(.2f)
                .padding(end = 15.dp),
            shape = CircleShape
        ) {
            Text(
               text = "Chiama"
            )
        }
        val isRistoranteAperto = ristoranteViewModel.isRistoranteAperto(ristorante)
        Text(
            text = if(isRistoranteAperto) "Aperto" else "Chiuso",
            fontSize = 15.sp,
            color = if(isRistoranteAperto) Green else MaterialTheme.colorScheme.error,
            modifier = Modifier
                .align(CenterVertically)
                .weight(.11f)
                .clickable(
                    enabled = true,
                    onClick = onOpenDialog
                )
        )
    }
    Text(
        text = ristorante.descrizione,
        modifier = Modifier.padding(top = 10.dp)
    )
}

private fun makeCall(context: Context, phoneNumber: String){
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
    context.startActivity(intent)
}