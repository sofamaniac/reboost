package com.sofamaniac.reboost.ui.post

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.ProfileRoute
import com.sofamaniac.reboost.reddit.Comment
import com.sofamaniac.reboost.reddit.Listing
import com.sofamaniac.reboost.reddit.More
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.Thing
import com.sofamaniac.reboost.reddit.comment.Sort
import com.sofamaniac.reboost.reddit.emptyListing
import com.sofamaniac.reboost.ui.SimpleMarkdown
import kotlinx.coroutines.launch


@Composable
fun CommentNode(comment: Comment, modifier: Modifier = Modifier) {
    Column {
        Text(comment.data.author, color = MaterialTheme.colorScheme.primary)
        SimpleMarkdown(
            comment.data.body,
            modifier,
        )
    }
}

fun flattenComments(comment: Thing, depth: Int = 0): List<Pair<Thing, Int>> {
    return when (comment) {
        is Comment -> {
            listOf(Pair(comment, depth)) + comment.data.replies.flatMap {
                flattenComments(
                    it,
                    depth + 1
                )
            }
        }

        is More -> {
            listOf(Pair(comment, depth))
        }

        else -> {
            Log.e("flattenComments", "Unknown comment type: ${comment.javaClass.name}")
            emptyList()
        }

    }
}


@Composable
fun CommentView(comment: Comment, modifier: Modifier = Modifier) {
    val comments = flattenComments(comment)
    Column(modifier = Modifier.fillMaxWidth()) {
        for (comment in comments) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(comment.second) {
                    VerticalDivider(
                        modifier = Modifier
                            .width(2.dp)
                            .fillMaxHeight()
                    )

                }
                when (comment.first) {
                    is Comment -> CommentNode(comment.first as Comment)
                    is More -> MoreViewer(comment.first as More)
                    else -> {
                        Log.e(
                            "CommentViewImp",
                            "Unknown comment type: ${comment.first.javaClass.name}"
                        )
                    }
                }
            }
        }
        HorizontalDivider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentListRoot(
    viewModel: CommentsViewModel,
    selected: MutableIntState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current!!
    LaunchedEffect(viewModel) {
        viewModel.refresh()
    }
    PullToRefreshBox(
        isRefreshing = viewModel.isRefreshing,
        onRefresh = {
            viewModel.comments = emptyListing()
            scope.launch { viewModel.refresh() }
        },
        modifier = modifier.fillMaxSize()
    ) {
        val comments = viewModel.comments.data.children
        LazyColumn(modifier = Modifier.fillMaxSize(), state = viewModel.listState) {
            // Show post
            item {
                View(viewModel.post, selected) {
                    val selftext = viewModel.post.data.selftext.html()
                    if (selftext.isNotBlank()) {
                        Column {
                            Text(
                                viewModel.post.data.author.username,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable(onClick = {
                                    navController.navigate(ProfileRoute(viewModel.post.data.author.username))
                                })
                            )
                            SimpleMarkdown(selftext)
                        }
                    }
                }

                HorizontalDivider()
            }
            items(comments.size) { index ->
                when (val comment = comments[index]) {
                    is Comment -> CommentView(comment)
                    is More -> MoreViewer(comment)
                    else -> {
                        Log.e(
                            "CommentListRoot",
                            "Unknown comment type: ${comment.javaClass.name}"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MoreViewer(commentsResponse: More, modifier: Modifier = Modifier) {
    Text("More", modifier, style = MaterialTheme.typography.titleSmall)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(viewModel: CommentsViewModel, scrollBehavior: TopAppBarScrollBehavior?) {
    val navController = LocalNavController.current!!
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() },

                ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        },
        title = {
            Column {
                Text("Comments", style = MaterialTheme.typography.titleMedium)
                Text("${viewModel.sort}", style = MaterialTheme.typography.labelSmall)
            }
        },
        actions = {
            Icon(Icons.Default.Search, "Search comments")
            SortMenu(viewModel)
            Icon(Icons.Default.MoreVert, "More Options")
        }
    )
}

@Composable
fun SortMenu(state: CommentsViewModel) {
    var sortExpanded = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Box {
        IconButton(onClick = { sortExpanded.value = true }) {
            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
        }
        DropdownMenu(
            expanded = sortExpanded.value,
            onDismissRequest = { sortExpanded.value = false }) {
            Sort.entries.forEach { sort ->
                DropdownMenuItem(text = { Text(sort.toString()) }, onClick = {
                    state.sort = sort
                    scope.launch { state.refresh() }
                    sortExpanded.value = false
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsViewer(
    selected: MutableIntState,
    post: Post,
    modifier: Modifier = Modifier,
    viewModel: CommentsViewModel = viewModel(factory = CommentsViewModelFactory(post))
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = { TopBar(viewModel, scrollBehavior) },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        CommentListRoot(
            viewModel = viewModel,
            selected = selected,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

data class CommentsViewModel(val post: Post, val initialSort: Sort = Sort.Best) : ViewModel() {
    var comments by mutableStateOf<Listing<Thing>>(emptyListing())
    var isRefreshing by mutableStateOf(false)
    var listState by mutableStateOf(LazyListState())
    var sort: Sort by mutableStateOf(initialSort)

    suspend fun refresh() {
        isRefreshing = true
        comments =
            RedditAPI.service.getComments(post.data.subreddit.name, post.data.id, sort = sort)
                .body()?.comments ?: emptyListing()
        isRefreshing = false
    }
}

class CommentsViewModelFactory(private val post: Post) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommentsViewModel(post) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
