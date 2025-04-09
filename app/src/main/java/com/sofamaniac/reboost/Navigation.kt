package com.sofamaniac.reboost

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.sofamaniac.reboost.ui.subreddit.HomeView
import com.sofamaniac.reboost.ui.subreddit.SavedView
import com.sofamaniac.reboost.ui.subreddit.SubredditView
import com.sofamaniac.reboost.ui.subredditList.SubscriptionState
import kotlinx.serialization.Serializable

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

object Home : Route {
    override val route: String = RouteType.Home.name
    override val title: String = "Home"
}

object Profile : Route {
    override val route: String = RouteType.Profile.name
    override val title: String = "Profile"
    const val user = "user"
    val arguments = listOf(navArgument(user) { type = NavType.StringType })
}

object Saved : Route {
    override val route: String = RouteType.Saved.name
    override val title: String = "Saved"
}

object Subscriptions : Route {
    override val route: String = RouteType.Subscriptions.name
    override val title: String = "Subscriptions"
}

object Search : Route {
    override val route: String = RouteType.Search.name
    override val title: String = "Search"
}

object Inbox : Route {
    override val route: String = RouteType.Inbox.name
    override val title: String = "Inbox"
}

object Subreddit : Route {
    override val route: String = RouteType.Subreddit.name
    const val destination: String = "destination"
    override val title: String = "Subreddit"
    val arguments = listOf(navArgument(Subreddit.destination) { type = NavType.StringType })
}

object Post : Route {
    override val route: String = RouteType.Post.name
    const val destination: String = "destination"
    override val title: String = "Post"
    val arguments = listOf(navArgument(destination) { type = NavType.StringType })
}

interface Route {
    val route: String
    val title: String
}