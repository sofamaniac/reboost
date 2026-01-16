/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.subredditList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.reboost.data.remote.dto.Thing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(private val subscriptionsRepository: SubscriptionsRepository) :
    ViewModel() {
    private var _subscriptions: MutableStateFlow<List<Thing.Subreddit>?> = MutableStateFlow(null)

    val subscriptions: StateFlow<List<Thing.Subreddit>?> get() = _subscriptions.asStateFlow()
    fun loadSubscriptions() {
        viewModelScope.launch {
            var after: String? = ""
            _subscriptions.value = emptyList()
            while (after != null) {
                val response = subscriptionsRepository.getSubreddits(after)
                after = response.data.lastOrNull()?.data?.name
                _subscriptions.update {
                    it?.plus(response.data) ?: response.data
                }
            }
            Log.d(
                "SubscriptionViewModel",
                "loadSubscriptions: ${_subscriptions.value?.size} subreddits loaded"
            )
        }
    }

    fun refresh() {
        loadSubscriptions()
    }

    init {
        loadSubscriptions()
    }
}
