package com.sofamaniac.reboost

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sofamaniac.reboost.auth.BasicAuthClient
import com.sofamaniac.reboost.auth.Manager
import com.sofamaniac.reboost.auth.StoreManager
import com.sofamaniac.reboost.reddit.RedditAPI
import com.sofamaniac.reboost.ui.theme.ReboostTheme
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse


interface Tab {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(drawerState: DrawerState, scrollBehavior: TopAppBarScrollBehavior?)

    @Composable
    fun Content()
}

class MainActivity : ComponentActivity() {

    private lateinit var authState: Manager
    private lateinit var tabs: List<Tab>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = RedditAPI.getApiService(this)
        tabs = listOf(
            com.sofamaniac.reboost.ui.SubredditViewerState("awwnime"),
            com.sofamaniac.reboost.ui.SubredditViewerState("anime"),
            com.sofamaniac.reboost.ui.subredditList.SubscriptionState(),
            com.sofamaniac.reboost.ui.SubredditViewerState("unixporn"),
            com.sofamaniac.reboost.ui.SubredditViewerState("unixporn")
        )
        enableEdgeToEdge()
        setContent {
            ReboostTheme {
                authState = Manager(this)
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) {
                    if (it.resultCode == RESULT_OK) {
                        val resp = AuthorizationResponse.fromIntent(it.data!!)
                        val error = AuthorizationException.fromIntent(it.data)
                        authState.authManager.update(resp, error)

                        handleAuthorizationResponse(resp, error) { }
                        // Handle the authorization response
                    } else {
                        Log.e("MainActivity", "onCreate: $it")
                    }
                }
                MaterialTheme {
                    val navController = rememberNavController()
                    MainScreen(
                        authState,
                        tabs,
                        navController = navController,
                        onLoginClicked = {
                            // Create and launch the auth intent here
                            val authIntent =
                                authState.authService.getAuthorizationRequestIntent(authState.authRequest)
                            launcher.launch(authIntent)
                        },
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authState.dispose()
    }

    private fun handleAuthorizationResponse(
        resp: AuthorizationResponse?,
        error: AuthorizationException?,
        onLoginSuccess: () -> Unit
    ) {
        if (resp != null) {
            val basicAuth = BasicAuthClient(authState.authConfig.clientId)
            val tokenExchangeRequest = resp.createTokenExchangeRequest()
            Log.d("MainActivity", "Token request: ${tokenExchangeRequest.jsonSerializeString()}")
            Log.d("MainActivity", "redirect uri: ${authState.authConfig.redirectUri}")
            authState.authService.performTokenRequest(
                tokenExchangeRequest,
                basicAuth
            ) { tokenResponse, tokenException ->
                if (tokenResponse != null) {
                    authState.authManager.update(tokenResponse, tokenException)
                    Log.d("MainActivity", "onCreate: ${authState.authManager.loggedIn}")
                    onLoginSuccess()
                } else {
                    Log.d("MainActivity", "token exception: ${tokenException.toString()}")
                }
            }
        } else {
            Log.e("MainActivity", "auth exception: ${error.toString()}")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeTopBar(
    activeTab: String,
    tabs: List<Tab>,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    drawerState: DrawerState,
) {
    val tab = when (activeTab) {
        "home" ->
            tabs[0]

        "subscriptions" ->
            tabs[2]

        else ->
            tabs.last()
    }
    tab.TopBar(drawerState, scrollBehavior)
}


@Composable
fun BottomBar(navController: NavHostController) {
    var selected by remember { mutableIntStateOf(0) }
    val tabs = listOf<Route>(
        Routes.home,
        Routes.search,
        Routes.subscriptions,
        Routes.inbox,
        Routes.profile
    )
    CustomNavigationBar {
        tabs.forEachIndexed { index, tab ->
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    selected = index
                    navController.navigate(tab.route)
                }
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.title,
                    tint = if (selected == index)
                        MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
    }
}

@Composable
fun CustomNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .defaultMinSize(minHeight = 16.dp) // Change minHeight to your desired height
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authState: Manager,
    tabs: List<Tab>,
    navController: NavHostController,
    onLoginClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                authState.authManager,
                onClick = {
                    onLoginClicked()
                    scope.launch { drawerState.close() }
                },
            )
        },
    ) {
        Column(modifier) {
            val scrollBehavior =
                TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    MakeTopBar(
                        navController.currentBackStackEntry?.destination?.route ?: "home",
                        tabs,
                        scrollBehavior,
                        drawerState,
                    )
                },
                bottomBar = {
                    BottomBar(navController)
                }
            )
            { innerPadding ->
                NavigationGraph(
                    navController,
                    tabs,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun LoginButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = { onClick() }, modifier = modifier) {
        Text(
            "Login",
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun DrawerContent(
    authManager: StoreManager,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            val context = LocalContext.current
            if (!authManager.loggedIn) {
                Spacer(modifier = Modifier.padding(16.dp))
                LoginButton(
                    modifier = modifier.padding(16.dp),
                    onClick = onClick
                )
            } else {
                var username by remember { mutableStateOf("Anonymous") }
                if (username == "Anonymous") {
                    LaunchedEffect(username) {
                        username =
                            RedditAPI.getApiService(context).getIdentity().body()?.username
                                ?: "Anonymous"
                    }
                }
                Text(
                    "Hello $username!",
                    modifier = modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    tabs: List<Tab>,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.home.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(Routes.home.route) {
            tabs.first().Content()
        }
        composable(Routes.search.route) {
            Text("Search")
        }
        composable(Routes.subscriptions.route) {
            tabs[2].Content()
        }
        composable(Routes.inbox.route) {
            Text("You got mail!")
        }
        composable(Routes.profile.route) {
            tabs[1].Content()
        }
    }

}