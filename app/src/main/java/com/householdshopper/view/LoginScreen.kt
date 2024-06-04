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
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.AsyncImage
import com.householdshopper.ui.theme.blue
import com.householdshopper.viewmodel.LoginViewModel
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel){

    val annotatedString = buildAnnotatedString {
        append("Don’t have account? ")

        pushStringAnnotation(tag = "REGISTER", annotation = "register")
        withStyle(style = SpanStyle(color = blue)) {
            append("Register")
        }
        pop()

        append(" now")
    }
    val loginResult by viewModel.loginResult.collectAsState()
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
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
        Text(text = "Household Shopper",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(48.dp))
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
        Spacer(modifier = Modifier.height(8.dp))
        loginResult?.let {
            if(it.success){
                LaunchedEffect(Unit) {
                    navController.navigate("home") // TODO advanced navigation based on if household already exists
                }
            }
            Text(
                text = it.message,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        ClickableText(
            text = annotatedString,
            style = TextStyle(fontSize = 16.sp, color = Color.White),
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "REGISTER", start = offset, end = offset)
                    .firstOrNull()?.let {
                            navController.navigate("register")
                    }
                }
            )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {viewModel.login(email,password)},
            modifier = Modifier.width(150.dp)
            ) {
            Text(text = "Login")
        }
    }
}