package com.householdshopper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.householdshopper.model.repository.LoginRepository
import com.householdshopper.model.repository.RegisterRepository
import com.householdshopper.model.repository.ShoppingListRepository
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
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val loginRepository = LoginRepository()
    val loginViewModel: LoginViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return LoginViewModel(loginRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
    val registerRepository = RegisterRepository()
    val registerViewModel: RegisterViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return RegisterViewModel(registerRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
    val shoppingListRepository = ShoppingListRepository()
    val homeViewModel: HomeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
                    @Suppress("UNCHECKED_CAST")
                    return HomeViewModel(shoppingListRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
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
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(navController = navController, loginViewModel = loginViewModel)
                        }
                        composable("register"){
                            RegisterScreen(navController = navController, registerViewModel = registerViewModel)
                        }
                        composable("home") {
                            HomeScreen(navController = navController, homeViewModel = homeViewModel)
                        }
                        composable("list/{listId}"){backStackEntry ->
                            val listId = backStackEntry.arguments?.getString("listId")
                            val viewModel = hiltViewModel<ShoppingListViewModel>()
                            ShoppingListScreen(navController = navController, viewModel = viewModel, listId= listId)
                        }
                    }
                }
            }
        }
    }
}