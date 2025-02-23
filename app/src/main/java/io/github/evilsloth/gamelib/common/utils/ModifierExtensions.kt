package io.github.evilsloth.gamelib.common.utils

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

fun Modifier.requestInitialFocus(enabled: Boolean = true) = composed {
    if (enabled) {
        val requester = remember { FocusRequester() }
        LaunchedEffect(requester) {
            requester.requestFocus()
        }
        focusRequester(requester)
    } else {
        this
    }
}