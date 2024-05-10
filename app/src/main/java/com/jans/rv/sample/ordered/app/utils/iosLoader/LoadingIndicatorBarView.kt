package com.jans.rv.sample.ordered.app.utils.iosLoader

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.RelativeLayout

@SuppressLint("ViewConstructor")
    class LoadingIndicatorBarView(
    context: Context,
    private val cornerRadius: Float
    ) :
        RelativeLayout(context) {
        init {
            initViews()
        }

        private fun initViews() {
            background = ToolBox.roundedCornerRectWithColor(
                Color.BLACK, cornerRadius
            )

            alpha = 0.5f
        }
    }