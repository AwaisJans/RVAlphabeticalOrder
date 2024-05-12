package com.jans.rv.sample.ordered.app.libStickyHeaders.util

import androidx.recyclerview.widget.RecyclerView


/**
 * Interface for getting the orientation of a RecyclerView from its LayoutManager
 */
interface OrientationProvider {
    fun getOrientation(recyclerView: RecyclerView?): Int

    fun isReverseLayout(recyclerView: RecyclerView?): Boolean
}
