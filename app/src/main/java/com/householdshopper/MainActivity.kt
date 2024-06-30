package com.householdshopper

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.householdshopper.ui.theme.HouseholdShopperTheme
import com.householdshopper.view.HomeScreen
import com.householdshopper.view.LoginScreen
import com.householdshopper.view.RegisterScreen
import com.householdshopper.view.ShoppingListScreen
import com.householdshopper.viewmodel.HomeViewModel
import com.householdshopper.viewmodel.LoginViewModel
import com.householdshopper.viewmodel.RegisterViewModel
import com.householdshopper.viewmodel.ShoppingListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.navDeepLink
import com.google.firebase.auth.FirebaseAuth
import com.householdshopper.model.repository.SharedDataRepository
import com.householdshopper.view.CreateShoppingListScreen
import com.householdshopper.view.HouseholdScreen
import com.householdshopper.view.InvitesScreen
import com.householdshopper.viewmodel.CreateListViewModel
import com.householdshopper.viewmodel.HouseholdViewModel
import com.householdshopper.viewmodel.InvitesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HouseholdShopperTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val navController = rememberNavController()
                    val currentUser = FirebaseAuth.getInstance().currentUser

                    val deepLinkUri = intent?.data

                    deepLinkUri?.let {
                        navController.navigate(it.toString())
                    }

                    AppNavHost(navController = navController, currentUser = currentUser)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}