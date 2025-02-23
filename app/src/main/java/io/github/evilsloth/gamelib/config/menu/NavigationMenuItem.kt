package io.github.evilsloth.gamelib.config.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.evilsloth.gamelib.R

enum class NavigationMenuItem(
    val title: Int,
    val path: String,
    val icon: ImageVector
) {
    LIBRARY(R.string.menu_library, "library", Icons.AutoMirrored.Filled.List),
    ACCOUNTS(R.string.menu_accounts, "accounts", Icons.Filled.AccountBox)
}