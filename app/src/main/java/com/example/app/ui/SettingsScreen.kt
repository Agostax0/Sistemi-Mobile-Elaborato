package com.example.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.Orange
import com.example.app.viewModel.SettingsViewModel
import com.example.app.viewModel.UtenteViewModel

@Composable
fun SettingsScreen(
    onLogoutButtonClicked: () -> Unit,
    settingsViewModel: SettingsViewModel,
    utenteViewModel: UtenteViewModel
) {
    val radioOptions = listOf("Chiaro", "Scuro")
    val theme = settingsViewModel.theme.collectAsState(initial = "")
    val scroll = rememberScrollState(0)
    // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(
        Modifier.selectableGroup().verticalScroll(scroll)) {
        Text(
            text = "Aspetto",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(all = 5.dp)
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tema",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            radioOptions.forEach { text ->
                Text(
                    text = text,
                    color = (
                        if (text == theme.value) {
                            Color.White
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        }
                    ),
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(size = 14.dp),
                        )
                        .clickable {
                            settingsViewModel.saveTheme(text)
                        }
                        .background(
                            if (text == theme.value) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.primaryContainer
                            }
                        )
                        .padding(
                            vertical = 12.dp,
                            horizontal = 19.dp,
                        )
                        .weight(0.55f)
                )
            }
        }

        Divider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 1.dp,
            modifier = Modifier.padding(15.dp)
        )

        Text(
            text = "Modifica profilo",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(horizontal = 5.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                utenteViewModel.clearSession()

                onLogoutButtonClicked()
            },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Orange),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ){
            Text(text = "LOGOUT")
        }
    }
}
