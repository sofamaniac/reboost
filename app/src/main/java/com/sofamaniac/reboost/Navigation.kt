package com.sofamaniac.reboost

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.sofamaniac.reboost.ui.SubredditViewerState
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
        override var state: Tab = SubredditViewerState("awwnime")
    }

    val subscriptions = object : Route {
        override val route: String = RouteType.Subscriptions.name
        override val title: String = "Subscriptions"
        override val icon = Icons.AutoMirrored.Outlined.List
        override var state: Tab = SubredditViewerState("linux")
    }

    val search = object : Route {
        override val route: String = RouteType.Search.name
        override val title: String = "Search"
        override val icon = Icons.Outlined.Search
        override var state: Tab = SubredditViewerState("anime")
    }

    val inbox = object : Route {
        override val route: String = RouteType.Inbox.name
        override val title: String = "Inbox"
        override val icon: ImageVector = Icons.Filled.Email
        override var state: Tab = SubredditViewerState("arknuts")
    }

    val profile = object : Route {
        override val route: String = RouteType.Profile.name
        override val title: String = "Profile"
        override val icon: ImageVector = Icons.Filled.Person
        override var state: Tab = SubredditViewerState("unixporn")
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