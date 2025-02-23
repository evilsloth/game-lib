package io.github.evilsloth.gamelib.accounts.steam

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import io.github.evilsloth.gamelib.R
import io.github.evilsloth.gamelib.common.ui.SubPage
import io.github.evilsloth.gamelib.config.ui.theme.GameLibTheme

@Composable
fun SteamAuthPage(
    navController: NavHostController,
    steamAuthViewModel: SteamAuthViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    SubPage(title = stringResource(R.string.log_in_to_steam), navController = navController) {
        ApiDataForm(
            onSave = { apiKey, steamId ->
                steamAuthViewModel.saveLoginData(apiKey, steamId)
                navController.popBackStack()
            }
        )
    }
}

@Composable
private fun ApiDataForm(
    onSave: (String, String) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        var apiKey by remember { mutableStateOf("") }
        var steamId by remember { mutableStateOf("") }

        TextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text(stringResource(R.string.api_key)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        TextField(
            value = steamId,
            onValueChange = { steamId = it },
            label = { Text(stringResource(R.string.steam_id)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onSave(apiKey, steamId) },
            modifier = Modifier.fillMaxWidth(),
            enabled = (apiKey.isNotBlank() && steamId.isNotBlank())
        ) {
            Text(stringResource(R.string.save))
        }
        Spacer(Modifier.height(24.dp))
        Notice()
    }
}

@Composable
private fun Notice() {
    Text(text = buildAnnotatedString {
        val textStyle = SpanStyle(fontSize = 12.sp)
        val linkStyle = SpanStyle(
            color = Color(0xFF168EFF),
            fontSize = 12.sp,
            textDecoration = TextDecoration.Underline
        )

        withStyle(style = textStyle) {
            append(stringResource(R.string.steam_auth_notice_1))
            append("\n")
            append(stringResource(R.string.steam_auth_notice_2_1))
        }

        withLink(LinkAnnotation.Url(url = "https://steamcommunity.com/dev/apikey")) {
            withStyle(style = linkStyle) {
                append("https://steamcommunity.com/dev/apikey")
            }
        }

        withStyle(style = textStyle) {
            append(" ")
            append(stringResource(R.string.steam_auth_notice_2_2))
            append("\n\n")
            append(stringResource(R.string.steam_auth_notice_3))
            append("\n")
            append(stringResource(R.string.steam_auth_notice_4))
            append("\n")
            append(stringResource(R.string.steam_auth_notice_5))
            append("\n")
            append(stringResource(R.string.steam_auth_notice_6))
        }
    })
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun AccountsPreview() {
    GameLibTheme {
        Scaffold { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                ApiDataForm(onSave = { _, _ -> })
            }
        }
    }
}
