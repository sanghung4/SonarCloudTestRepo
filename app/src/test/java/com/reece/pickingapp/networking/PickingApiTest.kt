package com.reece.pickingapp.networking

import android.app.Activity
import android.os.Looper
import android.util.Log
import androidx.navigation.NavController
import com.apollographql.apollo.ApolloClient
import com.google.android.material.snackbar.Snackbar
import com.okta.authfoundation.client.OidcClientResult
import com.okta.authfoundation.credential.Token
import com.okta.authfoundationbootstrap.CredentialBootstrap
import com.okta.webauthenticationui.WebAuthenticationClient.Companion.createWebAuthenticationClient
import com.reece.pickingapp.BuildConfig
import com.reece.pickingapp.R
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.testingUtils.ExtensionMock
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.utils.extensions.showMessage
import com.reece.pickingapp.viewmodel.SignInViewModel
import com.reece.pickingapp.wrappers.OIDCConfigWrapper
import com.reece.pickingapp.wrappers.OktaWebAuthWrapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import android.content.Context
import com.okta.webauthenticationui.WebAuthenticationClient
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class PickingApiTest : DescribeSpec({
    describe("PickingApi") {
        lateinit var uut: PickingApi
        lateinit var mockActivityService: ActivityService
        lateinit var mockUserPreferences: UserPreferences
        lateinit var mockOktaWebAuthWrapper: OktaWebAuthWrapper
        lateinit var mockCredentialBootstrap: CredentialBootstrap
        lateinit var mockSignInViewModelBrowserState : SignInViewModel.BrowserState
        val actionGlobalAuthNavigationValue = R.id.action_global_auth_navigation
        lateinit var mockContext:Context

        lateinit var mockWebAuthenticationClient: WebAuthenticationClient


        lateinit var mockActivity: Activity
        lateinit var mockNavController: NavController
        lateinit var mockOidcClientResult:OidcClientResult<Unit>
        lateinit var mockOidcClientResultWithToken:OidcClientResult<Token>
        lateinit var mockPickingApi: PickingApi

        beforeEach {
            mockPickingApi = mockk()

            mockUserPreferences = mockk{
                every { setUsername(any()) } returns Unit
                every { setEmailId(any()) } returns Unit
                every { setBranch(any()) } returns Unit

            }
            ExtensionMock.activityExtensions()

            mockWebAuthenticationClient = mockk{

            }
            mockSignInViewModelBrowserState = mockk()

            mockContext = mockk()
            mockActivity = mockk {
                every { showMessage(any(), any()) } returns Unit
                every { applicationContext } returns mockContext
                every { getString(any()) } returns "SomeString"
            }

            mockOidcClientResult = mockk()
            mockOidcClientResultWithToken = mockk()

            mockCredentialBootstrap = mockk{
                //coEvery { oidcClient.createWebAuthenticationClient() } returns mockWebAuthenticationClient
                coEvery { defaultCredential().token?.idToken } returns ""
                //coEvery { defaultCredential().getValidAccessToken() } returns "someValidToken"
            }

            mockkStatic(Activity::class)


            mockNavController = mockk{
                every { navigate(actionGlobalAuthNavigationValue) } returns Unit
            }

            mockActivityService = mockk {
                every { activity } returns mockActivity
                every { navController } returns mockNavController
            }

            mockOktaWebAuthWrapper = mockk {
                coEvery { getToken() } returns "someToken"
                coEvery { getOktaUserName() } returns "someName"
                coEvery { getOktaUserEmail() } returns "someName@reece.com"
                coEvery { signOut(context=mockContext, signOutCallBack= any() )  } returns Unit
                coEvery { signOut(context=mockActivity, signOutCallBack= any() )  } returns Unit
                coEvery { refreshOktaToken() } returns Unit
                coEvery { refreshOktaToken(any(), any()) } returns Unit
            }

            uut = PickingApi(
                mockActivityService,
                mockUserPreferences,
                mockOktaWebAuthWrapper
            )


        }

        describe("when .setUp()"){
            beforeEach {
                every{mockOktaWebAuthWrapper.createWebAuthClient()} returns Unit
                uut.setUp()
            }

            it("calls oktaWebAuthWrapper.createWebAuthClient()"){
                verify { mockOktaWebAuthWrapper.createWebAuthClient() }
            }
        }

        describe("when isOktaAuthenticated()"){
            var result: Boolean = false
            beforeEach {
                coEvery{mockOktaWebAuthWrapper.isAuthenticated()} returns true
                result=uut.isOktaAuthenticated()
            }
            it("calls oktaWebAuthWrapper.isAuthenticated()"){
                coVerify {mockOktaWebAuthWrapper.isAuthenticated()}
            }
            describe("oktaWebAuthWrapper.isAuthenticated() returns true"){
                it("isOktaAuthenticated() returns true"){
                    result shouldNotBe false
                }
            }

            describe("oktaWebAuthWrapper.isAuthenticated() returns fase"){
                beforeEach {
                    coEvery{mockOktaWebAuthWrapper.isAuthenticated()} returns false
                    result=uut.isOktaAuthenticated()
                }
                it("isOktaAuthenticated() returns false"){
                    result shouldNotBe true
                }
            }


        }

        describe("when .getOktaUserEmail()"){
            beforeEach {
                coEvery { mockOktaWebAuthWrapper.getOktaUserEmail() } returns "some_email@reece.com"
                uut.getOktaUserEmail()

            }

            it("calls mockOktaWebAuthWrapper"){
                coVerify { mockOktaWebAuthWrapper.getOktaUserEmail() }
            }

        }

        describe("when .getOktaUserName()"){
            beforeEach {
                coEvery { mockOktaWebAuthWrapper.getOktaUserName() } returns "Jon Doe"
                uut.getOktaUserName()

            }

            it("calls mockOktaWebAuthWrapper"){
                coVerify { mockOktaWebAuthWrapper.getOktaUserName() }
            }

        }

        describe("when .signOutCallBack()"){
            beforeEach {
                uut.signOutCallBack()
                //every { mockActivityService.navController?.navigate(R.id.action_global_auth_navigation) } returns Unit
            }

            it("resets user data"){
                verify {
                    mockUserPreferences.setUsername("")
                    mockUserPreferences.setEmailId("")
                }
            }

        }

        describe("when getOktaUserName()"){
            beforeEach {
                uut.getOktaUserName()
            }
            it("calls mockOktaWebAuthWrapper"){
                coVerify {
                    mockOktaWebAuthWrapper.getOktaUserName()
                }
            }
        }

        describe("when getOktaUserEmail()"){
            beforeEach {
                uut.getOktaUserEmail()
            }
            it("calls mockOktaWebAuthWrapper"){
                coVerify {
                    mockOktaWebAuthWrapper.getOktaUserEmail()
                }
            }
        }



        describe("when .getApolloClient()") {
            var result: ApolloClient? = null

            beforeEach {
                val mockLooper: Looper = mockk()
                mockkStatic(Looper::class)
                every { Looper.myLooper() } returns mockLooper
                every { Looper.getMainLooper() } returns mockLooper

            }

            describe("and the auth token is valid") {
                beforeEach {
                    coEvery { mockOktaWebAuthWrapper.isAuthenticated() } returns true
                    coEvery { mockOktaWebAuthWrapper.getToken() } returns "someToken"
                    result = uut.getApolloClient()
                }

                it("returns an ApolloClient") {
                    result shouldNotBe null
                }

                describe("that ApolloClient") {
                    it("has a serverUrl of BuildConfig.API_BASE_URL") {
                        result?.serverUrl.toString() shouldBe BuildConfig.API_BASE_URL
                    }
                }
            }


            describe("and the auth token is not valid") {
                beforeEach {
                    coEvery { mockOktaWebAuthWrapper.isAuthenticated() } returns false
                    coEvery { mockOktaWebAuthWrapper.getToken()} returns ""
                    every { mockActivity.getString(R.string.authorization_expired) } returns "mockString"
                    result = uut.getApolloClient()
                }

                describe("when .authorizationExpired()") {
                    describe("when .signOut()") {
                        beforeEach {
                            uut.signOut(mockContext)
                        }
                        it("calls .webAuth.signOut()") {
                            coVerify {
                                mockOktaWebAuthWrapper.signOut(mockContext, any())
                            }
                        }
                    }

                }

                it("returns a null value") {
                    result shouldBe null
                }
            }

        }

        describe("when .isAuthenticated()") {
            var result: Boolean? = null

            describe("when sessionClient.isAuthenticated = true AND userPreferences.getUsername() has a value AND userPreferences.getBranch() has a value AND userPreferences.getEmailId() has a value") {
                beforeEach {
                    coEvery { mockOktaWebAuthWrapper.isAuthenticated() } returns true
                    every { mockUserPreferences.getUsername() } returns "SomeUsername"
                    every { mockUserPreferences.getBranch() } returns "1234"
                    every { mockUserPreferences.getEmailId() } returns "example@reece.com"
                    result = uut.isAuthenticated()
                }

                it("returns a value of true") {
                    result shouldBe true
                }
            }

            describe("when sessionClient.isAuthenticated = false") {
                beforeEach {
                    coEvery { mockOktaWebAuthWrapper.isAuthenticated() } returns false
                    every { mockUserPreferences.getUsername() } returns "someUsername"
                    every { mockUserPreferences.getBranch() } returns "someBranchId"
                    every { mockUserPreferences.getEmailId() } returns "someEmailId"
                    result = uut.isAuthenticated()
                }

                it("returns a value of false") {
                    result shouldBe false
                }
            }

            describe("when userPreferences.getUsername() is empty") {
                beforeEach {
                    coEvery { mockOktaWebAuthWrapper.isAuthenticated() } returns true
                    every { mockUserPreferences.getUsername() } returns ""
                    every { mockUserPreferences.getBranch() } returns "someBranchId"
                    every { mockUserPreferences.getEmailId() } returns "someEmailId"
                    result = uut.isAuthenticated()
                }

                it("returns a value of false") {
                    result shouldBe false
                }
            }
            describe("when userPreferences.getEmailId() is empty") {
                beforeEach {
                    coEvery { mockOktaWebAuthWrapper.isAuthenticated() } returns true
                    every { mockUserPreferences.getEmailId() } returns ""
                    every { mockUserPreferences.getUsername() } returns "someUserName"
                    every { mockUserPreferences.getBranch() } returns "someBranchId"
                    result = uut.isAuthenticated()
                }

                it("returns a value of false") {
                    result shouldBe false
                }
            }
            describe("when userPreferences.getBranch() is empty") {
                beforeEach {
                    coEvery { mockOktaWebAuthWrapper.isAuthenticated() } returns true
                    every { mockUserPreferences.getUsername() } returns "someUserName"
                    every { mockUserPreferences.getEmailId() } returns "someEmailId"
                    every { mockUserPreferences.getBranch() } returns ""
                    result = uut.isAuthenticated()
                }

                it("returns a value of false") {
                    result shouldBe false
                }
            }
        }



        describe("when .signIn()") {

            beforeEach {
                coEvery { mockOktaWebAuthWrapper.signIn(any()) } returns mockSignInViewModelBrowserState
                uut.signIn(mockContext)
            }

            it("calls .webAuth.signIn()") {
                coVerify { mockOktaWebAuthWrapper.signIn(mockContext) }
            }
        }
        describe("when .signOut()") {
            beforeEach {
                uut.signOut(mockContext)
            }

            it("calls mockOktaWebAuthWrapper.signOut()") {
                coVerify {
                    mockOktaWebAuthWrapper.signOut(mockContext, any())
                }
            }
        }

        describe("when authorizationExpired()"){
            beforeEach {
                uut.authorizationExpired()
            }

            it("calls activityService.signOut()"){
                coVerify {
                    mockOktaWebAuthWrapper.signOut(any(), any())
                }
            }
        }
    }
})