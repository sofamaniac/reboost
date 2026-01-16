/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.feed

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Thing.Post
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.post.Sort
import com.sofamaniac.reboost.data.repository.AccountsRepository
import com.sofamaniac.reboost.domain.model.PagedResponse
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class SavedRepository @Inject constructor(
    api: RedditAPIService,
    private val accountsRepository: AccountsRepository
) : PostRepository( api), FeedRepository {
    override suspend fun getPosts(after: String, sort: Sort, timeframe: Timeframe?): PagedResponse<Post> {
        val user = accountsRepository.activeAccount.first()
        if (user.isAnonymous()) return PagedResponse()
        return makeRequest {
            api.getSaved(
                after = after,
                user = user.username,
                sort = sort,
                timeframe = timeframe
            )
        }
    }

}
