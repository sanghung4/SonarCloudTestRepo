package com.reece.pickingapp.networking

import android.content.Context
import android.os.Looper
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.google.android.material.snackbar.Snackbar
import com.reece.pickingapp.BuildConfig
import com.reece.pickingapp.R
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.SnackBarType
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.utils.extensions.showMessage
import com.reece.pickingapp.view.state.SnackBarState
import com.reece.pickingapp.viewmodel.SignInViewModel
import com.reece.pickingapp.wrappers.OktaWebAuthWrapper
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@Module
@InstallIn(ActivityComponent::class)
class PickingApi @Inject constructor(
    private val activityService: ActivityService,
    private val userPreferences: UserPreferences,
    private val oktaWebAuthWrapper: OktaWebAuthWrapper,
) {
    val TAG = "PickingApi"

    fun setUp() {
        activityService.activity?.let { activity ->
            oktaWebAuthWrapper.createWebAuthClient()
        }
    }

    suspend fun getApolloClient(): ApolloClient? {
        check(Looper.myLooper() == Looper.getMainLooper()) {
            "Only the main thread can get the apolloClient instance"
        }
        val token = oktaWebAuthWrapper.getToken()
        return if (oktaWebAuthWrapper.isAuthenticated() && !token.isNullOrEmpty()) {

            val okHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(AuthorizationInterceptor(token))
                    .build()

            return ApolloClient.builder()
                .serverUrl(BuildConfig.API_BASE_URL)
                .okHttpClient(okHttpClient)
                .build()
        } else {
            activityService.activity?.applicationContext?.let {  context ->
                signOut(context)
            }
            null
        }
    }

    suspend fun isAuthenticated(): Boolean {
        return oktaWebAuthWrapper.isAuthenticated() &&
                !userPreferences.getUsername().isNullOrEmpty() &&
                !userPreferences.getBranch().isNullOrEmpty() &&
                !userPreferences.getEmailId().isNullOrEmpty()
    }

    suspend fun isOktaAuthenticated(): Boolean {
        return oktaWebAuthWrapper.isAuthenticated()
    }

    suspend fun signIn(context: Context, userEmail: String = " "): SignInViewModel.BrowserState {
        return oktaWebAuthWrapper.signIn(contx = context, email = userEmail)
    }


    suspend fun signOut(context: Context) {
        oktaWebAuthWrapper.signOut(context) {
            signOutCallBack()
        }
    }

     fun signOutCallBack(){
        userPreferences.setUsername(emptyString)
        userPreferences.setEmailId(emptyString)
        activityService.navController?.navigate(R.id.action_global_auth_navigation)
    }

    suspend fun getOktaUserName() :String{
        return oktaWebAuthWrapper.getOktaUserName()
    }
    suspend fun getOktaUserEmail() :String{
        return oktaWebAuthWrapper.getOktaUserEmail()
    }

    suspend fun authorizationExpired() {
        activityService.activity?.let { activity -> signOut(activity) }
        activityService.activity?.showMessage(
            SnackBarState(
                SnackBarType.ERROR,
                activityService.activity?.getString(R.string.authorization_expired) ?: emptyString
            ),
            Snackbar.LENGTH_LONG
        )
    }
}

private class AuthorizationInterceptor(val token: String) : Interceptor {
    private val TAG = "AuthInterceptor"

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(TAG, token)

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }
}