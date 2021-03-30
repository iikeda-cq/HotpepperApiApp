package com.google.codelab.hotpepperapiapp

import androidx.core.graphics.ColorUtils
import kotlin.math.max

object IntExt {
    fun Int.actionBarColorToStatusBarColor(): Int {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(this, hsl)
        hsl[0] = max(0F, hsl[0] - 6)
        hsl[2] = max(0F, hsl[2] - 0.09F)
        return ColorUtils.HSLToColor(hsl)
    }
}
