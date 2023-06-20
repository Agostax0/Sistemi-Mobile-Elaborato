package com.example.app.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.data.entity.Utente
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.Purple40
import com.example.app.ui.theme.White
import com.example.app.viewModel.UtenteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onSuccessfulRegister: ()->Unit,
    onLoginButtonClicked: ()->Unit,
    utenteViewModel: UtenteViewModel
    ){
    var username by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val passwordFocusRequester = FocusRequester()
    val nameFocusRequester = FocusRequester()
    val surnameFocusRequester = FocusRequester()
    val emailFocusRequester = FocusRequester()
    //TODO carica foto profilo

    val inputType = InputType.Name

    Column (
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_icon),
                contentDescription = "Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(60.dp).padding(end = 5.dp)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(fontSize = 30.sp, fontFamily = FontFamily.Cursive),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onLoginButtonClicked() },
            ) {
                Text(text = "TORNA AL LOGIN")
            }
        }
        Divider(
            color = MaterialTheme.colorScheme.primaryContainer,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 2.dp)
        )
        TextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(15)),
            leadingIcon = { Icon(imageVector = inputType.icon, null) },
            label = { Text(text = inputType.label) },
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = inputType.keyboardOptions,
            visualTransformation = inputType.visualTransformation,
            keyboardActions = KeyboardActions(onNext = {
                passwordFocusRequester.requestFocus()
            })
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester)
                .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(15)),
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, null) },
            label = { Text(text = inputType.label) },
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = inputType.keyboardOptions,
            visualTransformation = inputType.visualTransformation,
            keyboardActions = KeyboardActions(onNext = {
                nameFocusRequester.requestFocus()
            })
        )
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nameFocusRequester)
                .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(15)),
            leadingIcon = { Icon(imageVector = inputType.icon, null) },
            label = { Text(text = "Nome") },
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = inputType.keyboardOptions,
            visualTransformation = inputType.visualTransformation,
            keyboardActions = KeyboardActions(onNext = {
                surnameFocusRequester.requestFocus()
            })
        )
        TextField(
            value = surname,
            onValueChange = { surname = it },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(surnameFocusRequester)
                .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(15)),
            leadingIcon = { Icon(imageVector = inputType.icon, null) },
            label = { Text(text = "Cognome") },
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = inputType.keyboardOptions,
            visualTransformation = inputType.visualTransformation,
            keyboardActions = KeyboardActions(onNext = {
                emailFocusRequester.requestFocus()
            })
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocusRequester)
                .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(15)),
            leadingIcon = { Icon(imageVector = Icons.Default.Email, null) },
            label = { Text(text = "Email") },
            shape = MaterialTheme.shapes.small,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = inputType.visualTransformation,
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            })
        )

        Button(
            onClick = {
                val info = Utente(nome = name, cognome = surname, username = username, password = password, email = email, icona = "test", esperienzaTotale = 0L)

                utenteViewModel.addNewUtente(info)
                utenteViewModel.startSession(info)
                utenteViewModel.selectutente(info)
                onSuccessfulRegister()
            },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Orange),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "REGISTRATI", color = White)
        }
    }
}
