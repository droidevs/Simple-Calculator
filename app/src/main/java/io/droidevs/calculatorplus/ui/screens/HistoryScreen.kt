package io.droidevs.calculatorplus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.droidevs.calculatorplus.ui.component.HistoryItem
import io.droidevs.calculatorplus.ui.model.HistoryUi


@Composable
fun HistoryScreen(
    historyItems: List<HistoryUi>,
    onItemClick: (HistoryUi) -> Unit,
    onItemDelete: (HistoryUi) -> Unit,
    modifier: Modifier = Modifier
) {
    if (historyItems.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No history yet.")
        }
    } else {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 300.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(historyItems.size) { index ->
                val item = historyItems[index]
                HistoryItem(
                    item = item,
                    onClick = { onItemClick(item) },
                    onDeleteConfirmed = { onItemDelete(item) }
                )
            }
        }
    }
}
