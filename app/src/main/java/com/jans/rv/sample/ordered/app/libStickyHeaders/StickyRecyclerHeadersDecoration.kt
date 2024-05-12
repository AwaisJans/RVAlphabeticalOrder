package com.jans.rv.sample.ordered.app.libStickyHeaders

import android.graphics.Canvas
import android.graphics.Rect
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.jans.rv.sample.ordered.app.libStickyHeaders.caching.HeaderProvider
import com.jans.rv.sample.ordered.app.libStickyHeaders.caching.HeaderViewCache
import com.jans.rv.sample.ordered.app.libStickyHeaders.calculation.DimensionCalculator
import com.jans.rv.sample.ordered.app.libStickyHeaders.rendering.HeaderRenderer
import com.jans.rv.sample.ordered.app.libStickyHeaders.util.LinearLayoutOrientationProvider
import com.jans.rv.sample.ordered.app.libStickyHeaders.util.OrientationProvider

class StickyRecyclerHeadersDecoration private constructor(
    private val mAdapter: StickyRecyclerHeadersAdapter<*>,
    private val mRenderer: HeaderRenderer,
    private val mOrientationProvider: OrientationProvider,
    private val mDimensionCalculator: DimensionCalculator,
    private val mHeaderProvider: HeaderProvider,
    private val mHeaderPositionCalculator: HeaderPositionCalculator,
    private val mVisibilityAdapter: ItemVisibilityAdapter?
) : ItemDecoration() {
    private val mHeaderRects = SparseArray<Rect>()

    private val mTempRect = Rect()

    // TODO: Consider passing in orientation to simplify orientation accounting within calculation
    constructor(adapter: StickyRecyclerHeadersAdapter<*>) : this(
        adapter,
        LinearLayoutOrientationProvider(),
        DimensionCalculator(),
        null
    )

    constructor(
        adapter: StickyRecyclerHeadersAdapter<*>,
        visibilityAdapter: ItemVisibilityAdapter?
    ) : this(adapter, LinearLayoutOrientationProvider(), DimensionCalculator(), visibilityAdapter)

    private constructor(
        adapter: StickyRecyclerHeadersAdapter<*>, orientationProvider: OrientationProvider,
        dimensionCalculator: DimensionCalculator, visibilityAdapter: ItemVisibilityAdapter?
    ) : this(
        adapter, orientationProvider, dimensionCalculator, HeaderRenderer(orientationProvider),
        HeaderViewCache(adapter, orientationProvider), visibilityAdapter
    )

    private constructor(
        adapter: StickyRecyclerHeadersAdapter<*>,
        orientationProvider: OrientationProvider,
        dimensionCalculator: DimensionCalculator,
        headerRenderer: HeaderRenderer,
        headerProvider: HeaderProvider,
        visibilityAdapter: ItemVisibilityAdapter?
    ) : this(
        adapter, headerRenderer, orientationProvider, dimensionCalculator, headerProvider,
        HeaderPositionCalculator(
            adapter, headerProvider, orientationProvider,
            dimensionCalculator
        ), visibilityAdapter
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }
        if (mHeaderPositionCalculator.hasNewHeader(
                itemPosition,
                mOrientationProvider.isReverseLayout(parent)
            )
        ) {
            val header = getHeaderView(parent, itemPosition)
            setItemOffsetsForHeader(outRect, header, mOrientationProvider.getOrientation(parent))
        }
    }


    private fun setItemOffsetsForHeader(itemOffsets: Rect, header: View?, orientation: Int) {
        mDimensionCalculator.initMargins(mTempRect, header!!)
        if (orientation == LinearLayoutManager.VERTICAL) {
            itemOffsets.top = header.height + mTempRect.top + mTempRect.bottom
        } else {
            itemOffsets.left = header.width + mTempRect.left + mTempRect.right
        }
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)

        val childCount = parent.childCount
        if (childCount <= 0 || mAdapter.itemCount <= 0) {
            return
        }

        for (i in 0 until childCount) {
            val itemView = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(itemView)
            if (position == RecyclerView.NO_POSITION) {
                continue
            }

            val hasStickyHeader = mHeaderPositionCalculator.hasStickyHeader(
                itemView,
                mOrientationProvider.getOrientation(parent),
                position
            )
            if (hasStickyHeader || mHeaderPositionCalculator.hasNewHeader(
                    position,
                    mOrientationProvider.isReverseLayout(parent)
                )
            ) {
                val header = mHeaderProvider.getHeader(parent, position)
                //re-use existing Rect, if any.
                var headerOffset = mHeaderRects[position]
                if (headerOffset == null) {
                    headerOffset = Rect()
                    mHeaderRects.put(position, headerOffset)
                }
                mHeaderPositionCalculator.initHeaderBounds(
                    headerOffset,
                    parent,
                    header!!,
                    itemView,
                    hasStickyHeader
                )
                mRenderer.drawHeader(parent, canvas, header, headerOffset)
            }
        }
    }


    fun findHeaderPositionUnder(x: Int, y: Int): Int {
        for (i in 0 until mHeaderRects.size()) {
            val rect = mHeaderRects[mHeaderRects.keyAt(i)]
            if (rect.contains(x, y)) {
                val position = mHeaderRects.keyAt(i)
                if (mVisibilityAdapter == null || mVisibilityAdapter.isPositionVisible(position)) {
                    return position
                }
            }
        }
        return -1
    }


    fun getHeaderView(parent: RecyclerView?, position: Int): View? {
        return mHeaderProvider.getHeader(parent, position)
    }

    /**
     * Invalidates cached headers.  This does not invalidate the recyclerview, you should do that manually after
     * calling this method.
     */
    fun invalidateHeaders() {
        mHeaderProvider.invalidate()
        mHeaderRects.clear()
    }
}
