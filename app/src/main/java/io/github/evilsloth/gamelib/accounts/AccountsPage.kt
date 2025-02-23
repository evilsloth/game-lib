package io.github.evilsloth.gamelib.accounts

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import io.github.evilsloth.gamelib.R
import io.github.evilsloth.gamelib.accounts.amazon.AmazonAuthViewModel
import io.github.evilsloth.gamelib.accounts.egs.EgsAuthViewModel
import io.github.evilsloth.gamelib.accounts.gog.GogAuthViewModel
import io.github.evilsloth.gamelib.accounts.steam.SteamAuthViewModel
import io.github.evilsloth.gamelib.common.ui.TopPage
import io.github.evilsloth.gamelib.config.ui.theme.GameLibTheme

@Composable
fun AccountsPage(
    navController: NavHostController,
    drawerState: DrawerState,
    gogAuthViewModel: GogAuthViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    egsAuthViewModel: EgsAuthViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    amazonAuthViewModel: AmazonAuthViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    steamAuthViewModel: SteamAuthViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val authenticatedInGog by gogAuthViewModel.authenticated.collectAsState()
    val authenticatedInEgs by egsAuthViewModel.authenticated.collectAsState()
    val authenticatedInAmazon by amazonAuthViewModel.authenticated.collectAsState()
    val authenticatedInSteam by steamAuthViewModel.authenticated.collectAsState()

    LaunchedEffect(Unit) {
        gogAuthViewModel.refreshStatus()
        egsAuthViewModel.refreshStatus()
        amazonAuthViewModel.refreshStatus()
        steamAuthViewModel.refreshStatus()
    }

    TopPage(navController, drawerState) {
        Column {
            StoreLoginRow(
                name = "GOG",
                icon = R.drawable.icon_gog,
                authenticated = authenticatedInGog,
                onLogin = { navController.navigate("accounts/gog") },
                onLogout = { gogAuthViewModel.logOut() }
            )

            StoreLoginRow(
                name = "Epic Games Store",
                icon = R.drawable.icon_epic,
                authenticated = authenticatedInEgs,
                onLogin = { navController.navigate("accounts/egs") },
                onLogout = { egsAuthViewModel.logOut() }
            )

            StoreLoginRow(
                name = "Amazon Prime Gaming",
                icon = R.drawable.icon_amazon,
                authenticated = authenticatedInAmazon,
                onLogin = { navController.navigate("accounts/amazon") },
                onLogout = { amazonAuthViewModel.logOut() }
            )

            StoreLoginRow(
                name = "Steam",
                icon = R.drawable.icon_steam,
                authenticated = authenticatedInSteam,
                onLogin = { navController.navigate("accounts/steam") },
                onLogout = { steamAuthViewModel.logOut() }
            )
        }
    }
}

@Composable
private fun StoreLoginRow(
    name: String,
    icon: Int,
    authenticated: Boolean,
    onLogin: () -> Unit,
    onLogout: () -> Unit
) {
    Row(
        Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(name)
        }
        if (authenticated) {
            Button(onClick = { onLogout() }) {
                Text(stringResource(R.string.log_out))
            }
        } else {
            Button(onClick = { onLogin() }) {
                Text(stringResource(R.string.log_in))
            }
        }
    }

    HorizontalDivider()
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun AccountsPreview() {
    GameLibTheme {
        Scaffold { contentPadding ->
            Column(Modifier.padding(contentPadding)) {
                StoreLoginRow(
                    name = "GOG",
                    icon = R.drawable.icon_gog,
                    authenticated = false,
                    onLogin = {},
                    onLogout = {}
                )
                StoreLoginRow(
                    name = "Epic Games Store",
                    icon = R.drawable.icon_epic,
                    authenticated = true,
                    onLogin = {},
                    onLogout = {}
                )
            }
        }
    }
}