package io.droidevs.calculatorplus.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onNavigateAbout: () -> Unit,
    onNavigateHelp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (hapticsEnabled, setHapticsEnabled) = remember { mutableStateOf(true) }
    val (thousandsSeparator, setThousandsSeparator) = remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Preferences",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingsToggleRow(
            title = "Haptic feedback",
            description = "Vibrate on key presses",
            checked = hapticsEnabled,
            onCheckedChange = setHapticsEnabled
        )
        SettingsToggleRow(
            title = "Thousands separators",
            description = "Group large numbers automatically",
            checked = thousandsSeparator,
            onCheckedChange = setThousandsSeparator
        )

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Support",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        ListItem(
            headlineContent = { Text("About") },
            supportingContent = { Text("App version and credits") },
            modifier = Modifier.clickable { onNavigateAbout() }
        )
        ListItem(
            headlineContent = { Text("Help") },
            supportingContent = { Text("Tips and shortcuts") },
            modifier = Modifier.clickable { onNavigateHelp() }
        )
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(description) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    )
}
