package com.example.app.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.AppScreen
import com.example.app.R
import com.example.app.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginButtonClicked: ()->Unit,
    onRegisterButtonClicked: ()->Unit,
    showError: Boolean,
//               placesViewModel:
) {

    Log.d("NAV_TAG" + " LoginScreen.kt" ,"entering with : "+ showError)

    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString("Registrati"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = { Log.d("NAV_TAG " + "LoginScreen.kt","onRegisterButtonClicked")
                        onRegisterButtonClicked() },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                textDecoration = TextDecoration.Underline,
                color = Purple40
            )
        )
    }
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }

        Text(text = stringResource(id = R.string.app_name), style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive))

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Username") },
            value = username.value,
            onValueChange = { username.value = it })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Password") },
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password.value = it })

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = { Log.d("NAV_TAG " + "LoginScreen.kt","onLoginButtonClicked")
                            onLoginButtonClicked()},
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "LOGIN", color = White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Forgot password?"),
            onClick = { /* TODO */ },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        if(showError){
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Username e/o Password incorrette", style = TextStyle(fontSize = 13.sp), color = Color.Red)
        }



    }

}