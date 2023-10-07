package com.techme.jetpack.pages.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.techme.jetpack.ext.invokeViewModel
import com.techme.jetpack.list.AbsListFragment
import com.techme.jetpack.plugin.runtime.NavDestination
import kotlinx.coroutines.launch

@NavDestination(type = NavDestination.NavType.Fragment, route = "home_fragment", asStarter = true)
class HomeFragment : AbsListFragment() {
    private val viewModel: HomeViewModel by invokeViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.setFeedType(getFeedType())
            viewModel.hotFeeds.collect {
                submitData(it)
            }
        }
    }

    companion object {
        fun newInstance(feedType: String?): Fragment {
            val args = Bundle()
            args.putString("feedType", feedType)
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}