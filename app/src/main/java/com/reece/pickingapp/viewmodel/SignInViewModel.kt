package com.reece.pickingapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okta.authfoundation.claims.name
import com.okta.authfoundationbootstrap.CredentialBootstrap
import com.reece.pickingapp.BuildConfig
import com.reece.pickingapp.R
import com.reece.pickingapp.interfaces.DataEntryInterface
import com.reece.pickingapp.models.EclipseCredentialModel
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.repository.EclipseLoginRepository
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.view.state.LoaderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SignInViewModel @Inject constructor(
    private val pickingApi: PickingApi,
    private val userPreferences: UserPreferences,
    private val eclipseLoginRepository : EclipseLoginRepository,
    private val activityService: ActivityService) : ViewModel(), DataEntryInterface {
    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }

    //region LiveData Vars
    private var clickedUser: EclipseCredentialModel? = null

    val loaderState = MutableLiveData<LoaderState>()

    private val _state = MutableLiveData<BrowserState>(BrowserState.Loading)
    val state: LiveData<BrowserState> = _state

    private val _usersState = MutableStateFlow<List<EclipseCredentialModel>> (emptyList())
    val credentialList = _usersState.asStateFlow()

    private val _versionNumber = MutableLiveData<String>()
    val versionNumber: LiveData<String>
        get() = _versionNumber
    //endregion

    init {
        viewModelScope.launch {
            _state.value = BrowserState.currentCredentialState()
        }
        _versionNumber.value =
            activityService.getString(R.string.app_version, BuildConfig.VERSION_NAME)
    }

    fun login(context: Context) {
        login(context = context, email = " ")
    }
    fun login(context: Context, email:String = " ") {
        viewModelScope.launch {
            _state.value = BrowserState.Loading
            _state.value = pickingApi.signIn(context, email)
        }
    }

    lateinit var navigateToEclipseLoginCallback: () -> Unit

    fun setUp(
        navigationCallback: () -> Unit
    ) {
        navigateToEclipseLoginCallback = navigationCallback
        activityService.activity?.invalidateOptionsMenu()
        getUserCredentials()
    }

    suspend fun isRegisteredUser():EclipseCredentialModel?{
        return eclipseLoginRepository.getCredentialByEmail(pickingApi.getOktaUserEmail())
    }


    fun saveUserData (eclipseCredentialModel: EclipseCredentialModel? = null){
        viewModelScope.launch {
            userPreferences.setEmailId(pickingApi.getOktaUserEmail())
        }
        eclipseCredentialModel?.let {
            userPreferences.setUsername(eclipseCredentialModel.username)
            //TODO: Save branch ID too
        }
    }

    fun setClickedUser (user: EclipseCredentialModel){
        clickedUser = user
    }

    fun getClickedUser(): EclipseCredentialModel?{
        return this.clickedUser
    }

    fun getUserCredentials(){
        viewModelScope.launch(Dispatchers.IO) {
            eclipseLoginRepository.getCredentials().distinctUntilChanged()
                .collect { listOfCredentials ->
                    if (listOfCredentials.isNullOrEmpty()) {
                        _usersState.value = emptyList()
                    } else {
                        _usersState.value = listOfCredentials
                    }

                }
        }
    }

    fun removeCredential(credential: EclipseCredentialModel) = viewModelScope.launch {
        eclipseLoginRepository.deleteCredential(credential)
    }

    suspend fun signOutUser(context: Context){
        pickingApi.signOut(context)
    }


    //endregion
    sealed class BrowserState {
        object Loading : BrowserState()
        class LoggedOut(val errorMessage: String? = null) : BrowserState()
        class LoggedIn private constructor(
            val name: String,
            val errorMessage: String?
        ) : BrowserState() {
            companion object {
                /**
                 * Creates the [LoggedIn] state using the [CredentialBootstrap.defaultCredential]s ID Token name claim.
                 */
                suspend fun create(errorMessage: String? = null): BrowserState {
                    val credential = CredentialBootstrap.defaultCredential()
                    val name = credential.idToken()?.name ?: ""
                    return LoggedIn(name, errorMessage)
                }
            }
        }

        companion object {
            /**
             * Creates the [BrowserState] given the [CredentialBootstrap.defaultCredential]s presence of a token.
             *
             * @return Either [LoggedIn] or [LoggedOut].
             */
            suspend fun currentCredentialState(errorMessage: String? = null): BrowserState {
                val credential = CredentialBootstrap.defaultCredential()
                return if (credential.token == null) {
                    LoggedOut(errorMessage)
                } else {
                    LoggedIn.create(errorMessage)
                }
            }
        }
    }
}