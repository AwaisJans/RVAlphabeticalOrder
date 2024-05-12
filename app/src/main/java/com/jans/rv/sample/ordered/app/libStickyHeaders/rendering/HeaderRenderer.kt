package com.jans.rv.sample.ordered.app.libStickyHeaders.rendering

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.jans.rv.sample.ordered.app.libStickyHeaders.calculation.DimensionCalculator
import com.jans.rv.sample.ordered.app.libStickyHeaders.util.OrientationProvider

class HeaderRenderer private constructor(
    private val mOrientationProvider: OrientationProvider,
    private val mDimensionCalculator: DimensionCalculator
) {
    private val mTempRect = Rect()

    constructor(orientationProvider: OrientationProvider) : this(
        orientationProvider,
        DimensionCalculator()
    )


    fun drawHeader(recyclerView: RecyclerView, canvas: Canvas, header: View, offset: Rect) {
        canvas.save()

        if (recyclerView.layoutManager!!.clipToPadding) {
            initClipRectForHeader(mTempRect, recyclerView, header)
            canvas.clipRect(mTempRect)
        }

        canvas.translate(offset.left.toFloat(), offset.top.toFloat())

        header.draw(canvas)
        canvas.restore()
    }


    private fun initClipRectForHeader(clipRect: Rect, recyclerView: RecyclerView, header: View) {
        mDimensionCalculator.initMargins(clipRect, header)
        if (mOrientationProvider.getOrientation(recyclerView) == LinearLayout.VERTICAL) {
            clipRect[recyclerView.paddingLeft, recyclerView.paddingTop, recyclerView.width - recyclerView.paddingRight - clipRect.right] =
                recyclerView.height - recyclerView.paddingBottom
        } else {
            clipRect[recyclerView.paddingLeft, recyclerView.paddingTop, recyclerView.width - recyclerView.paddingRight] =
                recyclerView.height - recyclerView.paddingBottom - clipRect.bottom
        }
    }
}
