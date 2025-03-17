package com.sofamaniac.reboost

enum class RouteType {
    Home,
    Search,
    Subscriptions,
    Inbox,
    Profile
}

object Routes {
    val home = object : Route {
        override val route: String = RouteType.Home.name
        override val title: String = "Home"
    }

    val subscriptions = object : Route {
        override val route: String = RouteType.Subscriptions.name
        override val title: String = "Subscriptions"
    }
}

interface Route {
    val route: String
    val title: String
}