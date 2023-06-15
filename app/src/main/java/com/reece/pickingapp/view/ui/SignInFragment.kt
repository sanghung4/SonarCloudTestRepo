package com.reece.pickingapp.view.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.okta.authfoundationbootstrap.CredentialBootstrap
import com.reece.pickingapp.R
import com.reece.pickingapp.databinding.FragmentSignInBinding
import com.reece.pickingapp.models.EclipseCredentialModel
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.SnackBarType
import com.reece.pickingapp.utils.extensions.showMessage
import com.reece.pickingapp.view.state.SnackBarState
import com.reece.pickingapp.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel by viewModels<SignInViewModel>()
    var TAG = "SignInFragment"
    @Inject
    lateinit var activityService: ActivityService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setUp(navigationCallback = { navigateToEclipseLogin() })
        binding = FragmentSignInBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.userList.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val list = viewModel.credentialList.collectAsState().value
                viewModel.saveUserData()
                // In Compose world
                UserListComposeView(
                    users = list,
                    onDeleteCredential = { viewModel.removeCredential(it) },
                    onRowClick = { context, email ->
                        viewModel.login(context= context, email=email)}
                ) { viewModel.setClickedUser(it) }
            }
        }

        binding.signInButton.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LoginWithOktaButton(
                    onRowClick = {viewModel.login(it)},
                )
            }

        }
        /**
         * Update the user interface for changes in the sign-in flow.
         *
         * Use an observer to react to updates in [BrowserState]. Updates are asynchronous and are triggered both by user actions,
         * such as button clicks, and completing the flow.
         */
        viewModel.state.observe(this) { state ->
            when (state) {
                is SignInViewModel.BrowserState.LoggedIn -> {
                    binding.progressBarSignin.visibility = View.GONE
                    Log.d(TAG, "state is SignInViewModel.BrowserState.LoggedIn")
                    lifecycleScope.launch {
                        if (CredentialBootstrap.defaultCredential().getValidAccessToken() != null) {
                            Log.d(
                                TAG,
                                "CredentialBootstrap.defaultCredential().getValidAccessToken()!=null " +
                                        "${CredentialBootstrap.defaultCredential().token?.idToken}"
                            )
                        }
                    }

                    if (state.errorMessage == null) {
                        //send navigation
                        lifecycleScope.launch {
                            val registeredUser = viewModel.isRegisteredUser()
                            if (registeredUser != null){
                                if(registeredUser.username.isEmpty()){
                                    navigateToEclipseLogin()
                                }else {
                                    if ((viewModel.getClickedUser() == registeredUser)) {
                                        //User clicked is the user logged in
                                        viewModel.saveUserData(registeredUser)
                                        navigateToEclipseLogin()
                                    } else {
                                        //Ask the user to login in their own eclipse credential
                                        activityService.showMessage(
                                            SnackBarState(
                                                SnackBarType.ERROR,
                                                activityService.getString(R.string.error_wrong_okta_user)
                                            ),
                                            Snackbar.LENGTH_LONG
                                        )
                                        context?.let { viewModel.signOutUser(context = it) }
                                    }
                                }
                            }else{
                                //First time User
                                viewModel.saveUserData()
                                navigateToEclipseLogin()
                            }

                        }


                    } else {
                        //show a snack
                        showErrorMessage(state.errorMessage)
                    }
                }

                is SignInViewModel.BrowserState.LoggedOut -> {
                    binding.progressBarSignin.visibility = View.GONE
                    binding.signInButton.isEnabled = true
                    if (state.errorMessage != null) {
                        showErrorMessage(state.errorMessage)
                    }
                }

                SignInViewModel.BrowserState.Loading -> {
                    binding.progressBarSignin.visibility = View.VISIBLE
                    binding.signInButton.isEnabled = false
                }
            }
        }
        return binding.root
    }

    private fun navigateToEclipseLogin() {
        Log.d("SignInFragment", "navigateToEclipseLogin()")
        findNavController().navigate(SignInFragmentDirections.navigateToEclipseLogin())
    }

    private fun showErrorMessage(message: String) {
        activity?.showMessage(
            SnackBarState(
                SnackBarType.ERROR,
                message
            ),
            Snackbar.LENGTH_LONG
        )
    }
}



