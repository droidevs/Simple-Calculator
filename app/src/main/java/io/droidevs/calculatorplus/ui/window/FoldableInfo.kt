package io.droidevs.calculatorplus.ui.window


import android.graphics.Rect
import androidx.window.layout.FoldingFeature


suspend fun getFoldableInfo(foldingFeature: FoldingFeature) : FoldableInfo {
    // Create a FoldableInfo object if a FoldingFeature is present.
    return foldingFeature.let {
        FoldableInfo(
            hingeType = foldingFeature.occlusionType,
            orientation = foldingFeature.orientation,
            openedState = foldingFeature.state,
            showSeparateScreens = foldingFeature.isSeparating,
            bounds = foldingFeature.bounds
        )
    }
}

class FoldableInfo(
    val hingeType: FoldingFeature.OcclusionType = FoldingFeature.OcclusionType.NONE,
    val orientation: FoldingFeature.Orientation = FoldingFeature.Orientation.HORIZONTAL,
    val openedState : FoldingFeature.State = FoldingFeature.State.FLAT,
    val showSeparateScreens: Boolean = false,
    val bounds: Rect = Rect()
){

    /**
     * Determines if the foldable device is in a tabletop posture.
     * also called "laptop mode" or "flex mode"
     * @return True if device is half-opened and horizontally oriented; false otherwise.
     */
    fun isTableTopPosture() : Boolean{
        return this.openedState == FoldingFeature.State.HALF_OPENED &&
                this.orientation == FoldingFeature.Orientation.HORIZONTAL
    }


    /**
     * Determines if the foldable device is in a book posture.
     * like a book
     * @return True if the device is half-opened and vertically oriented; false otherwise.
     */
    fun isBookPosture(): Boolean {
        return this.openedState == FoldingFeature.State.HALF_OPENED &&
                this.orientation == FoldingFeature.Orientation.VERTICAL
    }
}