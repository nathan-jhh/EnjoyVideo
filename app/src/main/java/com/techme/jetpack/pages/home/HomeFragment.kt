package com.techme.jetpack.pages.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.techme.jetpack.R
import com.techme.jetpack.databinding.LayoutFragmentHomeBinding
import com.techme.jetpack.ext.invokeViewModel
import com.techme.jetpack.http.ApiService
import com.techme.jetpack.list.AbsListFragment
import com.techme.jetpack.navigation.BaseFragment
import com.techme.jetpack.navigation.navigateTo
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
}