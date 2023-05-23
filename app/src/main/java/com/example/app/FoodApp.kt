package com.example.app

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.app.data.FoodAppDB
import com.example.app.ui.*
import com.example.app.ui.compose.LoginScreen
import com.example.app.viewModel.*
import dagger.hilt.android.HiltAndroidApp

sealed class AppScreen(val name: String){
    object Home : AppScreen("Home")
    object Settings : AppScreen("Settings")
    object Login : AppScreen("Login")
    object RestaurantMain : AppScreen("RestaurantMain")
    object RestaurantMenu : AppScreen("RestaurantMenu")
    object RestaurantScoreboard : AppScreen("RestaurantScoreboard")
    object Map : AppScreen("Map")
    object Profile : AppScreen("Profile")
    object Register : AppScreen("Register")
    object Filter : AppScreen("Filter")
}
const val ROOT_ROUTE = "root"
const val AUTHENTICATION_ROUTE = "authentication"
const val RESTAURANT_ROUTE = "restaurant"
val RESTAURANT_SCREENS = listOf<String>(
    AppScreen.RestaurantMain.name,
    AppScreen.RestaurantMenu.name,
    AppScreen.RestaurantScoreboard.name
)

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
                color = MaterialTheme.colorScheme.onSecondary
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
                        tint = MaterialTheme.colorScheme.onSecondary
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
                            tint = MaterialTheme.colorScheme.onSecondary
                        )}
                }
                else {
                    IconButton(onClick = onProfileButtonClicked) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = stringResource(id = R.string.profile),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    )
}

@Composable
fun BottomAppBarFunc(
    currentScreen: String,
    modifier: Modifier = Modifier,
    onMainClicked: () -> Unit,
    onMenuClicked: () -> Unit,
    onScoreboardClicked: () -> Unit
) {
    BottomAppBar(
        modifier = modifier
            .height(55.dp),
        contentColor = MaterialTheme.colorScheme.primaryContainer,
        content = {
            IconButton(
                onClick = onMainClicked,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Info",
                    color = if(currentScreen == AppScreen.RestaurantMain.name) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            IconButton(
                onClick = onMenuClicked,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Menu",
                    color = if(currentScreen == AppScreen.RestaurantMenu.name) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            IconButton(
                onClick = onScoreboardClicked,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Classifica",
                    color = if(currentScreen == AppScreen.RestaurantScoreboard.name) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
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

                TopAppBarFunction(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { if(!RESTAURANT_SCREENS.contains(currentScreen)) navController.navigateUp() else navController.navigate(ROOT_ROUTE) },
                    onSettingsButtonClicked = { navController.navigate(AppScreen.Settings.name) },
                    onProfileButtonClicked = { navController.navigate(AppScreen.Profile.name) }
                )
            }
        },
        bottomBar = {
            if(RESTAURANT_SCREENS.contains(currentScreen)) {
                BottomAppBarFunc(
                    currentScreen = currentScreen,
                    onMainClicked = { navController.navigate(AppScreen.RestaurantMain.name) },
                    onMenuClicked = { navController.navigate(AppScreen.RestaurantMenu.name) },
                    onScoreboardClicked = { navController.navigate(AppScreen.RestaurantScoreboard.name) }
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
        route = ROOT_ROUTE,
        modifier = modifier.padding(innerPadding)
    ) {
        val NAV_TAG = "NAV_TAG "

        composable(route = AppScreen.Home.name) {
            HomeScreen(
                onMapButtonClicked = {
                    navController.navigate(AppScreen.Map.name)
                },
                onRestaurantClicked = {
                    navController.navigate(RESTAURANT_ROUTE)
                },
                onFiltersClicked = {
                    navController.navigate(AppScreen.Filter.name)
                },
                ristoranteViewModel = ristoranteViewModel
            )
        }

        navigation(
            startDestination = AppScreen.RestaurantMain.name,
            route = RESTAURANT_ROUTE
        ) {
            composable(route = AppScreen.RestaurantMain.name) {
                RistoranteMainScreen(
                    ristoranteViewModel = ristoranteViewModel
                )
            }
            composable(route = AppScreen.RestaurantMenu.name) {
                RistoranteMenuScreen(
                    ristoranteViewModel = ristoranteViewModel
                )
            }
            composable(route = AppScreen.RestaurantScoreboard.name) {
                RistoranteScoreboardScreen(
                    ristoranteViewModel = ristoranteViewModel
                )
            }
        }

        composable(route = AppScreen.Filter.name) {
            FiltersScreen(
                onConfirmClicked = {
                    navController.navigate(AppScreen.Home.name)
                },
                ristoranteViewModel = ristoranteViewModel
            )
        }
        composable(route = AppScreen.Map.name) {

            Log.d(NAV_TAG + "FoodApp.kt" ,"navigating "+AppScreen.Map.name)

            MapScreen(
                //MapScreen
                onRestaurantTickClicked = {
                    //carica il ristorante
                    navController.navigate(RESTAURANT_ROUTE)
                   //navController.popBackStack(AppScreen.Restaurant.name, inclusive = false)
                },
                //placesViewModel = placesViewModel
            )
        }

        composable(route = AppScreen.Login.name) {

            Log.d(NAV_TAG + "FoodApp.kt" ,"navigating "+AppScreen.Login.name)

            LoginScreen(
                onLoginButtonClicked = {
                    //navController.popBackStack(AppScreen.Home.name, inclusive = true)
                    navController.navigate(AppScreen.Home.name)
                                       },
                onRegisterButtonClicked = {
                    navController.navigate(AppScreen.Register.name)
                },
                utenteViewModel = utenteViewModel
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
                    navController.navigate(AppScreen.Login.name)
                }
            )
        }
    }
}