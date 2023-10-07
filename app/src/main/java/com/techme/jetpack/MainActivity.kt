package com.techme.jetpack

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.techme.jetpack.navigation.AppBottomBar
import com.techme.jetpack.navigation.NavGraphBuilder
import com.techme.jetpack.ext.switchTab
import com.techme.jetpack.util.AppConfig

class MainActivity : AppCompatActivity() {
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NavGraphBuilder.build(navController, this, R.id.fragment_container)

        val appBottomBar = findViewById<AppBottomBar>(R.id.app_bottom_bar)
        appBottomBar.setOnItemSelectedListener {
            val tab = AppConfig.getBottomBarConfig().tabs[it.order]
            navController.switchTab(tab.route)
            !TextUtils.isEmpty(it.title)
        }
    }
}