package com.reece.pickingapp.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.okta.authfoundation.AuthFoundationDefaults
import com.okta.authfoundation.claims.email
import com.okta.authfoundation.client.OidcClient
import com.okta.authfoundation.client.OidcConfiguration
import com.okta.authfoundation.client.SharedPreferencesCache
import com.okta.authfoundation.credential.CredentialDataSource.Companion.createCredentialDataSource
import com.okta.authfoundationbootstrap.CredentialBootstrap
import com.okta.webauthenticationui.WebAuthenticationClient.Companion.createWebAuthenticationClient
import com.reece.pickingapp.BuildConfig
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.repository.EclipseLoginRepository
import com.reece.pickingapp.repository.UserPreferencesImp
import com.reece.pickingapp.testingUtils.MockActivityService
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.view.state.LoaderState
import com.reece.pickingapp.view.state.ResponseState
import com.reece.pickingapp.wrappers.OktaWebAuthWrapper
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import okhttp3.HttpUrl.Companion.toHttpUrl

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest: DescribeSpec( {
    describe("SignInViewModelTest") {
        lateinit var uut: SignInViewModel
        lateinit var mainThreadSurrogate: ExecutorCoroutineDispatcher
        lateinit var mockEclipseLoginRepository: EclipseLoginRepository
        lateinit var mockActivityService: ActivityService
        lateinit var mockApplication :Application
        lateinit var mockContext: Context
        lateinit var mockOktaWebAuthWrapper: OktaWebAuthWrapper
        lateinit var mockSignInViewModelBrowserState : SignInViewModel.BrowserState
        lateinit var mockActivity: Activity


        @MockK
        lateinit var mockPickingApi : PickingApi
        lateinit var mockUserPreferences: UserPreferencesImp

        beforeEach {
            mainThreadSurrogate = newSingleThreadContext("UI thread")
            Dispatchers.setMain(mainThreadSurrogate)

            mockActivity = mockk {
                every { invalidateOptionsMenu() } returns Unit
            }

            mockActivityService = MockActivityService().create()
            every { mockActivityService.activity } returns mockActivity

            mockContext = mockk()


            mockUserPreferences= mockk{
                every { setEmailId(any()) } returns Unit

            }
            mockPickingApi = mockk()
            mockApplication = mockk()
            mockSignInViewModelBrowserState = mockk()
            mockEclipseLoginRepository = mockk()

            uut = SignInViewModel(
                pickingApi=mockPickingApi,
                userPreferences = mockUserPreferences,
                activityService = mockActivityService,
                eclipseLoginRepository = mockEclipseLoginRepository
            )
            mockOktaWebAuthWrapper = mockk {
                coEvery { getToken() } returns "someToken"
                coEvery { signOut(mockContext){mockPickingApi.signOutCallBack()} } returns Unit
                coEvery { refreshOktaToken() } returns Unit
                coEvery { refreshOktaToken(any(), any()) } returns Unit
            }

        }

        describe("when setUp()"){
            beforeEach {
                lateinit var mockNavigationForAuthScreenCallback: () -> Unit
                var navigationForAuthScreenCallbackWasCalled = false
                mockNavigationForAuthScreenCallback = { navigationForAuthScreenCallbackWasCalled = true }
                coEvery { mockOktaWebAuthWrapper.isAuthenticated() } returns false
                coEvery { mockPickingApi.isOktaAuthenticated() } returns false
                coEvery { mockPickingApi.isAuthenticated() } returns false
                uut.loaderState.value = LoaderState.Default
                uut.setUp(
                    navigationCallback = mockNavigationForAuthScreenCallback,
                )
            }
            it("calls invalidateOptionsMenu()"){
                verify { mockActivity.invalidateOptionsMenu() }
            }
        }


        describe("when .login()"){
            beforeEach {
                coEvery { mockOktaWebAuthWrapper.signIn(any()) } returns mockSignInViewModelBrowserState

                uut.login(mockContext)
            }
            it("calls pickingApi.signIn(context)"){
                coVerify {
                    mockPickingApi.signIn(mockContext)
                }
            }
        }

        describe("when .saveUserData()"){
            beforeEach {
                coEvery { mockPickingApi.getOktaUserEmail() } returns "some_email@reece.com"
                uut.saveUserData()
            }
            it("calls userPreferences.setEmailId"){
                verify {
                    mockUserPreferences.setEmailId(any())
                }
            }
        }

    }

})
