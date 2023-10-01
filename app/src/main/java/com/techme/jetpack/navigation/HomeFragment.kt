package com.techme.jetpack.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.techme.jetpack.R
import com.techme.jetpack.databinding.LayoutFragmentHomeBinding
import com.techme.jetpack.http.ApiService
import com.techme.jetpack.plugin.runtime.NavDestination
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

@NavDestination(type = NavDestination.NavType.Fragment, route = "home_fragment", asStarter = true)
class HomeFragment : BaseFragment() {
    lateinit var homeBinding: LayoutFragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = LayoutFragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    var flag = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        homeBinding.navigateToCategoryFragment.setOnClickListener {
            // 对于fragment 类型的路由节点，在 navigate 跳转
            // 的时候使用的fragmentTransaction#replace
            // 因此跳转后，当前的fragment会执行onDestroyView方法
            if (flag) {
                navController.navigateTo("category_fragment")
                flag = true
            } else {
                navController.navigate(
                    R.id.category_fragment,
                    null,
                    NavOptions.Builder().setRestoreState(true).build()
                )
            }

        }

        lifecycleScope.launch {
            ApiService.getService().getFeeds()
        }
    }
}