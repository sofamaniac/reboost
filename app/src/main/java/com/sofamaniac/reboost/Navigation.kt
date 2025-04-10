package com.sofamaniac.reboost

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