package com.example.app

import android.app.Application
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app.data.FoodAppDB
import com.example.app.ui.HomeScreen
import com.example.app.ui.SettingsScreen
import com.example.app.ui.LoginScreen
import com.example.app.ui.MapScreen
import com.example.app.ui.RegisterScreen
import com.example.app.ui.ProfileScreen
/*import com.example.app.data.FoodAppDB
import com.example.app.ui.AddScreen
import com.example.app.ui.DetailsScreen


import com.example.app.viewModel.PlacesViewModel
import com.example.app.viewModel.SettingsViewModel*/
import dagger.hilt.android.HiltAndroidApp

sealed class AppScreen(val name: String){
    object Home : AppScreen("Home")
    object Settings : AppScreen("Settings Screen")
    object Login : AppScreen("Login Screen")
    object Restaurant : AppScreen("Restaurant Screen")
    object Map : AppScreen("Map Screen")
    object Profile : AppScreen("Profile Screen")
    object Register : AppScreen("Register Screen")
}

class FoodApp : Application(){
    // lazy --> the database and the repository are only created when they're needed
    val database by lazy { FoodAppDB.getDatabase(this) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarFunction(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    onSettingsButtonClicked: () -> Unit,
    onProfileButtonClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = currentScreen,
                fontWeight = FontWeight.Medium,
            )
        },
        modifier = modifier,
        navigationIcon = {
            //se si può navigare indietro (non home screen) allora appare la freccetta
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back button",
                        tint = colorResource(id = R.color.white)
                    )
                }
            }
        },
        actions = {
            if (currentScreen == AppScreen.Profile.name) {
                IconButton(onClick = onSettingsButtonClicked ) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = stringResource(id = R.string.settings),
                        tint = colorResource(id = R.color.white)
                    )}
            }
            else{
                IconButton(onClick =  onProfileButtonClicked ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = stringResource(id = R.string.profile),
                        tint = colorResource(id = R.color.white)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = colorResource(id = R.color.main_orange)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp(
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = backStackEntry?.destination?.route ?: AppScreen.Home.name

    Scaffold(
        topBar = {
            TopAppBarFunction(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                onSettingsButtonClicked = { navController.navigate(AppScreen.Settings.name) },
                onProfileButtonClicked = { navController.navigate(AppScreen.Profile.name) }
            )
        }
    ) { innerPadding ->
        NavigationGraph(navController, innerPadding)
    }
}


@Composable
private fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    //val placesViewModel = hiltViewModel<PlacesViewModel>()
    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.name,
        modifier = modifier.padding(innerPadding)
    ) {
        composable(route = AppScreen.Home.name) {
            HomeScreen(
                onMapButtonClicked = {
                    //Mappa
                    navController.navigate(AppScreen.Map.name)
                },
                onRestaurantClicked = {
                    navController.navigate(AppScreen.Restaurant.name)
                },
                //placesViewModel = placesViewModel
            )
        }
        composable(route = AppScreen.Map.name) {
            MapScreen(
                //MapScreen
                onRestaurantTickClicked = {
                    //carica il ristorante
                    navController.navigate(AppScreen.Restaurant.name)
                   //navController.popBackStack(AppScreen.Restaurant.name, inclusive = false)
                },
                //placesViewModel = placesViewModel
            )
        }
        composable(route = AppScreen.Login.name) {
            LoginScreen(
                onLoginButtonClicked ={
                    //controllo login
                    navController.navigate(AppScreen.Home.name)
                },
                onRegisterButtonClicked = {
                    navController.navigate(AppScreen.Register.name)
                },
                //placesViewModel = placesViewModel
            )
        }
        composable(route = AppScreen.Settings.name) {
            //val settingsViewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(
                onLogoutButtonClicked = {
                    //logout
                    navController.navigate(AppScreen.Login.name)
                }
                //settingsViewModel
            )
        }
        composable(route = AppScreen.Profile.name){
            ProfileScreen(

                //placesViewModel = placesViewModel
            )
        }
    }
}