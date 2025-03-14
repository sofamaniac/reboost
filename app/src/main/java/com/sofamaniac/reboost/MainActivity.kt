package com.sofamaniac.reboost

import android.app.Activity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication




class MainActivity : ComponentActivity() {


    private lateinit var authService: AuthorizationService
    private lateinit var authRequest: AuthorizationRequest
    private lateinit var authState: AuthState

    private val authConfig = AuthConfig(
        authorizationEndpoint = "https://ssl.reddit.com/api/v1/authorize",
        tokenEndpoint = "https://ssl.reddit.com/api/v1/access_token",
        redirectUri = "com.sofamaniac.reboost://callback",
        clientId = BuildConfig.REDDIT_CLIENT_ID,
        scopes = listOf("identity")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authService = AuthorizationService(this)
        authRequest = createAuthorizationRequest(authConfig)
        authState = AuthState()
        enableEdgeToEdge()
        setContent {
            var userPseudo by remember { mutableStateOf("Anonymous") }
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val resp = AuthorizationResponse.fromIntent(it.data!!)
                    val error = AuthorizationException.fromIntent(it.data)
                    authState.update(resp, error)
                    handleAuthorizationResponse(resp, error, { userPseudo = "Bloop" })
                    // Handle the authorization response
                } else {
                    Log.e("MainActivity", "onCreate: ${it}")
                }
            }

            MainScreen(
                userPseudo = userPseudo,
                onLoginClicked = {
                    val authIntent = authService.getAuthorizationRequestIntent(authRequest)
                    launcher.launch(authIntent)
                },
            )

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
            val basicAuth = CustomClientAuthentication(authConfig.clientId)
            val tokenExchangeRequest = resp.createTokenExchangeRequest()
            Log.d("MainActivity", "Token request: ${tokenExchangeRequest.jsonSerializeString()}")
            Log.d("MainActivity", "redirect uri: ${authConfig.redirectUri}")
            authService.performTokenRequest(tokenExchangeRequest, basicAuth) { tokenResponse, tokenException ->
                if (tokenResponse != null) {
                    authState.update(tokenResponse, tokenException)
                    // TODO: persist authState
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
fun MainScreen(
    userPseudo: String,
    onLoginClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val scope = rememberCoroutineScope()
    val activity = MainActivity()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            //Only show the login button if the user is not logged in
            if (userPseudo.isEmpty() && userPseudo != "Anonymous") {
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        onLoginClicked()
                        scope.launch { drawerState.close() }
                    }) {
                    Text("Login")
                }
            }
        },
    ) {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                if (userPseudo.isNotEmpty() && userPseudo != "Anonymous") {
                    Text("Hello $userPseudo!", modifier = Modifier.padding(16.dp))
                } else {
                    Text("Please login", modifier = Modifier.padding(16.dp))
                    LoginButton(onClick = { onLoginClicked() })
                }
            }
        }
    }
}

@Composable
fun LoginButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = { onClick() }) { Text("Login") }
}

class CustomClientAuthentication(private val clientId: String) : ClientAuthentication {
    override fun getRequestHeaders(clientId: String): MutableMap<String, String> {
        return mutableMapOf(
            "Authorization" to "Basic " + Base64.encodeToString("$clientId:".toByteArray(), Base64.NO_WRAP)
        )
    }

    override fun getRequestParameters(clientId: String): Map<String?, String?>? {
        return mutableMapOf()
    }

}