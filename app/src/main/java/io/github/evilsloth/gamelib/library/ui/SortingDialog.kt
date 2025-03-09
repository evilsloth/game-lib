package io.github.evilsloth.gamelib.library.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.evilsloth.gamelib.R
import io.github.evilsloth.gamelib.config.ui.theme.GameLibTheme
import io.github.evilsloth.gamelib.library.model.SortingOption

@Composable
fun SortingDialog(
    selectedOption: SortingOption,
    onSelectOption: (SortingOption) -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        title = {
            Text(stringResource(R.string.sort_by))
        },
        text = {
            val radioOptions = SortingOption.entries

            Column(Modifier.selectableGroup()) {
                radioOptions.forEach { sortingOption ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .selectable(
                                selected = sortingOption == selectedOption,
                                onClick = { onSelectOption(sortingOption) },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = sortingOption == selectedOption,
                            onClick = null
                        )
                        Text(
                            text = stringResource(sortingOption.title),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        onDismissRequest = { onClose() },
        confirmButton = {},
        dismissButton = {}
    )
}

@Preview(showBackground = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SortingDialogPreview() {
    GameLibTheme {
        Scaffold { contentPadding ->
            Box(Modifier.padding(contentPadding)) {
                SortingDialog(
                    selectedOption = SortingOption.NAME_ASC,
                    onSelectOption = {},
                    onClose = {}
                )
            }
        }
    }
}