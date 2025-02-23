package io.github.evilsloth.gamelib.library.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.evilsloth.gamelib.R
import io.github.evilsloth.gamelib.library.model.LibraryItem

@Composable
fun GamesList(
    games: List<LibraryItem>,
    onClick: (item: LibraryItem) -> Unit,
    onLongClick: (LibraryItem) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(games.size) { index ->
            GameListItem(
                game = games[index],
                onClick = onClick,
                onLongClick = onLongClick
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GameListItem(
    game: LibraryItem,
    onClick: (LibraryItem) -> Unit,
    onLongClick: (LibraryItem) -> Unit
) {
    ElevatedCard(
        Modifier.combinedClickable(
            onClick = { onClick(game) },
            onLongClick = { onLongClick(game) },
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(game.thumbnailUrl ?: R.drawable.no_photo)
                    .crossfade(100)
                    .placeholder(R.drawable.placeholder)
                    .build(),
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 48.dp, height = 64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(
                text = game.name,
                Modifier.padding(horizontal = 4.dp, vertical = 2.dp).weight(1.0f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Image(
                painterResource(
                    id = when (game.platform) {
                        LibraryItem.Platform.AMAZON -> R.drawable.icon_amazon
                        LibraryItem.Platform.EGS -> R.drawable.icon_epic
                        LibraryItem.Platform.GOG -> R.drawable.icon_gog
                        LibraryItem.Platform.STEAM -> R.drawable.icon_steam
                    }
                ),
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}