package com.example.app

import android.app.Application
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.app.data.FoodAppDB
import com.example.app.ui.*
import com.example.app.ui.LoginScreen
import com.example.app.viewModel.*
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch

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
    object Loading : AppScreen("Loading")
}
const val ROOT_ROUTE = "root"
const val AUTHENTICATION_ROUTE = "authentication"
const val RESTAURANT_ROUTE = "restaurant"
val RESTAURANT_SCREENS = listOf(
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
    onTitleButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    onProfileButtonClicked: () -> Unit,
    ristoranteViewModel: RistoranteViewModel,
    utenteViewModel: UtenteViewModel,
    session: String
) {
    val context = LocalContext.current
    val utenti by utenteViewModel.utenti.collectAsState(initial = listOf())
    if(utenti.isNotEmpty() || utenteViewModel.utenteLoggato != null) {
        val utenteLoggato = if (utenteViewModel.utenteLoggato == null)
            utenti.find { it.username == session }!! else utenteViewModel.utenteLoggato!!
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = (if(RESTAURANT_SCREENS.contains(currentScreen)) ristoranteViewModel.ristoranteSelected?.nome else stringResource(id = R.string.app_name))!!,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .clickable(onClick = onTitleButtonClicked)
                )
            },
            modifier = modifier,
            navigationIcon = {
                //se si può navigare indietro (non home screen) allora appare la freccetta
                if (canNavigateBack && currentScreen != AppScreen.Home.name) {
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
                    IconButton(onClick = onSettingsButtonClicked) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = stringResource(id = R.string.settings),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )}
                }
                else if(currentScreen != AppScreen.Settings.name) {
                    IconButton(onClick = onProfileButtonClicked) {
                        AsyncImage(
                            model = ImageRequest
                                .Builder(context)
                                .data(utenteLoggato.icona)
                                .crossfade(true)
                                .build(),
                            contentDescription = "foto profilo",
                            modifier = Modifier.clip(shape = RoundedCornerShape(50.dp))
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        )
    }

}

