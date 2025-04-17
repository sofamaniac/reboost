package com.sofamaniac.reboost

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
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

@Serializable
object Home : Route {
    override val route: String = RouteType.Home.name
    override val title: String = "Home"
}

@Serializable
class ProfileRoute(val author: String) : Route {
    override val route: String = RouteType.Profile.name
    override val title: String = "Profile"
}

@Serializable
object SavedRoute : Route {
    override val route: String = RouteType.Saved.name
    override val title: String = "Saved"
}

@Serializable
object SubscriptionsRoute : Route {
    override val route: String = RouteType.Subscriptions.name
    override val title: String = "Subscriptions"
}

@Serializable
object SearchRoute : Route {
    override val route: String = RouteType.Search.name
    override val title: String = "Search"
}

@Serializable
object InboxRoute : Route {
    override val route: String = RouteType.Inbox.name
    override val title: String = "Inbox"
}

@Serializable
class SubredditRoute(val subreddit: String) : Route {
    override val route: String = RouteType.Subreddit.name
    override val title: String = "Subreddit"
}

@Serializable
class PostRoute(val post_permalink: String) : Route {
    override val route: String = RouteType.Post.name
    override val title: String = "Post"
}

@Serializable
object LicensesRoute : Route {
    override val route: String = "Licenses"
    override val title: String = "Licenses"
}

interface Route {
    val route: String
    val title: String
}

// TODO move closer in the navgraph. Maybe one per tab ? Or move its initalisation to the tabs ?
val LocalNavController = compositionLocalOf<NavController?> { null }
