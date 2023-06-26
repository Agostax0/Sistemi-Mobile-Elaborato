package com.example.app.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app.R
import com.example.app.data.entity.BadgeUtente
import com.example.app.data.entity.Ristorante
import java.lang.Math.min
import java.lang.Math.sqrt

import com.example.app.ui.theme.*
import com.example.app.viewModel.SettingsViewModel

import com.example.app.ui.Header
import com.example.app.viewModel.BadgeUtenteViewModel
import com.example.app.viewModel.RistoranteViewModel
import com.example.app.viewModel.UtentePossiedeBadgeRistoranteViewModel
import com.example.app.viewModel.UtentePossiedeBadgeUtenteViewModel
import com.example.app.viewModel.UtenteScansionaRistoranteViewModel
import com.example.app.viewModel.UtenteViewModel

val rainbowBrush = Brush.sweepGradient(
    listOf(Color(0xFF9575CD),Color(0xFFBA68C8),Color(0xFFE57373),Color(0xFFFFB74D),Color(0xFFFFF176), Color(0xFFAED581), Color(0xFF4DD0E1),Color(0xFF9575CD)))

class CustomBrush2(val rarity: Int, val exchangeColor: Color, val theme: String):ShaderBrush(){

    val startColor: Color = when(theme){
        "Chiaro"->
            when(rarity){
                3-> Color(red = 228, green = 172, blue = 1, alpha = 255)
                2-> Color(red = 144, green = 153, blue = 180, alpha = 255)
                else->Color(red = 143, green = 68, blue = 91, alpha = 255)
            }
        else->
            when(rarity){
                3->Color(red = 245, green = 233, blue = 171, alpha = 255)
                2-> Color(red = 245, green = 245, blue = 255, alpha = 255)
                else->Color(red = 255, green = 201, blue = 152, alpha = 255)
            }
        }

    val mainColor: Color = when(rarity){3->Gold 2-> Silver else-> Copper}

    val colorList: List<Color> = listOf(startColor,mainColor,exchangeColor)

    override fun createShader(size: Size): Shader {
        return LinearGradientShader(
            colors = colorList,
            from = Offset.Zero,
            to = Offset(0f, size.height/2f),
            tileMode = TileMode.Mirror
        )
    }

}



