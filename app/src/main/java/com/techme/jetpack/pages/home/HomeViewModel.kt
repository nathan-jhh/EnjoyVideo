package com.techme.jetpack.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.techme.jetpack.http.ApiResult
import com.techme.jetpack.http.ApiService
import com.techme.jetpack.model.Feed

import com.techme.jetpack.pages.login.UserManager

class HomeViewModel : ViewModel() {
    val hotFeeds = Pager(config = PagingConfig(
        pageSize = 10, initialLoadSize = 10, enablePlaceholders = false, prefetchDistance = 1
    ), pagingSourceFactory = {
        HomePagingSource()
    }).flow.cachedIn(viewModelScope)

    private var feedType: String = "all"
    fun setFeedType(feedType: String) {
        this.feedType = feedType
    }

    inner class HomePagingSource : PagingSource<Long, Feed>() {
        override fun getRefreshKey(state: PagingState<Long, Feed>): Long? {
            return null
        }

        override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Feed> {
            val result = kotlin.runCatching {
                ApiService.getService().getFeeds(
                    feedId = params.key ?: 0L,
                    feedType = feedType,
                    userId = UserManager.userId()
                )
            }
            if (result.isFailure) {
                result.exceptionOrNull()?.printStackTrace()
            }
            val apiResult = result.getOrDefault(ApiResult())
            if (apiResult.success && apiResult.body?.isNotEmpty() == true) {
                return LoadResult.Page(apiResult.body!!, null, apiResult.body!!.last().id)
            }
            return if (params.key == null) LoadResult.Page(
                arrayListOf(), null, 0
            ) else LoadResult.Error(java.lang.RuntimeException("No more data to fetch"))
        }
    }
}