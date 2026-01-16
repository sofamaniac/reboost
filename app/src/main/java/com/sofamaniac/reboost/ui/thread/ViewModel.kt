package com.sofamaniac.reboost.ui.thread

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.sofamaniac.reboost.PostRoute
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.data.local.entities.toDomainModel
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.comment.Sort
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.domain.repository.ThreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

@HiltViewModel
class ThreadViewModel @Inject constructor(
    private val repository: ThreadRepository,
    private val visitedPostsDao: VisitedPostsDao,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val permalink: String = savedStateHandle.toRoute<PostRoute>().post_permalink
    var id: String = repository.getPostId(permalink)

    val isRefreshing: Boolean
        get() = repository.isRefreshing()


    private val _sort = MutableStateFlow(Sort.Best)
    val sort: StateFlow<Sort> = _sort.asStateFlow()

    fun getPost(): PostData {
        val post = runBlocking(Dispatchers.IO) {
            val post = visitedPostsDao.getPost(id)
            if (post == null) {
                Log.e("ThreadViewModel", "getPost: Post not found in database ($id)")
            }
            post!!.toDomainModel()
        }
        return post
    }

    fun getComments(): List<Thing> {
        val comments = runBlocking(Dispatchers.IO) {
            repository.getComments(permalink, sort = _sort.value)
        }
        return comments
    }

    fun setSort(sort: Sort) {
        _sort.value = sort
    }


    fun refresh() {
        repository.refresh()
    }
}