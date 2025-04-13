package com.sofamaniac.reboost.ui.post

import android.R
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sofamaniac.reboost.reddit.Comment
import com.sofamaniac.reboost.reddit.CommentsResponse
import com.sofamaniac.reboost.reddit.Listing
import com.sofamaniac.reboost.reddit.ListingData
import com.sofamaniac.reboost.reddit.More
import com.sofamaniac.reboost.reddit.Post
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.reddit.Thing
import com.sofamaniac.reboost.reddit.emptyListing
import com.sofamaniac.reboost.reddit.post.Kind
import com.sofamaniac.reboost.ui.SimpleMarkdown
import kotlinx.coroutines.launch


@Composable
fun PostView(
    post: Post,
    navController: NavController,
    selected: MutableIntState,
    modifier: Modifier = Modifier
) {
    Column(
//        modifier = Modifier
//            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp) // Space between title, content, buttons
    ) {
        PostHeader(
            post,
            navController,
            selected,
            showSubredditIcon = false,
            modifier = modifier
        )
        val enablePreview = post.data.thumbnail.url.isNotEmpty()
        PostInfo(
            post,
            navController,
            modifier = modifier.border(width = 2.dp, color = Color.Red),
            enablePreview = enablePreview,
            titleClickable = false
        )
        if (post.data.kind == Kind.Self) {
            SimpleMarkdown(
                post.data.selftext.html(),
                modifier,
            )
        }
        BottomRow(post, modifier)
    }
}

@Composable
fun CommentView(comment: Comment, modifier: Modifier = Modifier, depth: Int = 0) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        repeat(depth) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .heightIn(min = 40.dp) // or match parent height manually
                    .padding(end = 4.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(2.dp))
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Space between title, content, buttons
        ) {
            SimpleMarkdown(
                comment.data.body,
                modifier,
            )
            HorizontalDivider()
            if (!comment.data.replies.isEmpty()) {
                CommentList(comment.data.replies, depth = depth + 1)
            }
        }
    }
}

@Composable
fun CommentList(comments: Listing<Thing>, modifier: Modifier = Modifier, depth: Int = 0) {
    Column {
        comments.forEach { comment ->
            when (comment) {
                is Comment -> CommentView(comment, depth = depth)
                is More -> MoreViewer(comment)
                else -> {
                    Log.e("CommentList", "Unknown comment type: ${comment.javaClass.name}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentListRoot(
    viewModel: CommentsViewModel,
    navController: NavController,
    selected: MutableIntState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(viewModel) {
        viewModel.refresh()
    }
    Log.d("CommentListRoot", "comments: ${viewModel.comments}")
    PullToRefreshBox(isRefreshing = viewModel.isRefreshing, onRefresh = {
        scope.launch { viewModel.refresh() }
    }) {
        LazyColumn(modifier = modifier.fillMaxSize(), state = viewModel.listState) {
            item {
                // PostView takes only what it needs
                PostView(
                    post = viewModel.post,
                    navController = navController,
                    selected = selected,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                HorizontalDivider()
            }
            items(viewModel.comments.data.children.size) { index ->
                val comment = viewModel.comments.data.children[index]
                when (comment) {
                    is Comment -> CommentView(comment)
                    is More -> MoreViewer(comment)
                    else -> {
                        Log.e("CommentListRoot", "Unknown comment type: ${comment.javaClass.name}")
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

@Composable
fun CommentsViewer(
    navController: NavController,
    selected: MutableIntState,
    post: Post,
    modifier: Modifier = Modifier
) {
    val viewModel = remember { CommentsViewModel(post) }
    Scaffold { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        CommentListRoot(
            viewModel = viewModel,
            navController = navController,
            selected = selected,
            modifier = modifier
                .fillMaxWidth()
        )
    }
}

data class CommentsViewModel(val post: Post) {
    var comments by mutableStateOf<Listing<Thing>>(emptyListing())
    var isRefreshing by mutableStateOf(false)
    var listState by mutableStateOf(LazyListState())

    suspend fun refresh() {
        isRefreshing = true
        comments = RedditAPI.service.getComments(post.data.subreddit.subreddit, post.data.id)
            .body()?.comments ?: comments
        isRefreshing = false
    }
}