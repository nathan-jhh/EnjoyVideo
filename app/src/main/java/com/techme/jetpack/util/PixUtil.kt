package com.techme.jetpack.util

object PixUtil {
    fun dp2px(dpValue: Int): Int {
        val metrics = AppGlobals.getApplication().resources.displayMetrics
        return (metrics.density * dpValue + 0.5f).toInt()
    }

    fun getScreenWidth(): Int {
        val metrics = AppGlobals.getApplication().resources.displayMetrics
        return metrics.widthPixels
    }

    fun getScreenHeight(): Int {
        val metrics = AppGlobals.getApplication().resources.displayMetrics
        return metrics.heightPixels
    }
}