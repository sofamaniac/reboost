package com.sofamaniac.reboost

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
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
    Profile
}

@Serializable
object Routes {
    val home = object : Route {
        override val route: String = RouteType.Home.name
        override val title: String = "Home"
        override val icon = Icons.Filled.Home
        override var state: Tab = HomeView()
    }

    val subscriptions = object : Route {
        override val route: String = RouteType.Subscriptions.name
        override val title: String = "Subscriptions"
        override val icon = Icons.AutoMirrored.Outlined.List
        override var state: Tab = SubscriptionState()
    }

    val search = object : Route {
        override val route: String = RouteType.Search.name
        override val title: String = "Search"
        override val icon = Icons.Outlined.Search
        override var state: Tab = SubredditView("anime")
    }

    val inbox = object : Route {
        override val route: String = RouteType.Inbox.name
        override val title: String = "Inbox"
        override val icon: ImageVector = Icons.Filled.Email
        override var state: Tab = SubredditView("arknuts")
    }

    val profile = object : Route {
        override val route: String = RouteType.Profile.name
        override val title: String = "Profile"
        override val icon: ImageVector = Icons.Filled.Person
        override var state: Tab = SavedView()
    }

}

val TABS = listOf(Routes.home, Routes.search, Routes.subscriptions, Routes.inbox, Routes.profile)

fun getTab(route: String): Route {
    return TABS.first { it.route == route }
}

interface Route {
    val route: String
    val title: String
    val icon: ImageVector
    var state: Tab
}