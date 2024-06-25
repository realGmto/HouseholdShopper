package com.householdshopper

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
import com.google.firebase.auth.FirebaseAuth
import com.householdshopper.model.repository.SharedDataRepository
import com.householdshopper.view.CreateShoppingListScreen
import com.householdshopper.view.HouseholdScreen
import com.householdshopper.viewmodel.CreateListViewModel
import com.householdshopper.viewmodel.HouseholdViewModel
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
                    NavHost(navController = navController, startDestination =  if (currentUser != null) "home" else "login") {
                        composable("login") {
                            val viewModel = hiltViewModel<LoginViewModel>()
                            LoginScreen(navController = navController, viewModel = viewModel)
                        }
                        composable("register"){
                            val viewModel = hiltViewModel<RegisterViewModel>()
                            RegisterScreen(navController = navController, viewModel = viewModel)
                        }
                        composable("home") {
                            val viewModel = hiltViewModel<HomeViewModel>()
                            HomeScreen(navController = navController, viewModel = viewModel)
                        }
                        composable("createList"){
                            val viewModel = hiltViewModel<CreateListViewModel>()
                            CreateShoppingListScreen(navController = navController, createListViewModel = viewModel)
                        }
                        composable("list/{listId}"){backStackEntry ->
                            val listId = backStackEntry.arguments?.getString("listId")!!
                            val viewModel = hiltViewModel<ShoppingListViewModel>()
                            ShoppingListScreen(navController = navController, viewModel = viewModel, listId= listId)
                        }
                        composable("household"){
                            val viewModel = hiltViewModel<HouseholdViewModel>()
                            HouseholdScreen(navController = navController, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}