package com.jans.rv.sample.ordered.app.libStickyHeaders.calculation

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup.MarginLayoutParams

class DimensionCalculator {
    fun initMargins(margins: Rect, view: View) {
        val layoutParams = view.layoutParams

        if (layoutParams is MarginLayoutParams) {
            initMarginRect(margins, layoutParams)
        } else {
            margins[0, 0, 0] = 0
        }
    }


    private fun initMarginRect(marginRect: Rect, marginLayoutParams: MarginLayoutParams) {
        marginRect[marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin] =
            marginLayoutParams.bottomMargin
    }
}
