package io.github.evilsloth.gamelib.library

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import io.github.evilsloth.gamelib.R
import io.github.evilsloth.gamelib.common.ui.NavigationDrawer
import io.github.evilsloth.gamelib.library.model.LibraryItem
import io.github.evilsloth.gamelib.library.ui.EmptyResults
import io.github.evilsloth.gamelib.library.ui.EmptyResultsFiltered
import io.github.evilsloth.gamelib.library.ui.GamesGrid
import io.github.evilsloth.gamelib.library.ui.GamesList
import io.github.evilsloth.gamelib.library.ui.SearchFilters
import io.github.evilsloth.gamelib.library.ui.SortingDialog
import kotlinx.coroutines.launch

private const val TAG = "LibraryPage"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LibraryPage(
    navController: NavHostController,
    drawerState: DrawerState,
    libraryViewModel: LibraryViewModel = hiltViewModel()
) {
    val searching by libraryViewModel.filterActive.collectAsState()
    val searchOpened by libraryViewModel.searchOpened.collectAsState()
    val searchText by libraryViewModel.searchValue.collectAsState()
    val refreshing by libraryViewModel.refreshing.collectAsState()
    val gamesState by libraryViewModel.games.collectAsState()
    val gridView by libraryViewModel.gridView.collectAsState()

    var showSortingOptions by remember { mutableStateOf(false) }
    val sortingBy by libraryViewModel.sortingBy.collectAsState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val games = gamesState

    NavigationDrawer(navController, drawerState) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        if (searchOpened) {
                            Row {
                                SearchFilters(
                                    searchText = searchText,
                                    onClearSearch = { libraryViewModel.clearSearch() },
                                    onFilterChange = { libraryViewModel.search(it) }
                                )
                            }
                        } else {
                            val gamesCount = games?.size ?: 0
                            Text(stringResource(R.string.menu_library) + " ($gamesCount)")
                        }
                    },
                    actions = {
                        if (!searchOpened) {
                            IconButton(onClick = { libraryViewModel.toggleView() }) {
                                Icon(Icons.AutoMirrored.Default.List, contentDescription = null)
                            }
                            IconButton(onClick = { showSortingOptions = true }) {
                                Icon(Icons.Default.SortByAlpha, contentDescription = null)
                            }
                            IconButton(
                                onClick = { libraryViewModel.openSearch() },
                                enabled = !refreshing
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )
                            }
                        }
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
            },
        ) { innerPadding ->
            val imeIsShown = WindowInsets.isImeVisible
            val bottomPadding = if (imeIsShown) 0.dp else innerPadding.calculateBottomPadding()

            Box(
                modifier = Modifier
                    .imePadding()
                    .padding(
                        bottom = bottomPadding,
                        top = innerPadding.calculateTopPadding(),
                        start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                        end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                if (games == null) {
                    // wait for db read...
                } else if (games.isEmpty() && !refreshing && !searching) {
                    EmptyResults(onRefresh = { libraryViewModel.loadLibraryGames() })
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (searching && games.isEmpty()) {
                            EmptyResultsFiltered(onClearFilters = { libraryViewModel.clearSearch() })
                        }

                        PullToRefreshBox(
                            isRefreshing = refreshing,
                            onRefresh = { libraryViewModel.loadLibraryGames() }
                        ) {
                            if (gridView) {
                                GamesGrid(
                                    games = games,
                                    onClick = { onItemClick(it, context) },
                                    onLongClick = { onItemLongClick(it, context) }
                                )
                            } else {
                                GamesList(
                                    games = games,
                                    onClick = { onItemClick(it, context) },
                                    onLongClick = { onItemLongClick(it, context) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSortingOptions) {
        SortingDialog(
            selectedOption = sortingBy,
            onSelectOption = { option ->
                libraryViewModel.sortBy(option)
                showSortingOptions = false
            },
            onClose = { showSortingOptions = false }
        )
    }
}

fun onItemClick(libraryItem: LibraryItem, context: Context) {
    if (libraryItem.url != null) {
        startActivity(context, Intent(Intent.ACTION_VIEW, libraryItem.url.toUri()), null)
    }
}

fun onItemLongClick(libraryItem: LibraryItem, context: Context) {
    Log.d(TAG, "External id: " + libraryItem.externalId)
    val clipboardManager: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText("id", libraryItem.externalId))
    Toast.makeText(context, "External id copied to clipboard!", Toast.LENGTH_SHORT).show()
}