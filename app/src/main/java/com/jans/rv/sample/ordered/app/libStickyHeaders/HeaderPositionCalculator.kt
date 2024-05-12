package com.jans.rv.sample.ordered.app.libStickyHeaders

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jans.rv.sample.ordered.app.libStickyHeaders.caching.HeaderProvider
import com.jans.rv.sample.ordered.app.libStickyHeaders.calculation.DimensionCalculator
import com.jans.rv.sample.ordered.app.libStickyHeaders.util.OrientationProvider
import kotlin.math.max

/**
 * Calculates the position and location of header views
 */
class HeaderPositionCalculator(
    private val mAdapter: StickyRecyclerHeadersAdapter<*>,
    private val mHeaderProvider: HeaderProvider,
    private val mOrientationProvider: OrientationProvider,
    private val mDimensionCalculator: DimensionCalculator
) {

    private val mTempRect1 = Rect()
    private val mTempRect2 = Rect()


    fun hasStickyHeader(itemView: View, orientation: Int, position: Int): Boolean {
        val offset: Int
        val margin: Int
        mDimensionCalculator.initMargins(mTempRect1, itemView)
        if (orientation == LinearLayout.VERTICAL) {
            offset = itemView.top
            margin = mTempRect1.top
        } else {
            offset = itemView.left
            margin = mTempRect1.left
        }

        return offset <= margin && mAdapter.getHeaderId(position) >= 0
    }

    fun hasNewHeader(position: Int, isReverseLayout: Boolean): Boolean {
        if (indexOutOfBounds(position)) {
            return false
        }

        val headerId = mAdapter.getHeaderId(position)

        if (headerId < 0) {
            return false
        }

        var nextItemHeaderId: Long = -1
        val nextItemPosition = position + (if (isReverseLayout) 1 else -1)
        if (!indexOutOfBounds(nextItemPosition)) {
            nextItemHeaderId = mAdapter.getHeaderId(nextItemPosition)
        }
        val firstItemPosition = if (isReverseLayout) mAdapter.itemCount - 1 else 0

        return position == firstItemPosition || headerId != nextItemHeaderId
    }

    private fun indexOutOfBounds(position: Int): Boolean {
        return position < 0 || position >= mAdapter.itemCount
    }

    fun initHeaderBounds(
        bounds: Rect,
        recyclerView: RecyclerView,
        header: View,
        firstView: View,
        firstHeader: Boolean
    ) {
        val orientation = mOrientationProvider.getOrientation(recyclerView)
        initDefaultHeaderOffset(bounds, recyclerView, header, firstView, orientation)

        if (firstHeader && isStickyHeaderBeingPushedOffscreen(recyclerView, header)) {
            val viewAfterNextHeader = getFirstViewUnobscuredByHeader(recyclerView, header)
            val firstViewUnderHeaderPosition = recyclerView.getChildAdapterPosition(
                viewAfterNextHeader!!
            )
            val secondHeader = mHeaderProvider.getHeader(recyclerView, firstViewUnderHeaderPosition)
            translateHeaderWithNextHeader(
                recyclerView, mOrientationProvider.getOrientation(recyclerView), bounds,
                header, viewAfterNextHeader, secondHeader
            )
        }
    }

    private fun initDefaultHeaderOffset(
        headerMargins: Rect,
        recyclerView: RecyclerView,
        header: View,
        firstView: View,
        orientation: Int
    ) {
        val translationX: Int
        val translationY: Int
        mDimensionCalculator.initMargins(mTempRect1, header)

        val layoutParams = firstView.layoutParams
        var leftMargin = 0
        var topMargin = 0
        if (layoutParams is MarginLayoutParams) {
            val marginLayoutParams = layoutParams
            leftMargin = marginLayoutParams.leftMargin
            topMargin = marginLayoutParams.topMargin
        }

        if (orientation == LinearLayoutManager.VERTICAL) {
            translationX = firstView.left - leftMargin + mTempRect1.left
            translationY = max(
                (firstView.top - topMargin - header.height - mTempRect1.bottom).toDouble(),
                (getListTop(recyclerView) + mTempRect1.top).toDouble()
            ).toInt()
        } else {
            translationY = firstView.top - topMargin + mTempRect1.top
            translationX = max(
                (firstView.left - leftMargin - header.width - mTempRect1.right).toDouble(),
                (getListLeft(recyclerView) + mTempRect1.left).toDouble()
            ).toInt()
        }

        headerMargins[translationX, translationY, translationX + header.width] =
            translationY + header.height
    }

    private fun isStickyHeaderBeingPushedOffscreen(
        recyclerView: RecyclerView,
        stickyHeader: View
    ): Boolean {
        val viewAfterHeader = getFirstViewUnobscuredByHeader(recyclerView, stickyHeader)
        val firstViewUnderHeaderPosition = recyclerView.getChildAdapterPosition(viewAfterHeader!!)
        if (firstViewUnderHeaderPosition == RecyclerView.NO_POSITION) {
            return false
        }

        val isReverseLayout = mOrientationProvider.isReverseLayout(recyclerView)
        if (firstViewUnderHeaderPosition > 0 && hasNewHeader(
                firstViewUnderHeaderPosition,
                isReverseLayout
            )
        ) {
            val nextHeader = mHeaderProvider.getHeader(recyclerView, firstViewUnderHeaderPosition)
            mDimensionCalculator.initMargins(mTempRect1, nextHeader!!)
            mDimensionCalculator.initMargins(mTempRect2, stickyHeader)

            if (mOrientationProvider.getOrientation(recyclerView) == LinearLayoutManager.VERTICAL) {
                val topOfNextHeader =
                    viewAfterHeader.top - mTempRect1.bottom - nextHeader.height - mTempRect1.top
                val bottomOfThisHeader =
                    recyclerView.paddingTop + stickyHeader.bottom + mTempRect2.top + mTempRect2.bottom
                if (topOfNextHeader < bottomOfThisHeader) {
                    return true
                }
            } else {
                val leftOfNextHeader =
                    viewAfterHeader.left - mTempRect1.right - nextHeader.width - mTempRect1.left
                val rightOfThisHeader =
                    recyclerView.paddingLeft + stickyHeader.right + mTempRect2.left + mTempRect2.right
                if (leftOfNextHeader < rightOfThisHeader) {
                    return true
                }
            }
        }

        return false
    }

    private fun translateHeaderWithNextHeader(
        recyclerView: RecyclerView, orientation: Int, translation: Rect,
        currentHeader: View, viewAfterNextHeader: View?, nextHeader: View?
    ) {
        mDimensionCalculator.initMargins(mTempRect1, nextHeader!!)
        mDimensionCalculator.initMargins(mTempRect2, currentHeader)
        if (orientation == LinearLayoutManager.VERTICAL) {
            val topOfStickyHeader = getListTop(recyclerView) + mTempRect2.top + mTempRect2.bottom
            val shiftFromNextHeader =
                viewAfterNextHeader!!.top - nextHeader.height - mTempRect1.bottom - mTempRect1.top - currentHeader.height - topOfStickyHeader
            if (shiftFromNextHeader < topOfStickyHeader) {
                translation.top += shiftFromNextHeader
            }
        } else {
            val leftOfStickyHeader = getListLeft(recyclerView) + mTempRect2.left + mTempRect2.right
            val shiftFromNextHeader =
                viewAfterNextHeader!!.left - nextHeader.width - mTempRect1.right - mTempRect1.left - currentHeader.width - leftOfStickyHeader
            if (shiftFromNextHeader < leftOfStickyHeader) {
                translation.left += shiftFromNextHeader
            }
        }
    }


    private fun getFirstViewUnobscuredByHeader(parent: RecyclerView, firstHeader: View): View? {
        val isReverseLayout = mOrientationProvider.isReverseLayout(parent)
        val step = if (isReverseLayout) -1 else 1
        val from = if (isReverseLayout) parent.childCount - 1 else 0
        var i = from
        while (i >= 0 && i <= parent.childCount - 1) {
            val child = parent.getChildAt(i)
            if (!itemIsObscuredByHeader(
                    parent,
                    child,
                    firstHeader,
                    mOrientationProvider.getOrientation(parent)
                )
            ) {
                return child
            }
            i += step
        }
        return null
    }


    private fun itemIsObscuredByHeader(
        parent: RecyclerView,
        item: View,
        header: View,
        orientation: Int
    ): Boolean {
        val layoutParams = item.layoutParams as RecyclerView.LayoutParams
        mDimensionCalculator.initMargins(mTempRect1, header)

        val adapterPosition = parent.getChildAdapterPosition(item)
        if (adapterPosition == RecyclerView.NO_POSITION || mHeaderProvider.getHeader(
                parent,
                adapterPosition
            ) !== header
        ) {
            return false
        }

        if (orientation == LinearLayoutManager.VERTICAL) {
            val itemTop = item.top - layoutParams.topMargin
            val headerBottom =
                getListTop(parent) + header.bottom + mTempRect1.bottom + mTempRect1.top
            if (itemTop >= headerBottom) {
                return false
            }
        } else {
            val itemLeft = item.left - layoutParams.leftMargin
            val headerRight =
                getListLeft(parent) + header.right + mTempRect1.right + mTempRect1.left
            if (itemLeft >= headerRight) {
                return false
            }
        }

        return true
    }

    private fun getListTop(view: RecyclerView): Int {
        return if (view.layoutManager!!.clipToPadding) {
            view.paddingTop
        } else {
            0
        }
    }

    private fun getListLeft(view: RecyclerView): Int {
        return if (view.layoutManager!!.clipToPadding) {
            view.paddingLeft
        } else {
            0
        }
    }
}
