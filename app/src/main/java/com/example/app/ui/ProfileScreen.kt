package com.example.app.ui

import android.R
import android.graphics.Paint.Align
import android.graphics.drawable.shapes.OvalShape
import android.util.Log
import android.widget.GridLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.request.ImageRequest
import com.example.app.data.entity.BadgeUtente
import com.example.app.data.relation.UtenteBadgeUtenteCrossRef
import com.example.app.viewModel.RistoranteViewModel
import com.example.app.viewModel.UtentePossiedeBadgeRistoranteViewModel
import com.example.app.viewModel.UtenteScansionaRistoranteViewModel
import com.example.app.viewModel.UtenteViewModel

import com.example.app.ui.theme.Gold
import com.example.app.ui.theme.Silver
import com.example.app.ui.theme.Copper
import com.example.app.viewModel.BadgeUtenteViewModel
import com.example.app.viewModel.UtentePossiedeBadgeUtenteViewModel
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date

import com.example.app.ui.CustomBrush2
import com.example.app.ui.HexagonShape
import com.example.app.ui.rainbowBrush
import com.example.app.viewModel.SettingsViewModel


private const val LEVEL_THRESHOLD = 100

@Composable
fun ProfileScreen(
    utenteViewModel: UtenteViewModel,
    session: String,
    utenteScansionaRistoranteViewModel : UtenteScansionaRistoranteViewModel,
    utentePossiedeBadgeRistoranteViewModel: UtentePossiedeBadgeRistoranteViewModel,
    utentePossiedeBadgeUtenteViewModel: UtentePossiedeBadgeUtenteViewModel,
    ristoranteViewModel: RistoranteViewModel,
    badgeUtenteViewModel: BadgeUtenteViewModel,
    settingsViewModel: SettingsViewModel,
    navigateToRestaurant: ()->Unit,
) {
    val context = LocalContext.current
    val utenti by utenteViewModel.utenti.collectAsState(initial = listOf())
    val currentTheme = settingsViewModel.theme.collectAsState(initial = "Chiaro").value

    if(utenti.isNotEmpty() || utenteViewModel.utenteLoggato != null) {
        val utenteLoggato = if (utenteViewModel.utenteLoggato == null)
            utenti.find { it.username == session }!! else utenteViewModel.utenteLoggato!!


        /**
         * Ristoranti preferiti
         */
        val ristorantiPreferiti = utenteScansionaRistoranteViewModel.getRistorantiPreferitiPerUtente(utenteLoggato.ID.toString()).collectAsState(
            initial = listOf()
        ).value

        /**
         * Lista dei badgeUtente con ID e BADGE_ID
         */
        val badgeUtenteRef = utentePossiedeBadgeUtenteViewModel.utentiBadgeUtenteRef.collectAsState(
            initial = listOf()
        ).value

        /**
         * Lista dei codici ristorante dei badge posseduti
         */
        val ristorantiVisitatiCrossRef = utentePossiedeBadgeRistoranteViewModel.utentiBadgeRistorante.collectAsState(initial = listOf())
            .value.filter { it.ID == utenteLoggato.ID }.map { crossRef -> crossRef.COD_BR }

        val badgesUtenteLoggatoCrossRef = utentePossiedeBadgeUtenteViewModel.utentiBadgeUtente.collectAsState(
            initial = listOf()
        ).value.filter { it.utente.ID == utenteLoggato.ID }

        val badgePossedutiDaUtenteLoggato: List<BadgeUtente> = if(badgesUtenteLoggatoCrossRef.isEmpty()) listOf() else badgesUtenteLoggatoCrossRef[0].badgeUtenti

        val badgesRari = if(badgePossedutiDaUtenteLoggato.isEmpty()) listOf() else badgePossedutiDaUtenteLoggato.sortedByDescending { it.livello }.take(4)

        val numberOfBadgesObtained = badgePossedutiDaUtenteLoggato.size

        val numberOfFavoriteRestaurants = ristorantiPreferiti.size

        /**
         * lista di 3 elementi con 3 ristoranti tra quelli con cui si ha un badge ristorante
         */
        val ristorantiVisitati = ristoranteViewModel.ristoranti.collectAsState(initial = listOf()).value.filter {ristorantiVisitatiCrossRef.contains(it.COD_BR)}.take(4)

        val sdf = SimpleDateFormat("dd/M/yyyy")



        val currentLevel = (utenteLoggato.esperienzaTotale / LEVEL_THRESHOLD) // a 2002 exp user is level 20
        val currentProgressToNextLevel = utenteLoggato.esperienzaTotale % LEVEL_THRESHOLD //ranges from 0 to 100
        val experienceForNextLevel = ((utenteLoggato.esperienzaTotale / 100) + 1 ) * 100



        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(){
                AsyncImage(
                    model = ImageRequest
                        .Builder(context)
                        .data(utenteLoggato.icona)
                        .crossfade(true)
                        .build(),
                    contentDescription = "foto profilo",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(height = 120.dp, width = 80.dp)
                        .clip(RoundedCornerShape(14.dp))

                )

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text=utenteLoggato.username,
                        style= TextStyle(fontSize = 20.sp,
                            fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 25.dp, bottom = 7.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween

                    ){

                        Text("LIVELLO: $currentLevel")

                        Text("EXP: ${utenteLoggato.esperienzaTotale}/${experienceForNextLevel}")
                    }

                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primaryContainer,
                        progress = currentProgressToNextLevel.toFloat()/100
                    )

                }

            }

            Header("BADGE RARI", )

            Row(horizontalArrangement = Arrangement.SpaceEvenly){

                badgesRari.forEach{ badge ->
                    val badgeUtente = badgeUtenteRef.find { it.ID == utenteLoggato.ID && it.COD_BU == badge.COD_BU }
                    if((badgeUtente!!.COD_BU == 3 && badgeUtente.esperienzaBadge >= 5) ||
                        (badge.COD_BU == 4 && badgeUtente.esperienzaBadge >= 50) ||
                        !listOf(3,4).contains(badge.COD_BU)) {
                        Box(modifier = Modifier.padding(10.dp)){
                            AsyncImage(
                                model = badge.icona,
                                contentDescription = badge.nome,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(HexagonShape())
                                    .height(100.dp)
                                    .width(100.dp)
                                    .clickable {  } //TODO
                                    .border(
                                        width = 8.dp,

                                        if (badge.livello == 0) rainbowBrush else
                                            CustomBrush2(
                                                badge.livello,
                                                MaterialTheme.colorScheme.primaryContainer,
                                                currentTheme
                                            ),
                                        HexagonShape()
                                    )
                            ) }
                    }

                }
            }


            Header(mainText = "BADGE RISTORANTI",)

            Row(horizontalArrangement = Arrangement.SpaceEvenly){

                ristorantiVisitati.forEach{ ristorante->
                    Box(modifier = Modifier.padding(10.dp)){
                        AsyncImage(
                            model = ristorante.icona,
                            contentDescription = ristorante.nome,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .clip(HexagonShape())
                                .size(100.dp)
                                .clickable { ristoranteViewModel.selectRistorante(ristorante)
                                    navigateToRestaurant() }

                        )
                    }
                }
            }
        }
    }
}
@Composable
fun Header(mainText:String){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
            ){

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(vertical = 5.dp)

        ){

            Text(mainText,
                Modifier
                    .padding(horizontal = 10.dp))
        }


        Divider(
            color = MaterialTheme.colorScheme.primaryContainer,
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}