@Composable
fun BadgeDisplayScreen(
    settingsViewModel: SettingsViewModel,
    utenteViewModel: UtenteViewModel,
    session :String,
    utenteScansionaRistoranteViewModel: UtenteScansionaRistoranteViewModel,
    utentePossiedeBadgeRistoranteViewModel : UtentePossiedeBadgeRistoranteViewModel,
    ristoranteViewModel: RistoranteViewModel,
    utentePossiedeBadgeUtenteViewModel : UtentePossiedeBadgeUtenteViewModel,
    badgeUtenteViewModel: BadgeUtenteViewModel,
    navigateToRestaurant: ()->Unit,
){
    val context = LocalContext.current
    val utenti by utenteViewModel.utenti.collectAsState(initial = listOf())
    val currentTheme = settingsViewModel.theme.collectAsState(initial = "").value

    if(utenti.isNotEmpty() || utenteViewModel.utenteLoggato != null) {
        val utenteLoggato = if (utenteViewModel.utenteLoggato == null)
            utenti.find { it.username == session }!! else utenteViewModel.utenteLoggato!!

        /**
         * Lista dei badgeUtente con ID e BADGE_ID, data ed esp dell'utente loggato
         */
        val badgeUtenteRef = utentePossiedeBadgeUtenteViewModel.utentiBadgeUtenteRef.collectAsState(
            initial = listOf()
        ).value.filter { it-> it.ID == utenteLoggato.ID }


        val ristoranti: List<Ristorante> = ristoranteViewModel.ristoranti.collectAsState(initial = listOf()).value

        /**
         * Lista del ristoranteRef dei badge ristoranti posseduti
         */
        val ristorantiVisitatiCrossRef = utentePossiedeBadgeRistoranteViewModel.utentiBadgeRistorante.collectAsState(initial = listOf())
            .value.filter { it.ID == utenteLoggato.ID }

        /**
         * alla pos [0] c'è una lista di BadgeUtente
         */
        val badgesUtenteLoggatoCrossRef = utentePossiedeBadgeUtenteViewModel.utentiBadgeUtente.collectAsState(
            initial = listOf()
        ).value.filter { it.utente.ID == utenteLoggato.ID }

        val badgeUtente: ArrayList<BadgeInfo> = ArrayList<BadgeInfo>()

        if(badgesUtenteLoggatoCrossRef.isNotEmpty() && badgeUtenteRef.isNotEmpty()){
            badgesUtenteLoggatoCrossRef[0].badgeUtenti.forEach { badge ->
                    val crossRefInfo = badgeUtenteRef.filter { it.COD_BU == badge.COD_BU }.first()

                    if((crossRefInfo.COD_BU == 3 && crossRefInfo.esperienzaBadge >= 5) ||
                        (crossRefInfo.COD_BU == 4 && crossRefInfo.esperienzaBadge >= 50) ||
                        !listOf(3,4).contains(crossRefInfo.COD_BU)) {
                        badgeUtente.add(
                            BadgeInfo(
                                badgeURL = badge.icona,
                                badgeDescription = badge.descrizione,
                                badgeName = badge.nome,
                                rarity = badge.livello,
                                experienceObtained = crossRefInfo.esperienzaBadge,
                                acquisitionDate = crossRefInfo.dataAcquisizione,
                                onBadgeClick = {
                                    showBadgeInfo.value = BadgeInfo(
                                        badgeURL = badge.icona,
                                        badgeDescription = badge.descrizione,
                                        badgeName = badge.nome,
                                        rarity = badge.livello,
                                        experienceObtained = crossRefInfo.esperienzaBadge,
                                        acquisitionDate = crossRefInfo.dataAcquisizione,
                                        onBadgeClick = {}
                                    )
                                    showDialog.value = true
                                })
                        )
                    }
            }
        }

        var badgeUtenteMancanti: ArrayList<BadgeInfo> = ArrayList<BadgeInfo>()

        val tuttiIBadge = badgeUtenteViewModel.badgeUtenti.collectAsState(initial = listOf()).value

        if(tuttiIBadge.isNotEmpty() && badgesUtenteLoggatoCrossRef.isNotEmpty()){
            val badgePossedutiDaUtente = badgesUtenteLoggatoCrossRef[0].badgeUtenti

            badgeUtenteMancanti = tuttiIBadge.filter { !badgePossedutiDaUtente.contains(it) }.map { BadgeInfo(
                badgeURL = it.icona,
                badgeName = it.nome,
                badgeDescription = it.descrizione,
                onBadgeClick = {
                    showBadgeInfo.value = BadgeInfo(
                        badgeURL = it.icona,
                        badgeDescription = it.descrizione,
                        badgeName = it.nome,
                        rarity = it.livello,
                        experienceObtained = 0,
                        acquisitionDate = "",
                        onBadgeClick = {}
                    )
                    showDialog.value = true
                },
                rarity = it.livello,
                acquisitionDate = "",
                experienceObtained = 0,
            )} as ArrayList<BadgeInfo>
        }

        val badgeRistoranti: ArrayList<BadgeInfo> = ArrayList<BadgeInfo>()

        if(ristorantiVisitatiCrossRef.isNotEmpty() && ristoranti.isNotEmpty()){

            for(ristoranteRef in ristorantiVisitatiCrossRef){
                val ristInfo = ristoranti.filter { it.COD_BR == ristoranteRef.COD_BR }.first()

                badgeRistoranti.add(
                    BadgeInfo(
                    badgeURL = ristInfo.icona,
                    badgeName = ristInfo.nome,
                        badgeDescription = "placeholder",
                        rarity = 0,
                        acquisitionDate = ristoranteRef.dataAcquisizione,
                        experienceObtained = ristoranteRef.esperienzaBadge,
                        onBadgeClick = {
                            ristoranteViewModel.selectRistorante(ristInfo)
                            navigateToRestaurant()
                        }
                )
                )
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
        ){

            Header(mainText = "Badge Ottenuti: ${badgeUtente.size}")

            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 72.dp), modifier = Modifier.padding(bottom = 9.dp, top=3.dp)){
                items(badgeUtente){badge->

                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)){
                        Box(){
                            AsyncImage(
                                model = badge.badgeURL,
                                contentDescription = badge.badgeDescription,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .clip(HexagonShape())
                                    .size(72.dp)
                                    .clickable { badge.onBadgeClick() }

                                    .border(
                                        width = 8.dp,

                                        if (badge.rarity == 0) rainbowBrush else
                                            CustomBrush2(
                                                badge.rarity,
                                                MaterialTheme.colorScheme.primaryContainer,
                                                currentTheme
                                            ),
                                        HexagonShape()
                                    )
                            )
                        }

                        Text(badge.acquisitionDate, style = TextStyle(fontSize = 11.sp), textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())

                    }
                }
            }

            Header(mainText = "Badge Ristoranti: ${badgeRistoranti.size}")

            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 72.dp),modifier = Modifier.padding(bottom = 9.dp, top=3.dp)){
                items(badgeRistoranti){badge->


                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)){
                        Box(modifier = Modifier.padding(bottom = 5.dp)){
                            AsyncImage(
                                model = badge.badgeURL,
                                contentDescription = badge.badgeDescription,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .clip(HexagonShape())
                                    .size(72.dp)
                                    .clickable { badge.onBadgeClick() }


                            )
                        }

                        Text(badge.acquisitionDate, style = TextStyle(fontSize = 11.sp), textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())

                    }
                }
            }

            Header(mainText = "Badge Non Ottenuti: ${badgeUtenteMancanti.size}")

            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 72.dp),modifier = Modifier.padding(bottom = 9.dp, top=3.dp)){
                items(badgeUtenteMancanti){badge->

                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)){
                        AsyncImage(
                            model = badge.badgeURL,
                            contentDescription = badge.badgeDescription,
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply{setToSaturation(0f)}),
                            modifier = Modifier
                                .size(72.dp)
                                .clip(HexagonShape())
                                .border(
                                    width = 8.dp,

                                    if (badge.rarity == 0) rainbowBrush else
                                        CustomBrush2(
                                            badge.rarity,
                                            MaterialTheme.colorScheme.primaryContainer,
                                            currentTheme
                                        ),
                                    HexagonShape()
                                )
                                .alpha(0.3f)
                                .clickable { badge.onBadgeClick() }
                                .padding(bottom = 5.dp)
                        )

                    }
                }
            }

        }
        if(showDialog.value){
            AlertDialog(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                textContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onDismissRequest = {
                    showDialog.value = false
                },
                title = {
                    showBadgeInfo.value!!.badgeName
                },
                text = {
                    val info = showBadgeInfo.value!!
                    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = info.badgeURL,
                            contentDescription = info.badgeName,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .clip(HexagonShape())
                                .size(120.dp)
                                .border(
                                    8.dp,
                                    if (info.rarity == 0) rainbowBrush else
                                        CustomBrush2(
                                            info.rarity,
                                            MaterialTheme.colorScheme.primaryContainer,
                                            currentTheme
                                        ),
                                    HexagonShape()
                                )
                        )
                        Text(info.badgeDescription)
                    }
                },
                confirmButton = {}
            )
        }

    }

}

class HexagonShape: Shape{
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ) = Outline.Generic(Path().apply {

        val radius = min(size.width / 2f, size.height / 2f)

        val triangleHeight = (kotlin.math.sqrt(3.0) * radius / 2)
        val centerX = size.width / 2
        val centerY = size.height / 2

        moveTo(centerX, centerY + radius)
        lineTo((centerX - triangleHeight).toFloat(), centerY + radius/2)
        lineTo((centerX - triangleHeight).toFloat(), centerY - radius/2)
        lineTo(centerX, centerY - radius)
        lineTo((centerX + triangleHeight).toFloat(), centerY - radius/2)
        lineTo((centerX + triangleHeight).toFloat(), centerY + radius/2)

        close()
    })
}

class BadgeInfo(val badgeURL:String,val rarity: Int, val badgeDescription: String, val badgeName: String, val acquisitionDate: String, val experienceObtained: Int, val onBadgeClick: ()->Unit){
}

val showDialog = mutableStateOf(false)
val showBadgeInfo: MutableState<BadgeInfo?> = mutableStateOf(null)
