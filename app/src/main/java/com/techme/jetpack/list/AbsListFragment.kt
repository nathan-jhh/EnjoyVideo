package com.techme.jetpack.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.techme.jetpack.R
import com.techme.jetpack.databinding.LayoutAbsListFragmentBinding
import com.techme.jetpack.ext.invokeViewBinding
import com.techme.jetpack.ext.setVisibility
import com.techme.jetpack.model.Feed
import kotlinx.coroutines.launch

open class AbsListFragment : Fragment(R.layout.layout_abs_list_fragment) {
    private val viewBinding: LayoutAbsListFragmentBinding by invokeViewBinding()
    private lateinit var feedAdapter: FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val context: Context = requireContext()

        // recyclerview
        viewBinding.listView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        feedAdapter = FeedAdapter(getFeedType(), lifecycleOwner = viewLifecycleOwner)
        val contactAdapter = feedAdapter.withLoadStateFooter(FooterLoadStateAdapter())
        viewBinding.listView.adapter = contactAdapter
        viewBinding.listView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // refreshLayout
        viewBinding.refreshLayout.setColorSchemeColors(context.getColor(R.color.color_theme))
        viewBinding.refreshLayout.setOnRefreshListener {
            feedAdapter.refresh()
        }

        lifecycleScope.launch {
            // 监听列表数据加载状态. 刷新、 重试
            // 当列表没有展示任务数据时，展示出兜底页
            feedAdapter.onPagesUpdatedFlow.collect {
                val hasData = feedAdapter.itemCount > 0
                viewBinding.refreshLayout.isRefreshing = false
                viewBinding.listView.setVisibility(hasData)
                viewBinding.loadingStatus.setVisibility(!hasData)
                if (!hasData) {
                    viewBinding.loadingStatus.showEmpty {
                        feedAdapter.retry()
                    }
                }
            }
        }
    }

    fun getFeedType(): String {
        return arguments?.getString("feedType") ?: "all"
    }

    fun submitData(pagingData: PagingData<Feed>) {
        lifecycleScope.launch {
            feedAdapter.submitData(pagingData)
        }
    }
}