@Composable
fun BottomAppBarFunc(
    currentScreen: String,
    navController: NavHostController
) {
    var text = ""
    var isSelected: Boolean
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background
    ) {
        RESTAURANT_SCREENS.forEach { item ->
            when (item) {
                AppScreen.RestaurantMain.name -> text = "Info"
                AppScreen.RestaurantMenu.name -> text = "Menu"
                AppScreen.RestaurantScoreboard.name -> text = "Classifica"
            }
            isSelected = currentScreen == item
            BottomNavigationItem(
                icon = {
                    Text(
                        text = text,
                        color = if(isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selected = isSelected,
                onClick = { navController.navigate(item) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp(
    session: String,
    navController: NavHostController = rememberNavController(),
    startLocationUpdates: () -> Unit,
    warningViewModel: WarningViewModel
) {
    val ristoranteViewModel = hiltViewModel<RistoranteViewModel>()
    val utenteViewModel = hiltViewModel<UtenteViewModel>()

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen

    val currentScreen = backStackEntry?.destination?.route ?: AppScreen.Loading.name

    Log.d("NAV_TAG", "current screen : $currentScreen")

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            //Rimuove la TopAppBar dalla pagina di Login e Register
            if(currentScreen != AppScreen.Login.name && currentScreen != AppScreen.Register.name && currentScreen != AppScreen.Loading.name) {
                TopAppBarFunction(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { if(!RESTAURANT_SCREENS.contains(currentScreen)) navController.navigateUp() else navController.navigate(AppScreen.Home.name) },
                    onTitleButtonClicked = { navController.navigate(AppScreen.Home.name) },
                    onSettingsButtonClicked = { navController.navigate(AppScreen.Settings.name) },
                    onProfileButtonClicked = { navController.navigate(AppScreen.Profile.name) },
                    ristoranteViewModel = ristoranteViewModel,
                    utenteViewModel = utenteViewModel,
                    session = session
                )
            }
        },
        bottomBar = {
            if(RESTAURANT_SCREENS.contains(currentScreen)) {
                BottomAppBarFunc(
                    currentScreen = currentScreen,
                    navController
                )
            }
        }
    ) { innerPadding ->
        NavigationGraph(navController, innerPadding, session= session, startLocationUpdates = startLocationUpdates)
        val context = LocalContext.current
        if (warningViewModel.showPermissionSnackBar.value) {
            PermissionSnackBarComposable(snackbarHostState, context, warningViewModel)
        }
        if (warningViewModel.showGPSAlertDialog.value) {
            GPSAlertDialogComposable(context, warningViewModel)
        }
        if (warningViewModel.showConnectivitySnackBar.value) {
            ConnectivitySnackBarComposable(
                snackbarHostState,
                context,
                warningViewModel
            )
        }
    }
}

@Composable
private fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    session: String,
    modifier: Modifier = Modifier,
    startLocationUpdates: () -> Unit
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
    val locationViewModel = hiltViewModel<LocationViewModel>()

    NavHost(
        navController = navController,
        startDestination = if(session == "default") AppScreen.Loading.name else if(session == "") AppScreen.Login.name else AppScreen.Home.name,
        route = ROOT_ROUTE,
        modifier = modifier.padding(innerPadding)
    ) {
        val NAV_TAG = "NAV_TAG"

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
                ristoranteViewModel = ristoranteViewModel,
                filtroConsegnaViewModel = filtroConsegnaViewModel,
                ristoranteFiltroConsegnaViewModel = ristoranteFiltroConsegnaViewModel,
                tipoRistoranteViewModel = tipoRistoranteViewModel,
                ristoranteTipoRistoranteViewModel = ristoranteTipoRistoranteViewModel,
                utenteScansionaRistoranteViewModel = utenteScansionaRistoranteViewModel,
                utenteViewModel = utenteViewModel,
                locationViewModel = locationViewModel,
                session = session
            )
        }

        navigation(
            startDestination = AppScreen.RestaurantMain.name,
            route = RESTAURANT_ROUTE
        ) {
            composable(route = AppScreen.RestaurantMain.name) {
                RistoranteMainScreen(
                    ristoranteViewModel = ristoranteViewModel,
                    ristoranteTipoRistoranteViewModel = ristoranteTipoRistoranteViewModel,
                    ristoranteFiltroConsegnaViewModel = ristoranteFiltroConsegnaViewModel,
                    utenteScansionaRistoranteViewModel = utenteScansionaRistoranteViewModel,
                    utentePossiedeBadgeRistoranteViewModel = utentePossiedeBadgeRistoranteViewModel,
                    utenteViewModel = utenteViewModel,
                    session = session
                )
            }
            composable(route = AppScreen.RestaurantMenu.name) {
                RistoranteMenuScreen(
                    ristoranteViewModel = ristoranteViewModel,
                    ristoranteMenuRistoranteViewModel = ristoranteMenuRistoranteViewModel
                )
            }
            composable(route = AppScreen.RestaurantScoreboard.name) {
                RistoranteScoreboardScreen(
                    ristoranteViewModel = ristoranteViewModel,
                    utentePossiedeBadgeRistoranteViewModel = utentePossiedeBadgeRistoranteViewModel,
                    utenteViewModel
                )
            }
        }

        composable(route = AppScreen.Filter.name) {
            FiltersScreen(filtroConsegnaViewModel = filtroConsegnaViewModel)
        }
        composable(route = AppScreen.Map.name) {

            Log.d(NAV_TAG + "FoodApp.kt" ,"navigating "+AppScreen.Map.name)

            MapScreen(startLocationUpdates,
                locationViewModel,
                ristoranteViewModel,
                onRestaurantClicked = {
                    navController.navigate(RESTAURANT_ROUTE)
                },
                tipoRistoranteViewModel = ristoranteTipoRistoranteViewModel,
                session = session,
                utenteViewModel = utenteViewModel,
                utenteScansionaRistoranteViewModel = utenteScansionaRistoranteViewModel
            )
        }

        composable(route = AppScreen.Login.name) {

            Log.d(NAV_TAG + "FoodApp.kt" ,"navigating "+AppScreen.Login.name)

            LoginScreen(
                onSuccessfulLogin = {
                    //navController.popBackStack(AppScreen.Home.name, inclusive = true)
                    navController.navigate(AppScreen.Home.name){
                        popUpTo(navController.graph.id){
                            inclusive = true
                        }
                    }
                    Log.d(NAV_TAG + "FoodApp.kt" ,"navigating to "+AppScreen.Home.name+ " after ${AppScreen.Login.name}")

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
                    navController.navigate(AppScreen.Login.name){
                        popUpTo(navController.graph.id){
                            inclusive = true
                        }
                    }
                },
                settingsViewModel = settingsViewModel,
                utenteViewModel = utenteViewModel
            )
        }
        composable(route = AppScreen.Profile.name){
            ProfileScreen(
                utenteViewModel = utenteViewModel,
                session = session,
                utenteScansionaRistoranteViewModel =  utenteScansionaRistoranteViewModel,
                utentePossiedeBadgeRistoranteViewModel =  utentePossiedeBadgeRistoranteViewModel,
                ristoranteViewModel = ristoranteViewModel,
                utentePossiedeBadgeUtenteViewModel = utentePossiedeBadgeUtenteViewModel,
                badgeUtenteViewModel = badgeUtenteViewModel,
                onFavoriteRestaurantsClicked = {}, //TODO
                onUserBadgesClicked = {}, //TODO
                onRestaurantBadgesClicked = {}, //TODO
            )
        }

        composable(route = AppScreen.Register.name){
            Log.d(NAV_TAG + "FoodApp.kt" ,"navigating "+AppScreen.Register.name)

            RegisterScreen(
                onSuccessfulRegister = {
                    navController.navigate(AppScreen.Home.name){
                        popUpTo(navController.graph.id){
                            inclusive = true
                        }
                    }
                },
                onLoginButtonClicked = {
                    navController.navigate(AppScreen.Login.name)
                },
                utenteViewModel = utenteViewModel
            )
        }

        composable(route = AppScreen.Loading.name){
            LoadingScreen(
                navigateToLogin = {
                    navController.navigate(AppScreen.Login.name){
                        popUpTo(navController.graph.id){
                            inclusive = true
                        }
                    }
                },
                navigateToHome = {
                    navController.navigate(AppScreen.Home.name){
                        popUpTo(navController.graph.id){
                            inclusive = true
                        }
                    }
                },
                startLocationUpdates,
                session
            )
        }
    }
}