package io.droidevs.calculatorplus.ui.window

import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.layout.FoldingFeature

enum class LayoutMode {
    PHONE_LANDSCAPE,
    PHONE_SPLIT_SQUARE,
    PHONE_PORTRAIT,
    NARROW_TABLET,
    DOUBLE_MEDIUM,
    DOUBLE_BIG,
    FOLDED_SPLIT_BOOK,
    FOLDED_SPLIT_TABLETOP;

    /**
     * Show the nav drawer when a bottom nav would be too
     * stretched and a side rail would cut into a double layout
     * too much. So this shows for a foldable with a double layout,
     * a very small window/phone size - split screen, or a medium
     * sized width screen.
     */
    fun showNavDrawer(): Boolean =
        ((this == DOUBLE_MEDIUM || this == PHONE_SPLIT_SQUARE ||
                this == FOLDED_SPLIT_BOOK))

    /**
     * Show the nav rail mostly when we're in landscape mode, and
     * there's plenty of width to take up. Narrow tablet is only one
     * screen, so this is the exception.
     */
    fun showNavRail(): Boolean =
        (this == PHONE_LANDSCAPE || this == DOUBLE_BIG ||
                this == FOLDED_SPLIT_TABLETOP || this == NARROW_TABLET)

    /**
     * Bottom nav is perfect for a typical phone size, in portrait mode.
     */
    fun showBottomNav(): Boolean =
        (this == PHONE_PORTRAIT)

    /**
     * Only these layouts have a double | split screen.
     */
    fun isSplitScreen(): Boolean =
        (this == DOUBLE_MEDIUM || this == DOUBLE_BIG)

    fun isSplitFoldable(): Boolean =
        (this == FOLDED_SPLIT_TABLETOP || this == FOLDED_SPLIT_BOOK)

}


fun getWindowLayoutMode(
    windowSize: WindowSize,
    foldableInfo: FoldableInfo?) : LayoutMode {
    Log.d("debug", "windowWH: ${windowSize.widthDp};${windowSize.heightDp}")

    return if ((foldableInfo != null) && foldableInfo.showSeparateScreens) {
        getFoldableLayout(foldableInfo)
    } else {
        val windowClass = windowSize.getClass()
        if (windowClass.heightClass == SizeClass.Compact)
            getLandscapeLayout(windowClass.widthClass)
        else
            getPortraitLayout(widthClass = windowClass.widthClass,windowSize.widthDp)
    }
}


fun getFoldableLayout(foldableInfo: FoldableInfo) : LayoutMode {
    return if (foldableInfo.orientation == FoldingFeature.Orientation.VERTICAL)
        LayoutMode.FOLDED_SPLIT_BOOK
    else
        LayoutMode.FOLDED_SPLIT_TABLETOP

}

fun getLandscapeLayout(widthClass: SizeClass) : LayoutMode {
    if (widthClass == SizeClass.Compact){
        //this is if it's very small; phone size & split screen.
        return LayoutMode.PHONE_SPLIT_SQUARE
    }
    else
        return LayoutMode.PHONE_LANDSCAPE
}


fun getPortraitLayout(widthClass: SizeClass, width: Dp): LayoutMode {
    //At this point, i know it's not a landscape/rotated phone size.
    // so let's check the width
    return when(widthClass){
        SizeClass.Compact -> LayoutMode.PHONE_PORTRAIT
        SizeClass.Medium -> {
            // some tablets measure 600.93896; just ove 600;
            if (width <= 650.dp)
                LayoutMode.NARROW_TABLET
            else
                LayoutMode.DOUBLE_MEDIUM
        }
        else -> {
            // override the expanded threshold. 800 vs 1000+ is big diff.
            if (width < 950.dp)
                LayoutMode.DOUBLE_MEDIUM
            else
                LayoutMode.DOUBLE_BIG
        }
    }
}