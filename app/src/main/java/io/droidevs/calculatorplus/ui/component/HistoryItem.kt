package io.droidevs.calculatorplus.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.droidevs.calculatorplus.ui.model.HistoryUi
import io.droidevs.calculatorplus.ui.window.LayoutMode
import io.droidevs.calculatorplus.ui.window.LocalWindow

@Composable
fun HistoryItem(
    item: HistoryUi,
    onClick: () -> Unit = {},
    onDeleteConfirmed: () -> Unit = {},
    onFavored: () -> Unit = {}
) {
    val windowInfo = LocalWindow.current
    val layoutMode = windowInfo.layoutMode
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Entry") },
            text = { Text("Are you sure you want to delete this history item?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteConfirmed()
                    showDeleteDialog = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(layoutMode != LayoutMode.PHONE_PORTRAIT){
                IconButton(
                    onClick = { onFavored() }
                ) {
                    Icon(
                        imageVector =
                        if (item.isFavored)
                            Icons.Default.Favorite
                        else
                            Icons.Default.FavoriteBorder,
                        tint =
                        if (item.isFavored)
                            Color.Yellow
                        else
                            Color.Green,
                        contentDescription = "Favorite")
                }
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.expression,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "= ${item.result}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if(layoutMode != LayoutMode.PHONE_PORTRAIT){
                IconButton(
                    onClick = { showDeleteDialog = true }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }

            when(windowInfo.layoutMode){
                LayoutMode.PHONE_PORTRAIT -> {
                    Box(

                    ) {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options")
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Favorite") },
                                onClick = {
                                    showMenu = false
                                    onFavored()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    showMenu = false
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }

                else -> {}
            }

        }
    }
}

