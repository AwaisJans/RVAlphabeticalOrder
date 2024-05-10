package com.jans.rv.sample.ordered.app.utils.iosLoader

import android.annotation.SuppressLint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape

@SuppressLint("StaticFieldLeak")
    class ToolBox
    private constructor() {
        companion object {
            fun roundedCornerRectWithColor(color: Int, cornerRadius: Float): ShapeDrawable {
                val radii = floatArrayOf(
                    cornerRadius, cornerRadius,
                    cornerRadius, cornerRadius,
                    cornerRadius, cornerRadius,
                    cornerRadius, cornerRadius
                )

                val roundedCornerShape = RoundRectShape(radii, null, null)

                val shape = ShapeDrawable()
                shape.paint.color = color
                shape.shape = roundedCornerShape

                return shape
            }
        }
    }
    
    