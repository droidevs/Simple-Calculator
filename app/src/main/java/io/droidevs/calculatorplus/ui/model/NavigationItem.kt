package io.droidevs.calculatorplus.ui.model

import androidx.compose.ui.graphics.vector.ImageVector


data class NavigationItem(
    val id: Int,
    val title : String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector,
    var isSelected : Boolean = false
)