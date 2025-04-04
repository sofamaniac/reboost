package com.sofamaniac.reboost.ui.subredditList

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sofamaniac.reboost.reddit.Identity
import kotlinx.coroutines.launch

class SubscriptionViewModel(repository: Repository) : ViewModel() {
    private var state: LazyListState? = null

    @Composable
    fun rememberLazyListState(): LazyListState {
        if (state == null) {
            state = androidx.compose.foundation.lazy.rememberLazyListState()
        }
        return state!!
    }

    val data = Pager(
        config = PagingConfig(pageSize = 100),
        initialKey = "",
        pagingSourceFactory = {
            Source(repository)
        }
    ).flow.cachedIn(viewModelScope)
    var user: Identity? = null

    init {
        viewModelScope.launch {
            user = repository.getUser()
        }
    }
}
