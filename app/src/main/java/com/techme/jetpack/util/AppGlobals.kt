package com.techme.jetpack.util

import android.app.Application

private var sApplication: Application? = null

object AppGlobals {
    fun getApplication(): Application {
        if (sApplication == null) {
            kotlin.runCatching {
                sApplication =
                    Class.forName("android.app.ActivityThread").getMethod("currentApplication")
                        .invoke(null, *emptyArray()) as Application
            }.onFailure {
                it.printStackTrace()
            }
        }
        return sApplication!!
    }
}