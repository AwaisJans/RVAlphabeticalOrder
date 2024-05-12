package com.jans.rv.sample.ordered.app.libStickyHeaders.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class LinearLayoutOrientationProvider : OrientationProvider {
    override fun getOrientation(recyclerView: RecyclerView?): Int {
        val layoutManager = recyclerView!!.layoutManager
        throwIfNotLinearLayoutManager(layoutManager)
        return (layoutManager as LinearLayoutManager?)!!.orientation
    }

    override fun isReverseLayout(recyclerView: RecyclerView?): Boolean {
        val layoutManager = recyclerView!!.layoutManager
        throwIfNotLinearLayoutManager(layoutManager)
        return (layoutManager as LinearLayoutManager?)!!.reverseLayout
    }

    private fun throwIfNotLinearLayoutManager(layoutManager: RecyclerView.LayoutManager?) {
        check(layoutManager is LinearLayoutManager) {
            "StickyListHeadersDecoration can only be used with a " +
                    "LinearLayoutManager."
        }
    }
}
