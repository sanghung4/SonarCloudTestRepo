package com.reece.pickingapp.wrappers

import android.content.Context
import android.util.Log
import com.okta.authfoundation.claims.email
import com.okta.authfoundation.claims.name
import com.okta.authfoundation.client.OidcClientResult
import com.okta.authfoundation.credential.RevokeTokenType
import com.okta.authfoundationbootstrap.CredentialBootstrap
import com.okta.webauthenticationui.WebAuthenticationClient
import com.okta.webauthenticationui.WebAuthenticationClient.Companion.createWebAuthenticationClient
import com.reece.pickingapp.BuildConfig

import com.reece.pickingapp.viewmodel.SignInViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

class OktaWebAuthWrapper {
    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }
    lateinit var webAuthClient: WebAuthenticationClient

   fun createWebAuthClient() {
        webAuthClient = CredentialBootstrap.oidcClient.createWebAuthenticationClient()
    }


    suspend fun getToken(): String? {
        return CredentialBootstrap.defaultCredential().token?.accessToken
    }

    suspend fun refreshOktaToken(onSuccessCallback: () -> Unit ?, onErrorCallback: () -> Unit){
        if (CredentialBootstrap.defaultCredential().token != null
            && CredentialBootstrap.defaultCredential().getAccessTokenIfValid() == null) {
            // The access_token expired, refresh the token.
            when (val result = CredentialBootstrap.defaultCredential().refreshToken()) {
                is OidcClientResult.Error -> {
                    // An error occurred. Access the error in `result.exception`.
                    onErrorCallback()
                }
                is OidcClientResult.Success -> {
                    // Token refreshed successfully.
                    onSuccessCallback()
                }
            }
        }
    }


    suspend fun refreshOktaToken(){
        if (CredentialBootstrap.defaultCredential().token != null
            && CredentialBootstrap.defaultCredential().getAccessTokenIfValid() == null) {
            // The access_token expired, refresh the token.
            when (val result = CredentialBootstrap.defaultCredential().refreshToken()) {
                is OidcClientResult.Error -> {
                    // An error occurred. Access the error in `result.exception`.
                }
                is OidcClientResult.Success -> {
                    // Token refreshed successfully.
                }
            }
        }
    }


/*
    suspend fun tokenIsValid(
        token: String,
        type: TokenType,
    ): Boolean =
        suspendCancellableCoroutine { continuation ->
            webAuthClient.sessionClient.introspectToken(
                token,
                type.hint,
                object : RequestCallback<IntrospectInfo?, AuthorizationException?> {
                    override fun onSuccess(result: IntrospectInfo) {
                        if (result.isActive && result.exp.asTimeIsValid()) {
                            continuation.resume(result.isActive)
                        } else {
                            GlobalScope.launch {
                                continuation.resume(refreshTokens())
                            }
                        }
                    }

                    override fun onError(error: String?, exception: AuthorizationException?) {
                        continuation.resume(false)
                    }
                }
            )
        }
*/

/*
    suspend fun refreshTokens(): Boolean =
        suspendCancellableCoroutine { continuation ->
            webAuthClient.sessionClient.refreshToken(
                object : RequestCallback<Tokens, AuthorizationException?> {
                    override fun onSuccess(result: Tokens) {
                        continuation.resume(true)
                    }

                    override fun onError(error: String?, exception: AuthorizationException?) {
                        continuation.resume(false)
                    }
                }
            )
        }
*/


    suspend fun isAuthenticated(): Boolean {
        refreshOktaToken()
        return (CredentialBootstrap.defaultCredential().getValidAccessToken() != null)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun signIn(contx: Context, email: String = " "): SignInViewModel.BrowserState {

        val result = CredentialBootstrap.oidcClient.createWebAuthenticationClient().login(
            context = contx,
            extraRequestParameters = mapOf("login_hint" to email),
            redirectUrl = BuildConfig.SIGN_IN_REDIRECT_URI,
        )
        var browserState: SignInViewModel.BrowserState
        when (result) {
            is OidcClientResult.Error -> {
                Log.e(TAG,result.exception.toString())
                browserState=  SignInViewModel.BrowserState.currentCredentialState("Failed to login.")
            }
            is OidcClientResult.Success -> {
                CredentialBootstrap.defaultCredential().storeToken(token = result.result)
                browserState=  SignInViewModel.BrowserState.LoggedIn.create()
            }
        }
        return browserState
    }

    suspend fun signOut(context: Context, signOutCallBack: () -> Unit){
        if (!CredentialBootstrap.defaultCredential().token?.idToken.isNullOrEmpty()){
            CredentialBootstrap.defaultCredential().token?.idToken.let {
                val result = CredentialBootstrap.oidcClient.createWebAuthenticationClient().logoutOfBrowser(
                    context = context,
                    redirectUrl = BuildConfig.SIGN_OUT_REDIRECT_URI,
                    CredentialBootstrap.defaultCredential().token?.idToken ?: "",
                )
                when (result) {
                    is OidcClientResult.Error -> {
                        Log.e(TAG, "Error to logout from Okta: " + result.exception)

                    }
                    is OidcClientResult.Success -> {
                        Log.d(TAG, "Okta Logout Success " + result.result)
                    }
                    else -> {
                        Log.e(TAG, "Error to logout from Okta: $result")
                    }
                }
            }
        }
        Log.d(TAG, "Okta Signed out - Revoking Token")
        CredentialBootstrap.defaultCredential().revokeToken(RevokeTokenType.ACCESS_TOKEN)
        CredentialBootstrap.defaultCredential().delete()
        signOutCallBack()
    }

    suspend fun getOktaUserName() : String{
        return CredentialBootstrap.defaultCredential().idToken()?.name ?: ""
    }

    suspend fun getOktaUserEmail() : String{
        return CredentialBootstrap.defaultCredential().idToken()?.email ?: ""
    }

    companion object {
        const val PREF_STORAGE_WEB = "web_client"
    }
}