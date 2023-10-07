// this file is generated by auto,please do not modify!!!
package com.techme.jetpack.plugin.runtime

import com.techme.jetpack.plugin.runtime.NavDestination.NavType.Activity
import com.techme.jetpack.plugin.runtime.NavDestination.NavType.Dialog
import com.techme.jetpack.plugin.runtime.NavDestination.NavType.Fragment
import com.techme.jetpack.plugin.runtime.NavDestination.NavType.None
import kotlin.collections.ArrayList
import kotlin.collections.List

object NavRegistry {
    private val navList: ArrayList<NavData> = ArrayList<NavData>()


    init {
        navList.add(NavData("tags_fragment","com.techme.jetpack.navigation.TagsFragment",false,Fragment))
                navList.add(NavData("user_fragment","com.techme.jetpack.navigation.UserFragment",false,Fragment))
                navList.add(NavData("category_fragment","com.techme.jetpack.pages.category.CategoryFragment",false,Fragment))
                navList.add(NavData("home_fragment","com.techme.jetpack.pages.home.HomeFragment",true,Fragment))

    }

    fun get(): List<NavData> {
        val list = ArrayList<NavData>()
                 list.addAll(navList)
                 return list

    }
}
