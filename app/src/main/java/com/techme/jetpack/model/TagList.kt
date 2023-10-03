package com.techme.jetpack.model

import androidx.annotation.Keep


@Keep
data class TagList(
    val activityIcon: String?,
    val background: String?,
    val enterNum: Int,
    val feedNum: Int,
    val followNum: Int,
    val hasFollow: Boolean,
    val icon: String?,
    val id: Int,
    val intro: String?,
    val tagId: Long,
    val title: String?
)