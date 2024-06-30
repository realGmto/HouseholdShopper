package com.householdshopper

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.google.firebase.auth.FirebaseUser
import com.householdshopper.view.CreateShoppingListScreen
import com.householdshopper.view.HomeScreen
import com.householdshopper.view.HouseholdScreen
import com.householdshopper.view.InvitesScreen
import com.householdshopper.view.LoginScreen
import com.householdshopper.view.RegisterScreen
import com.householdshopper.view.ShoppingListScreen
import com.householdshopper.viewmodel.CreateListViewModel
import com.householdshopper.viewmodel.HomeViewModel
import com.householdshopper.viewmodel.HouseholdViewModel
import com.householdshopper.viewmodel.InvitesViewModel
import com.householdshopper.viewmodel.LoginViewModel
import com.householdshopper.viewmodel.RegisterViewModel
import com.householdshopper.viewmodel.ShoppingListViewModel

@Composable
fun AppNavHost(navController: NavHostController, currentUser: FirebaseUser?){
    val uri = "https://household-shopper.com"

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
        composable(
            route = "invite",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$uri/invite"
                    action = Intent.ACTION_VIEW
                }
            )
        ){
            val viewModel = hiltViewModel<InvitesViewModel>()
            InvitesScreen(navController = navController, viewModel = viewModel)
        }
    }
}