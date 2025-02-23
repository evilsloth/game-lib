package io.github.evilsloth.gamelib.library.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.evilsloth.gamelib.R
import io.github.evilsloth.gamelib.common.utils.requestInitialFocus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilters(
    searchText: String,
    onClearSearch: () -> Unit,
    onFilterChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 8.dp),
        windowInsets = WindowInsets(top = 0.dp),
        inputField = {
            SearchBarDefaults.InputField(
                modifier = Modifier.requestInitialFocus(),
                onSearch = { focusManager.clearFocus() },
                expanded = false,
                onExpandedChange = { },
                placeholder = { Text(stringResource(R.string.search_game)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { onClearSearch() }) {
                        Icon(Icons.Default.Clear, contentDescription = null)
                    }
                },
                query = searchText,
                onQueryChange = { onFilterChange(it) }
            )
        },
        expanded = false,
        onExpandedChange = { },
    ) {}
}