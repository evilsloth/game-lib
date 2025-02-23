package io.github.evilsloth.gamelib.common.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import io.github.evilsloth.gamelib.R
import io.github.evilsloth.gamelib.config.menu.NavigationMenuItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTopBar(navController: NavHostController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var title by remember { mutableIntStateOf(R.string.app_name) }

    LaunchedEffect(key1 = navBackStackEntry) {
        launch {
            val currentPath = navBackStackEntry?.destination?.route ?: ""
            val menuItem = NavigationMenuItem.entries.find { currentPath.startsWith(it.path) }
            title = menuItem?.title ?: R.string.app_name
        }
    }

    TopAppBar(
        title = {
            Text(stringResource(title))
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
            }
        }
    )
}