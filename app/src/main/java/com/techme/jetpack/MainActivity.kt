package com.techme.jetpack

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.techme.jetpack.databinding.ActivityMainBinding
import com.techme.jetpack.ext.invokeViewBinding
import com.techme.jetpack.navigation.NavGraphBuilder
import com.techme.jetpack.ext.switchTab
import com.techme.jetpack.pages.login.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewBinding: ActivityMainBinding by invokeViewBinding()
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        NavGraphBuilder.build(navController, this, R.id.fragment_container)

        viewBinding.appBottomBar.apply {
            setOnItemSelectedListener { item ->
                val tab = this.getTab(item.order) ?: return@setOnItemSelectedListener false
                if (tab.needLogin && !UserManager.isLogin()) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        UserManager.loginIfNeed()
                        UserManager.getUser().collectLatest {
                            if (it.userId > 0) {
                                // navController.switchTab(tab.route!!)
                                viewBinding.appBottomBar.selectedItemId = item.itemId
                            }
                        }
                    }
                    false
                } else {
                    navController.switchTab(tab.route!!)
                    !TextUtils.isEmpty(item.title)
                }
            }
        }
    }
}