package com.example.app.ui

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.example.app.R
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onMapButtonClicked: ()-> Unit,
               onRestaurantClicked: ()->Unit,
//               placesViewModel:
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onMapButtonClicked },
                containerColor = White) {
                Icon(
                    Icons.Filled.Map,
                    contentDescription = "Map",
                    tint = Orange
                )
            }
        }
    ) {contentPadding ->
        Column(Modifier.padding(contentPadding)) {

        }
    }


}