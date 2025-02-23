package io.github.evilsloth.gamelib.library.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.evilsloth.gamelib.R

@Composable
fun EmptyResultsFiltered(
    onClearFilters: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.height(64.dp))
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            Modifier.size(96.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.library_search_empty), Modifier.padding(horizontal = 32.dp), textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Button(onClick = { onClearFilters() }) {
            Text(stringResource(R.string.clear_filter))
        }
    }
}