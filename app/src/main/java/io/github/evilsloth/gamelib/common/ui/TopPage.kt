package io.github.evilsloth.gamelib.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun TopPage(
    navController: NavHostController,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    NavigationDrawer(navController, drawerState) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { MenuTopBar(navController, drawerState) },
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                content()
            }
        }
    }
}