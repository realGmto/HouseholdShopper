package com.householdshopper

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.google.firebase.auth.FirebaseUser
import com.householdshopper.view.AddMemberScreen
import com.householdshopper.view.CreateHouseholdScreen
import com.householdshopper.view.CreateShoppingListScreen
import com.householdshopper.view.HomeScreen
import com.householdshopper.view.HouseholdScreen
import com.householdshopper.view.InvitesScreen
import com.householdshopper.view.LoginScreen
import com.householdshopper.view.RegisterScreen
import com.householdshopper.view.RestrictedInvitesScreen
import com.householdshopper.view.RestrictedScreen
import com.householdshopper.view.ShoppingListScreen
import com.householdshopper.viewmodel.AddMemberViewModel
import com.householdshopper.viewmodel.CreateHouseholdViewModel
import com.householdshopper.viewmodel.CreateListViewModel
import com.householdshopper.viewmodel.HomeViewModel
import com.householdshopper.viewmodel.HouseholdViewModel
import com.householdshopper.viewmodel.InvitesViewModel
import com.householdshopper.viewmodel.LoginViewModel
import com.householdshopper.viewmodel.RegisterViewModel
import com.householdshopper.viewmodel.RestrictedInvitesViewModel
import com.householdshopper.viewmodel.RestrictedViewModel
import com.householdshopper.viewmodel.ShoppingListViewModel

@Composable
fun AppNavHost(navController: NavHostController, currentUser: FirebaseUser?, inHousehold: Boolean){
    val uri = "https://household-shopper.com"

    NavHost(navController = navController, startDestination =  if (currentUser != null && inHousehold) "home" else if (currentUser != null) "restricted" else "login") {
        composable("login") {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable("register"){
            val viewModel = hiltViewModel<RegisterViewModel>()
            RegisterScreen(navController = navController, viewModel = viewModel)
        }
        composable("restricted"){
            val viewModel = hiltViewModel<RestrictedViewModel>()
            RestrictedScreen(navController = navController, viewModel = viewModel)
        }
        composable("invitesToHousehold"){
            val viewModel = hiltViewModel<RestrictedInvitesViewModel>()
            RestrictedInvitesScreen(navController = navController, viewModel = viewModel)
        }
        composable("home") {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable("createList"){
            val viewModel = hiltViewModel<CreateListViewModel>()
            CreateShoppingListScreen(navController = navController, viewModel = viewModel)
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
        composable("createHousehold"){
            val viewModel = hiltViewModel<CreateHouseholdViewModel>()
            CreateHouseholdScreen(navController = navController, viewModel = viewModel)
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
        composable(
            route = "addMember",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$uri/addMember"
                    action = Intent.ACTION_VIEW
                }
            )
        ){
            val viewModel = hiltViewModel<AddMemberViewModel>()
            AddMemberScreen(viewModel = viewModel, navController = navController)
        }
    }
}