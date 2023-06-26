package com.example.app.ui


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.app.data.entity.FiltroConsegna
import com.example.app.data.entity.Ristorante
import com.example.app.data.entity.TipoRistorante
import com.example.app.data.entity.Utente
import com.example.app.data.relation.UtenteBadgeRistoranteCrossRef
import com.example.app.data.relation.UtenteBadgeUtenteCrossRef
import com.example.app.data.relation.UtenteRistoranteCrossRef
import com.example.app.ui.theme.Green
import com.example.app.viewModel.RistoranteFiltroConsegnaViewModel
import com.example.app.viewModel.RistoranteTipoRistoranteViewModel
import com.example.app.viewModel.RistoranteViewModel
import com.example.app.viewModel.UtentePossiedeBadgeRistoranteViewModel
import com.example.app.viewModel.UtentePossiedeBadgeUtenteViewModel
import com.example.app.viewModel.UtenteScansionaRistoranteViewModel
import com.example.app.viewModel.UtenteViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import java.text.SimpleDateFormat
import java.util.Calendar

@Composable
fun RistoranteMainScreen(ristoranteViewModel: RistoranteViewModel,
                         modifier: Modifier = Modifier,
                         ristoranteTipoRistoranteViewModel: RistoranteTipoRistoranteViewModel,
                         ristoranteFiltroConsegnaViewModel: RistoranteFiltroConsegnaViewModel,
                         utenteScansionaRistoranteViewModel: UtenteScansionaRistoranteViewModel,
                         utentePossiedeBadgeRistoranteViewModel: UtentePossiedeBadgeRistoranteViewModel,
                         utentePossiedeBadgeUtenteViewModel: UtentePossiedeBadgeUtenteViewModel,
                         utenteViewModel: UtenteViewModel,
                         session: String
) {
    val context = LocalContext.current

    val selectedRistorante = ristoranteViewModel.ristoranteSelected
    val tipiRistoranti = ristoranteTipoRistoranteViewModel.tipiRistoranti.collectAsState(initial = listOf()).value
    val tipoRistorante = tipiRistoranti.find { it.ristorante == selectedRistorante }
    val filtriRistoranti = ristoranteFiltroConsegnaViewModel.filtriRistoranti.collectAsState(initial = listOf()).value
    val filtriSelectedRistorante = filtriRistoranti.find { it.ristorante == selectedRistorante }
    val utentiBadgeRistorante = utentePossiedeBadgeRistoranteViewModel.utentiBadgeRistorante.collectAsState(initial = listOf()).value

    val utenti by utenteViewModel.utenti.collectAsState(initial = listOf())
    if(utenti.isNotEmpty() || utenteViewModel.utenteLoggato != null) {
        val utenteLoggato = if (utenteViewModel.utenteLoggato == null)
            utenti.find { it.username == session }!!
        else utenteViewModel.utenteLoggato!!
        val badgeUtenteLoggatoRistoranteSelected = utentiBadgeRistorante.filter {
            it.COD_BR == selectedRistorante!!.COD_BR && it.ID == utenteLoggato.ID
        }
        val badgesUtenteLoggato = utentiBadgeRistorante.filter {
            it.ID == utenteLoggato.ID
        }
        val isPreferito = utenteScansionaRistoranteViewModel.getRistorantiPreferitiPerUtente(utenteLoggato.ID.toString()).collectAsState(
            initial = listOf()
        ).value.filter { it == selectedRistorante!!.COD_RIS }

        val openDialog = remember { mutableStateOf(false)  }
        val scroll = rememberScrollState(0)

        if (!tipoRistorante?.tipi.isNullOrEmpty() && !filtriSelectedRistorante?.filtri.isNullOrEmpty()) {
            val tipo: TipoRistorante = tipoRistorante!!.tipi[0]
            val filtriConsegna = filtriSelectedRistorante!!.filtri

            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .verticalScroll(scroll)
            ) {
                Header(ristorante = selectedRistorante!!,
                    tipoRistorante = tipo,
                    filtriConsegna,
                    utenteScansionaRistoranteViewModel,
                    isPreferito,
                    utenteLoggato
                )
                AsyncImage(
                    model = selectedRistorante.icona,
                    contentDescription = "immagine ristorante",
                    modifier = Modifier
                        .size(size = 300.dp)
                        .align(CenterHorizontally)
                        .padding(vertical = 10.dp),
                )

                Text(
                    text = selectedRistorante.nome,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                )

                ZonaUtente(
                    ristorante = selectedRistorante,
                    utentePossiedeBadgeRistoranteViewModel,
                    context,
                    badgeUtenteLoggatoRistoranteSelected,
                    badgesUtenteLoggato,
                    utenteLoggato.ID,
                    ristoranteViewModel.isRistoranteAperto(selectedRistorante),
                    utenteLoggato,
                    utenteViewModel,
                    utentePossiedeBadgeUtenteViewModel
                )
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
}

@Composable
fun Header(
    ristorante: Ristorante,
    tipoRistorante: TipoRistorante,
    filtriConsegna: List<FiltroConsegna>,
    utenteScansionaRistoranteViewModel: UtenteScansionaRistoranteViewModel,
    isPreferito: List<Int>,
    utenteLoggato: Utente
){
    var filtriText = ""
    filtriConsegna.forEach{
        filtriText += it.filtro + " / "
    }
    filtriText = filtriText.removeSuffix(" / ")

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
            onClick = {
                val newPreferito = isPreferito.isEmpty()
                utenteScansionaRistoranteViewModel.updatePreferito(
                    UtenteRistoranteCrossRef(
                        ID = utenteLoggato.ID,
                        COD_RIS = ristorante.COD_RIS,
                        preferito = newPreferito
                    )
                )
            },
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
                tint = if(isPreferito.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
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
    utentePossiedeBadgeRistoranteViewModel: UtentePossiedeBadgeRistoranteViewModel,
    context: Context,
    badgeUtenteLoggatoRistoranteSelected: List<UtenteBadgeRistoranteCrossRef>,
    badgeUtenteLoggato: List<UtenteBadgeRistoranteCrossRef>,
    ID: Int,
    isRistoranteAperto: Boolean,
    utenteLoggato: Utente,
    utenteViewModel: UtenteViewModel,
    utentePossiedeBadgeUtenteViewModel: UtentePossiedeBadgeUtenteViewModel
) {
    val options = ScanOptions()
    options.setOrientationLocked(false)

    val date = Calendar.getInstance().time
    val sdf = SimpleDateFormat("dd/M/yyyy")
    val currentDate = sdf.format(date)

    var badge = UtenteBadgeRistoranteCrossRef(ID, ristorante.COD_BR, currentDate,0)

    if(badgeUtenteLoggatoRistoranteSelected.isNotEmpty()) {
        badge = badgeUtenteLoggatoRistoranteSelected[0]
    }

    val barcodeLauncher = rememberLauncherForActivityResult(contract = ScanContract()) { result ->
        if(result.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            val esperienza = badge.esperienzaBadge + 30
            val newBadge = UtenteBadgeRistoranteCrossRef(badge.ID, badge.COD_BR, badge.dataAcquisizione, esperienza)
            utentePossiedeBadgeRistoranteViewModel.newScansione(newBadge)
            val expTot = utenteLoggato.esperienzaTotale + 30
            utenteViewModel.updateExp(utenteLoggato.ID, expTot.toString())
            Toast.makeText(context, "+30 Esperienza", Toast.LENGTH_LONG).show()

            if(badgeUtenteLoggato.isEmpty()) {  //BADGE SCANSIONA UN RISTORANTE
                utentePossiedeBadgeUtenteViewModel.newBadgeUtente(
                    UtenteBadgeUtenteCrossRef(
                        badge.ID, COD_BU = 2, dataAcquisizione = currentDate, esperienzaBadge = 1
                    )
                )
            }
            if(badge.COD_BR == 2) {  //BADGE 5 HAMBURGHERIE --> solo il ristorante 2 è amburgheria
                utentePossiedeBadgeUtenteViewModel.newBadgeUtente(
                    UtenteBadgeUtenteCrossRef(
                        badge.ID, COD_BU = 3, dataAcquisizione = currentDate, esperienzaBadge = badgeUtenteLoggato.filter { it.COD_BR == 2}.size
                    )
                )
            }
            //BADGE SCANSIONA 50 RISTORANTI
            utentePossiedeBadgeUtenteViewModel.newBadgeUtente(
                UtenteBadgeUtenteCrossRef(
                    badge.ID, COD_BU = 4, dataAcquisizione = currentDate, esperienzaBadge = badgeUtenteLoggato.size
                )
            )

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
        val isScanButtonEnabled = isRistoranteAperto && if(badgeUtenteLoggatoRistoranteSelected.isNotEmpty()) badgeUtenteLoggatoRistoranteSelected[0].dataAcquisizione != currentDate else true  //&& isAbbastanzaVicino
        Text(text = "Livello " + (badge.esperienzaBadge / 100).toString() +
                " (" + badge.esperienzaBadge + "/" + ((badge.esperienzaBadge - badge.esperienzaBadge%100) + 100).toString() + ")",
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
                ),
            enabled = isScanButtonEnabled
        ) {
            Icon(
                imageVector = Icons.Filled.QrCode,
                contentDescription = "Apri camera per codice QR",
                tint = if(isScanButtonEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
            )
        }
    }
    LinearProgressIndicator(progress = (badge.esperienzaBadge % 100f)/100)
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
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
    context.startActivity(intent)
}