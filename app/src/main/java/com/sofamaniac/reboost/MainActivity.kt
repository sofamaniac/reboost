package com.sofamaniac.reboost

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sofamaniac.reboost.ui.theme.ReboostTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import retrofit2.Response


class MainActivity : ComponentActivity() {

    private lateinit var authService: AuthorizationService
    private lateinit var authRequest: AuthorizationRequest
    private lateinit var authManager: AuthManager
    private val authConfig = AuthConfig(
        authorizationEndpoint = "https://ssl.reddit.com/api/v1/authorize",
        tokenEndpoint = "https://ssl.reddit.com/api/v1/access_token",
        redirectUri = "com.sofamaniac.reboost://callback",
        clientId = BuildConfig.REDDIT_CLIENT_ID,
        scopes = listOf(
            "identity",
            "edit",
            "flair",
            "history",
            "modconfig",
            "modflair",
            "modlog",
            "modposts",
            "modwiki",
            "mysubreddits",
            "privatemessages",
            "read",
            "report",
            "save",
            "submit",
            "subscribe",
            "vote",
            "wikiedit",
            "wikiread"
        )
    )


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authService = AuthorizationService(this)
        authRequest = createAuthorizationRequest(authConfig)
        authManager = AuthManager(this)
        Log.d("MainActivity", "onCreate: ${authManager.getCurrent().jsonSerializeString()}")
        enableEdgeToEdge()
        setContent {
            ReboostTheme {
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) {
                    if (it.resultCode == RESULT_OK) {
                        val resp = AuthorizationResponse.fromIntent(it.data!!)
                        val error = AuthorizationException.fromIntent(it.data)
                        authManager.update(resp, error)

                        handleAuthorizationResponse(resp, error, { })
                        // Handle the authorization response
                    } else {
                        Log.e("MainActivity", "onCreate: $it")
                    }
                }
                MaterialTheme {
                    val currentScreen = remember { mutableStateOf("home") }
                    val navController = rememberNavController()
                    val scrollBehavior =
                        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
                    Scaffold(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = { MakeTopBar(currentScreen.value, scrollBehavior) },
                        bottomBar = {
                            Row {
                                Button(onClick = {
                                    currentScreen.value = "home"
                                    navController.navigate("home")
                                }) { Text("Home") }
                                Button(onClick = {
                                    currentScreen.value = "saved"
                                    navController.navigate("saved")

                                }) { Text("Saved") }
                            }
                        }
                    )
                    { innerPadding ->
                        MainScreen(
                            authManager,
                            navController,
                            onLoginClicked = {
                                // Create and launch the auth intent here
                                val authIntent =
                                    authService.getAuthorizationRequestIntent(authRequest)
                                launcher.launch(authIntent)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authService.dispose()
    }

    private fun handleAuthorizationResponse(
        resp: AuthorizationResponse?,
        error: AuthorizationException?,
        onLoginSuccess: () -> Unit
    ) {
        if (resp != null) {
            val basicAuth = BasicAuthClient(authConfig.clientId)
            val tokenExchangeRequest = resp.createTokenExchangeRequest()
            Log.d("MainActivity", "Token request: ${tokenExchangeRequest.jsonSerializeString()}")
            Log.d("MainActivity", "redirect uri: ${authConfig.redirectUri}")
            authService.performTokenRequest(
                tokenExchangeRequest,
                basicAuth
            ) { tokenResponse, tokenException ->
                if (tokenResponse != null) {
                    authManager.update(tokenResponse, tokenException)
                    Log.d("MainActivity", "onCreate: ${authManager.loggedIn}")
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
fun MakeTopBar(title: String, scrollBehavior: TopAppBarScrollBehavior? = null) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(title)
        }
    )

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyImage(imageUrl: String, modifier: Modifier = Modifier) {

    GlideImage(
        model = imageUrl,
        contentDescription = "Image from URL",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit
    ) {
        it.placeholder(R.drawable.loading_image) // Default placeholder
        it.error(R.drawable.loading_image) // Default error
        it.load(imageUrl)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullPostViewer(title: String, viewer: PostViewer, modifier: Modifier = Modifier) {
    viewer.View()
}

@Composable
fun OnePostViewer(post: Post, modifier: Modifier = Modifier) {
    val json = Json {
        prettyPrint = true
    }
    Column(modifier = Modifier.padding(8.dp)) {
        PostItem(post, currentPost = remember { mutableStateOf(null) })
        Text(json.encodeToString(Post.serializer(), post), softWrap = true)
    }
}

class SavedViewer(context: Context) : PostViewer(context) {
    override suspend fun requestPage(): Response<Listing<Post>> {
        return apiService.getSavedPosts(username, after, count)
    }
}

class HomeViewer(context: Context) : PostViewer(context) {
    override suspend fun requestPage(): Response<Listing<Post>> {
        return apiService.getHotPosts(after, count)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authManager: AuthManager,
    navController: NavHostController,
    onLoginClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val viewer = HomeViewer(context)
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                authManager,
                onClick = {
                    onLoginClicked()
                    scope.launch { drawerState.close() }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        },
    ) {
        Column {
            NavigationGraph(
                navController,
            )
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
fun DrawerContent(authManager: AuthManager, onClick: () -> Unit, modifier: Modifier = Modifier) {
    //Only show the login button if the user is not logged in
    val context = LocalContext.current
    if (authManager.loggedIn) {
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
                    RedditAPI.getApiService(context).getIdentity().body()?.username ?: "Anonymous"
            }
        }
        Text(
            "Hello $username!",
            modifier = modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            HomeScreen()
        }
        composable("saved") {
            SavedScreen()
        }
    }

}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewer = HomeViewer(context)
    FullPostViewer("Home", viewer)
}

@Composable
fun SavedScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewer = SavedViewer(context)
    FullPostViewer("Saved", viewer)
}