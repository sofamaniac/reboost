package com.sofamaniac.reboost

import android.os.Bundle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.sofamaniac.reboost.reddit.AuthorInfo
import com.sofamaniac.reboost.reddit.post.SubredditInfo
import com.sofamaniac.reboost.ui.subreddit.HomeView
import com.sofamaniac.reboost.ui.subreddit.SavedView
import com.sofamaniac.reboost.ui.subreddit.SubredditView
import com.sofamaniac.reboost.ui.subredditList.SubscriptionState
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

enum class RouteType {
    Home,
    Search,
    Subscriptions,
    Inbox,
    Profile,
    Subreddit,
    Post,
    Saved,
}

@Serializable
object Home : Route {
    override val route: String = RouteType.Home.name
    override val title: String = "Home"
}

@Serializable
class Profile(val author: String) : Route {
    override val route: String = RouteType.Profile.name
    override val title: String = "Profile"
}

@Serializable
object Saved : Route {
    override val route: String = RouteType.Saved.name
    override val title: String = "Saved"
}

@Serializable
object Subscriptions : Route {
    override val route: String = RouteType.Subscriptions.name
    override val title: String = "Subscriptions"
}

@Serializable
object Search : Route {
    override val route: String = RouteType.Search.name
    override val title: String = "Search"
}

@Serializable
object Inbox : Route {
    override val route: String = RouteType.Inbox.name
    override val title: String = "Inbox"
}

@Serializable
class Subreddit(val subreddit: String) : Route {
    override val route: String = RouteType.Subreddit.name
    override val title: String = "Subreddit"
}

@Serializable
class Post(val post_permalink: String) : Route {
    override val route: String = RouteType.Post.name
    override val title: String = "Post"
}

interface Route {
    val route: String
    val title: String
}