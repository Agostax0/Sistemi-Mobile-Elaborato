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
    object Settings : AppScreen("Settings Screen")
    object Login : AppScreen("Login Screen")
    object Restaurant : AppScreen("Restaurant Screen")
    object Map : AppScreen("Map Screen")
    object Profile : AppScreen("Profile Screen")
    object Register : AppScreen("Register Screen")
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
                        tint = White
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
                            tint = White
                        )}
                }
                else {
                    IconButton(onClick = onProfileButtonClicked) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = stringResource(id = R.string.profile),
                            tint = White
                        )
                    }
                }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Orange
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

    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.name,
        //startDestination = AppScreen.Login.name,
        modifier = modifier.padding(innerPadding)
    ) {

        val NAV_TAG = "NAV_TAG "

        composable(route = AppScreen.Home.name) {

            Log.d(NAV_TAG + "FoodApp.kt" ,"entering "+AppScreen.Home.name)

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

            Log.d(NAV_TAG + "FoodApp.kt" ,"entering "+AppScreen.Map.name)

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

            Log.d(NAV_TAG + "FoodApp.kt" ,"entering "+AppScreen.Login.name)

            LoginScreen(
                onLoginButtonClicked = {
                    Log.d(NAV_TAG + "FoodApp.kt","onLoginButtonClicked")


                    showErrorFlag = showErrorFlag.not()

                    if(showErrorFlag)
                        navController.navigate(AppScreen.Home.name)
                    else
                        navController.navigate(AppScreen.Login.name)


                                       },
                onRegisterButtonClicked = {
                    Log.d(NAV_TAG + " " + AppScreen.Login.name ,"onRegisterButtonClicked")

                    navController.navigate(AppScreen.Register.name)
                },
                showError = showErrorFlag,
                //placesViewModel = placesViewModel
            )
        }
        composable(route = AppScreen.Settings.name) {
            //val settingsViewModel = hiltViewModel<SettingsViewModel>()

            Log.d(NAV_TAG + "FoodApp.kt" ,"entering "+AppScreen.Settings.name)

            SettingsScreen(
                onLogoutButtonClicked = {
                    //logout
                    navController.navigate(AppScreen.Login.name)
                }
                //settingsViewModel
            )
        }
        composable(route = AppScreen.Profile.name){

            Log.d(NAV_TAG + "FoodApp.kt" ,"entering "+AppScreen.Profile.name)

            ProfileScreen(

                //placesViewModel = placesViewModel
            )
        }

        composable(route = AppScreen.Register.name){
            Log.d(NAV_TAG + "FoodApp.kt" ,"entering "+AppScreen.Profile.name)

            RegisterScreen(
                onRegisterButtonClicked = {

                },
                onLoginButtonClicked = {

                }
            )
        }
    }
}