@Composable
private fun UserListComposeView(
    users: List<EclipseCredentialModel>,
    onDeleteCredential: (EclipseCredentialModel) -> Unit,
    onRowClick: (Context, String) -> Unit,
    saveUserClick: (EclipseCredentialModel) -> Unit
) {
    Column (modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        var reeceLogoPaddingTop = dimensionResource(id = R.dimen.margin_reece_logo_solo)
        if (users.isNotEmpty()){
            reeceLogoPaddingTop = dimensionResource(id = R.dimen.margin_reece_logo_with_List)
        }

        Image(
            painter = painterResource(id = R.drawable.reece_logo),
            contentDescription = "",
            modifier = Modifier.padding(top=reeceLogoPaddingTop, bottom = dimensionResource(id = R.dimen.margin_large))
        )
        
        Text(
            color = colorResource(id = R.color.morsco_blue_text_color_1),
            fontWeight = FontWeight.W600,
            fontSize = 22.sp,
            text = stringResource(id = R.string.app_name),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.margin_xlarge))
        
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(users, itemContent = {thisUser ->
                UserRow(
                    user = thisUser,
                    onDeleteCredential = onDeleteCredential,
                    onRowClick = onRowClick,
                    saveUserClick = saveUserClick
                )
                Divider(
                    color = colorResource(id = R.color.morsco_primary_text_color),
                    thickness = 1.dp
                )
            })
        }

    }
}

@Preview
@Composable
private fun UserRow(
    @PreviewParameter(SampleEclipseCredentialModelProvider::class)
    user: EclipseCredentialModel,
    onDeleteCredential: (EclipseCredentialModel) -> Unit = {},
    onRowClick: (Context, String) -> Unit = { context: Context, s: String -> },
    saveUserClick: (EclipseCredentialModel) -> Unit = {},
    ) {
        val context = LocalContext.current

        Row(
            modifier = Modifier
                .wrapContentHeight(CenterVertically)
                .fillMaxWidth()
                .height(56.dp)
                .background(color = Color.White)
                .clickable {
                    onRowClick(context, user.email)
                    saveUserClick(user)
                },
        ) {
           if(!user.username.isNullOrEmpty()){
               UserProfilePick(user.username.substring(0, 1))
           }else{
               UserProfilePick("")
           }
            UserInformation(user.username, user.email)
            Spacer(modifier = Modifier.weight(1.0F))
            RemoveButton(onDeleteCredential = onDeleteCredential, user = user)
        }
}

@Composable
private fun UserInformation(name: String? = "LA00213", mail: String? = "yair.rodriguez@reece.com") {
    Column() {
        Spacer(modifier = Modifier.weight(1.0F))
        Text(
            text = name ?: "",
            color = colorResource(id = R.color.morsco_primary_text_color),
            fontWeight = FontWeight.W500,
            fontSize = 16.sp

        )
        Text(
            text = mail ?: "",
            color = colorResource(id = R.color.morsco_primary_text_color),
            fontWeight = FontWeight.W400,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1.0F))
    }
}

@Composable
private fun RemoveButton(
    onDeleteCredential: (EclipseCredentialModel) -> Unit = {},
    user: EclipseCredentialModel,
) {
    TextButton(onClick = {
        onDeleteCredential(user)
    }) {
        Text(
            text = "Remove",
            color = colorResource(id = R.color.morsco_blue_1),
            style = TextStyle(textDecoration = TextDecoration.Underline),
            fontWeight = FontWeight.W500,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun UserProfilePick(initial: String = "Y") {
    Column() {
        Spacer(modifier = Modifier.weight(1.0F))
        Surface(modifier = Modifier.padding(vertical = 16.dp, horizontal = 9.dp)) {
            Surface(
                modifier = Modifier.size(24.dp),
                shape = CircleShape,
                color = colorResource(id = R.color.profile_pic_color)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = initial,
                        color = colorResource(id = R.color.morsco_primary_text_color),
                        fontWeight = FontWeight.W400,
                        fontSize = 12.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1.0F))
    }

}

@Preview
@Composable
private fun LoginWithOktaButton(onRowClick: (Context) -> Unit = {},){
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.morsco_blue_1)),
        shape = RoundedCornerShape(2.dp),
        onClick = {
            onRowClick(context)

    }) {

        Canvas(
            modifier = Modifier
                .size(size = 16.dp)
                .fillMaxHeight()
        ) {
            drawCircle(
                color = Color.White,
                style = Stroke(width = 3.dp.toPx())

            )
        }
        Text(
            modifier= Modifier.padding(start = 6.dp),
            text = stringResource(id = R.string.login_button),
            color = colorResource(id = R.color.white),
            fontWeight = FontWeight.W500,
            fontSize = 15.sp,
            lineHeight = 19.sp
        )
    }


}


class SampleEclipseCredentialModelProvider : PreviewParameterProvider<EclipseCredentialModel> {
    override val values = sequenceOf(
        EclipseCredentialModel(
            UUID.randomUUID(),
            "WMSAPPDV",
            "test@reece.com",
            "aes",
            true,
            "asdf"
        ),
        EclipseCredentialModel(
            UUID.randomUUID(),
            "RSCBIA",
            "pi_test1@dialexa.com",
            "aes",
            true,
            "asdf"
        ),
    )
}
