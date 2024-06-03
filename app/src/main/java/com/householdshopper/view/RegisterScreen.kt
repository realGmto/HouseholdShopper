package com.householdshopper.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.householdshopper.ui.theme.blue
import com.householdshopper.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(navController: NavHostController, registerViewModel: RegisterViewModel = viewModel()){
    val annotatedString = buildAnnotatedString {
        append("Already have account? ")

        pushStringAnnotation(tag = "LOGIN", annotation = "login")
        withStyle(style = SpanStyle(color = blue)) {
            append("Login")
        }
        pop()

        append(" now")
    }

    val registerResult by registerViewModel.registerResult.collectAsState()
    var username by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var repeatPassword by remember {
        mutableStateOf("")
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        AsyncImage(
            model = "https://static-00.iconduck.com/assets.00/cart-shopping-list-icon-493x512-fh2rzzxm.png",
            contentDescription = "Logo of Household Shopper",
            modifier = Modifier.width(96.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(text = "Register",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(48.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username")},
            modifier = Modifier
                .width(250.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email")},
            modifier = Modifier
                .width(250.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = { Text("Password")},
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .width(250.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = repeatPassword,
            onValueChange = {repeatPassword = it},
            label = { Text("Repeat password")},
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .width(250.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        ClickableText(
            text = annotatedString,
            style = TextStyle(fontSize = 16.sp, color = Color.White),
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "LOGIN", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navController.navigate("login")
                    }
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {/*TODO*/},
            modifier = Modifier.width(150.dp)
        ) {
            Text(text = "Register")
        }
    }
}