package com.sofamaniac.reboost

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.reboost.ui.theme.ReboostTheme
import kotlinx.coroutines.launch
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
                    MainScreen(
                        authManager = authManager,
                        onLoginClicked = {
                            val authIntent = authService.getAuthorizationRequestIntent(authRequest)
                            launcher.launch(authIntent)
                        },
                    )
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

abstract class Paginator<Type : Thing<Any>>(context: Context) : ViewModel() {
    protected val apiService = RedditAPI.getApiService(context)
    protected lateinit var username: String
    protected val _posts = mutableStateListOf<Type>()
    val posts: SnapshotStateList<Type> = _posts
    var after: String? = null
    var count: Int = 0

    init {
        firstPage()
    }

    abstract suspend fun requestPage(): Response<Listing<Type>>

    suspend fun handlePage() {
        val response = requestPage()
        if (response.isSuccessful) {
            val listing = response.body()
            listing?.let {
                _posts.addAll(it.data.children)
                count += it.size()
                after = it.after()
            }
        }

    }

    fun firstPage() {
        viewModelScope.launch {
            if (!::username.isInitialized) {
                username = apiService.getIdentity().body()?.username!!
            }
            handlePage()
        }
    }

    suspend fun loadPage() {
        if (after != null) {
            handlePage()
        }
    }

    @Composable
    abstract fun View(modifier: Modifier = Modifier)
}

abstract class PostViewer(context: Context) : Paginator<Post>(context) {

    @Composable
    override fun View(modifier: Modifier) {
        val posts: List<Post> = posts.toList()
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(posts) { post ->
                Text(post.data.title, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(thickness = 1.dp)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemIndex }.collect {
                if (it >= posts.size - 1) {
                    loadPage()
                }
            }
        }
    }
}

class SavedViewer(context: Context): PostViewer(context) {
    override suspend fun requestPage(): Response<Listing<Post>> {
        return apiService.getSavedPosts(username, after, count)
    }
}

class HomeViewer(context: Context): PostViewer(context) {
    override suspend fun requestPage(): Response<Listing<Post>> {
        return apiService.getHotPosts(after, count)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authManager: AuthManager,
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
                modifier
            )
        },
    ) {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                if (authManager.loggedIn) {
                    viewer.View(modifier)
                } else {
                    Text(
                        "Please login",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    LoginButton(onClick = { onLoginClicked() })
                }
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
fun DrawerContent(authManager: AuthManager, onClick: () -> Unit, modifier: Modifier = Modifier) {
    //Only show the login button if the user is not logged in
    val context = LocalContext.current
    if (!authManager.loggedIn) {
        LoginButton(
            modifier = Modifier.padding(16.dp),
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
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}