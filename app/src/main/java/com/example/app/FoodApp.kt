package com.example.app

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.app.data.FoodAppDB
import com.example.app.ui.*
import com.example.app.ui.compose.LoginScreen
import com.example.app.ui.theme.Orange
import com.example.app.ui.theme.White
import com.example.app.viewModel.*
/*import com.example.app.data.FoodAppDB
import com.example.app.ui.AddScreen
import com.example.app.ui.DetailsScreen


import com.example.app.viewModel.PlacesViewModel
import com.example.app.viewModel.SettingsViewModel*/
import dagger.hilt.android.HiltAndroidApp

sealed class AppScreen(val name: String){
    object Home : AppScreen("Home")
    object Settings : AppScreen("Settings")
    object Login : AppScreen("Login")
    object Restaurant : AppScreen("Restaurant")
    object Map : AppScreen("Map")
    object Profile : AppScreen("Profile")
    object Register : AppScreen("Register")
}

@HiltAndroidApp
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
                color = MaterialTheme.colorScheme.onPrimary
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
                        tint = MaterialTheme.colorScheme.onPrimary
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
                            tint = MaterialTheme.colorScheme.onPrimary
                        )}
                }
                else {
                    IconButton(onClick = onProfileButtonClicked) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = stringResource(id = R.string.profile),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
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

    val currentScreen = backStackEntry?.destination?.route ?: AppScreen.Home.name //TODO cambiare in Login
    //val currentScreen = backStackEntry?.destination?.route ?: AppScreen.Login.name

    Log.d("NAV_TAG", "current screen : $currentScreen")

    Scaffold(
        topBar = {
            //Rimuove la TopAppBar dalla pagina di Login e Register
            if(currentScreen != AppScreen.Login.name && currentScreen != AppScreen.Register.name){

                Log.d("NAV_TAG", "adding TopAppBar in : $currentScreen")

                TopAppBarFunction(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    onSettingsButtonClicked = { navController.navigate(AppScreen.Settings.name) },
                    onProfileButtonClicked = { navController.navigate(AppScreen.Profile.name) }
                )
            }
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
    val utenteViewModel = hiltViewModel<UtenteViewModel>()
    val ristoranteViewModel = hiltViewModel<RistoranteViewModel>()
    val badgeRistoranteViewModel = hiltViewModel<BadgeRistoranteViewModel>()
    val badgeUtenteViewModel = hiltViewModel<BadgeUtenteViewModel>()
    val ciboViewModel = hiltViewModel<CiboViewModel>()
    val filtroConsegnaViewModel = hiltViewModel<FiltroConsegnaViewModel>()
    val ristoranteFiltroConsegnaViewModel = hiltViewModel<RistoranteFiltroConsegnaViewModel>()
    val ristoranteMenuRistoranteViewModel = hiltViewModel<RistoranteMenuRistoranteViewModel>()
    val ristoranteTipoRistoranteViewModel = hiltViewModel<RistoranteTipoRistoranteViewModel>()
    val tipoRistoranteViewModel = hiltViewModel<TipoRistoranteViewModel>()
    val utentePossiedeBadgeRistoranteViewModel = hiltViewModel<UtentePossiedeBadgeRistoranteViewModel>()
    val utentePossiedeBadgeUtenteViewModel = hiltViewModel<UtentePossiedeBadgeUtenteViewModel>()
    val utenteScansionaRistoranteViewModel = hiltViewModel<UtenteScansionaRistoranteViewModel>()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()

    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.name,
        //startDestination = AppScreen.Login.name,
        modifier = modifier.padding(innerPadding)
    ) {
        val NAV_TAG = "NAV_TAG "

        composable(route = AppScreen.Home.name) {
            HomeScreen(
                onMapButtonClicked = {
                    navController.navigate(AppScreen.Map.name)
                },
                onRestaurantClicked = {
                    navController.navigate(AppScreen.Restaurant.name)
                },
                ristoranteViewModel = ristoranteViewModel
            )
        }
        composable(route = AppScreen.Restaurant.name) {
            RistoranteScreen(
                ristoranteViewModel = ristoranteViewModel
            )
        }
        composable(route = AppScreen.Map.name) {

            Log.d(NAV_TAG + "FoodApp.kt" ,"navigating "+AppScreen.Map.name)

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

        var showErrorFlag : Boolean = false

        composable(route = AppScreen.Login.name) {

            Log.d(NAV_TAG + "FoodApp.kt" ,"navigating "+AppScreen.Login.name)

            LoginScreen(
                onLoginButtonClicked = {

                    showErrorFlag = showErrorFlag.not()

                    if(showErrorFlag)
                        //navController.popBackStack(AppScreen.Home.name, inclusive = true)

                        Log.d(NAV_TAG + "FoodApp.kt" ,"successful login ")
                                       },
                onRegisterButtonClicked = {

                    navController.navigate(AppScreen.Register.name)
                },
                showError = showErrorFlag,
                //placesViewModel = placesViewModel
            )
        }
        composable(route = AppScreen.Settings.name) {
            //val settingsViewModel = hiltViewModel<SettingsViewModel>()

            SettingsScreen(
                onLogoutButtonClicked = {
                    //logout
                    navController.navigate(AppScreen.Login.name)
                },
                settingsViewModel = settingsViewModel
            )
        }
        composable(route = AppScreen.Profile.name){

            ProfileScreen(

                //placesViewModel = placesViewModel
            )
        }

        composable(route = AppScreen.Register.name){
            Log.d(NAV_TAG + "FoodApp.kt" ,"navigating "+AppScreen.Register.name)

            RegisterScreen(
                onRegisterButtonClicked = {

                },
                onLoginButtonClicked = {

                }
            )
        }
    }
}