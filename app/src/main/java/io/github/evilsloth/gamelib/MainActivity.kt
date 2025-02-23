package io.github.evilsloth.gamelib

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.evilsloth.gamelib.accounts.AccountsPage
import io.github.evilsloth.gamelib.accounts.amazon.AmazonAuthPage
import io.github.evilsloth.gamelib.accounts.egs.EgsAuthPage
import io.github.evilsloth.gamelib.accounts.gog.GogAuthPage
import io.github.evilsloth.gamelib.accounts.steam.SteamAuthPage
import io.github.evilsloth.gamelib.config.ui.theme.GameLibTheme
import io.github.evilsloth.gamelib.common.ui.NavigationDrawer
import io.github.evilsloth.gamelib.common.ui.MenuTopBar
import io.github.evilsloth.gamelib.common.ui.SubPage
import io.github.evilsloth.gamelib.common.ui.TopPage
import io.github.evilsloth.gamelib.library.LibraryPage

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameLibTheme {
                Navigation()
            }
        }
    }
}

@Composable
private fun Navigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    NavHost(navController = navController, startDestination = "library") {
        composable("accounts") { AccountsPage(navController, drawerState) }
        composable("accounts/gog") { GogAuthPage(navController) }
        composable("accounts/egs") { EgsAuthPage(navController) }
        composable("accounts/amazon") { AmazonAuthPage(navController) }
        composable("accounts/steam") { SteamAuthPage(navController) }
        composable("library") { LibraryPage(navController, drawerState) }
    }
}