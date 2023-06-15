package com.reece.pickingapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.okta.authfoundation.client.OidcClientResult
import com.okta.authfoundation.credential.RevokeTokenType
import com.okta.authfoundationbootstrap.CredentialBootstrap
import com.okta.webauthenticationui.WebAuthenticationClient.Companion.createWebAuthenticationClient
import com.reece.pickingapp.databinding.ActivityMainBinding
import com.reece.pickingapp.networking.ConnectivityService
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.view.ui.*
import com.reece.pickingapp.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var pickingApi: PickingApi

    @Inject
    lateinit var connectivityService: ConnectivityService

    @Inject
    lateinit var utils: Utils

    @Inject
    lateinit var activityService: ActivityService

    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var loginNavController: NavController? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var mainMenu: Menu
    private val userPrefViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setUI()
        setUpNavConfiguration()
        setUpSingletons()

        setNavigationHeaderData()
    }

    private fun setNavigationHeaderData() {
        binding.navigationHeaderInclude.branchId = userPrefViewModel.getBranch() ?: emptyString
        binding.navigationHeaderInclude.userName = userPrefViewModel.getUserName() ?: emptyString
        binding.navigationHeaderInclude.emailId = userPrefViewModel.getEmailId() ?: emptyString

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_for_navigation, menu)
        mainMenu = menu
        handleToolBarIcon()
        handleNavBar()
        return true
    }

    fun handleNavBar(){
        val fragmentName = (navController.currentDestination as FragmentNavigator.Destination).className
        if (fragmentName == LoginFlowFragment::class.java.name ){
            val fragmentContainer = binding.navHostFragment.findViewById<View>(R.id.nested_nav_host_login_fragment)
            var navControllera = Navigation.findNavController(fragmentContainer)
            navControllera.addOnDestinationChangedListener{ _, destination, _ ->
                when (destination.id) {
                    R.id.sign_in_fragment -> {
                        binding.toolBarInclude.appBarLayout.visibility = View.GONE
                        binding.toolBarInclude.toolbar.visibility = View.GONE
                        binding.navView.visibility = View.GONE
                        actionBar?.hide()
                    }
                    else -> {
                        binding.toolBarInclude.appBarLayout.visibility = View.VISIBLE
                        binding.toolBarInclude.toolbar.visibility = View.VISIBLE
                        binding.navView.visibility = View.VISIBLE
                    }
                }

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                activityService.hideKeyboard()
                val fragmentName =
                    (navController.currentDestination as FragmentNavigator.Destination).className
                if (fragmentName != SignInFragment::class.java.name && fragmentName != EclipseLoginFragment::class.java.name && fragmentName != LoginFlowFragment::class.java.name)
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }

    //region App Setup

    private fun setUI() {
        val view = binding.root
        setContentView(view)
    }

    private fun setUpNavConfiguration() {
        binding.navigationHeaderInclude.navVersion.text =
            getString(R.string.app_version, BuildConfig.VERSION_NAME)

        setSupportActionBar(binding.toolBarInclude.toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.findNavController()
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.orders_list_fragment)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.itemIconTintList = null

        navController.addOnDestinationChangedListener { _, destination, _ ->
            setNavigationHeaderData()
            handleToolBarIcon()
        }

        binding.navigationHeaderInclude.conSecond.setOnClickListener {
            Log.d(TAG, "Error Logs")
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            navController.navigate(R.id.error_fragment)

        }
        binding.navigationHeaderInclude.conLogout.setOnClickListener {
            Log.d(TAG, "LOGOUT")
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }

            setNavigationHeaderData()
            this.userPrefViewModel.logoutOfBrowser(this)
        }
    }



    private fun handleToolBarIcon() {
        if (this::mainMenu.isInitialized) {
            val fragmentName =
                (navController.currentDestination as FragmentNavigator.Destination).className
            mainMenu.findItem(R.id.profile).isVisible =
                !(fragmentName == SignInFragment::class.java.name || fragmentName == EclipseLoginFragment::class.java.name
                        || fragmentName == LoginFlowFragment::class.java.name
                        || fragmentName == PickFromOtherLocationsFragment::class.java.name)
        }
    }

    private fun setUpSingletons() {
        activityService.setUp(activity = this, navController,userPrefViewModel.getUserPreference())
        pickingApi.setUp()
        connectivityService.start()
    }




}