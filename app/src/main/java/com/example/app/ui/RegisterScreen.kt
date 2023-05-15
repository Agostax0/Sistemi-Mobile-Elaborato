package com.example.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterButtonClicked: ()->Unit,
    onLoginButtonClicked: ()->Unit
    ){

    val username = remember { mutableStateOf(TextFieldValue()) }
    val name = remember { mutableStateOf(TextFieldValue()) }
    val surname = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val email = remember { mutableStateOf(TextFieldValue()) }

    //TODO carica foto profilo


    Column (
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            TextField(
                value = username.value,
                onValueChange = { username.value = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = password.value, onValueChange = { password.value = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = name.value,
                onValueChange = { name.value = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = surname.value,
                onValueChange = { surname.value = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(20.dp))


        }


    }

}