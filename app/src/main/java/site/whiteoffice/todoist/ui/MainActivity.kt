package site.whiteoffice.todoist.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import site.whiteoffice.todoist.BuildConfig
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.PersistentStorage.getTokenFromSharedPreferences
import site.whiteoffice.todoist.PersistentStorage.testMethodRemoveToken
import site.whiteoffice.todoist.ui.Welcome.Welcome
import site.whiteoffice.todoist.ui.Welcome.WelcomeViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val connectivityManager by viewModels<ConnectivityManagerCustom>()


    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val welcomeLoginStatusKey = "welcomeLoginStatusKey"
        const val todoistActionCodeKey = "todoistActionCodeKey"

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostID) as NavHostFragment
        val navController = navHostFragment.navController

        Log.d(TAG, "intent : $intent")

        val bundle = Bundle()

        if (intent != null && intent.action == Intent.ACTION_MAIN) {
            val tokenStored =
                getTokenFromSharedPreferences(
                    application
                )
            if (tokenStored == null) {
                bundle.putInt(welcomeLoginStatusKey, WelcomeViewModel.LoginStatus.NeedCode.raw)

            } else {
                bundle.putInt(welcomeLoginStatusKey, WelcomeViewModel.LoginStatus.HaveToken.raw)

            }

        } else if (intent != null && intent.action == Intent.ACTION_VIEW) { //category = BROWSABLE
            bundle.putInt(welcomeLoginStatusKey, WelcomeViewModel.LoginStatus.NeedTokenActive.raw)
            handleIntent(intent, bundle)

        }
        navController.setGraph(R.navigation.nav_graph, bundle)

        appBarConfiguration = AppBarConfiguration(navController.graph)


        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.my_toolbar)
        setupWithNavController(toolBar, navController)


        lifecycleScope.launch(Dispatchers.Default) {
            Glide.get(applicationContext).clearDiskCache()
        }



    }



    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {

        connectivityManager.setUp()
        connectivityManager.isConnectedToInternetLiveData.observe(this, Observer {
            //TODO : do something interesting with bool value
        })

        return super.onCreateView(parent, name, context, attrs)
    }


    private fun handleIntent(intent: Intent, bundle:Bundle) {
        Log.d(TAG, "handleIntent")
        Log.d(TAG, "intent : $intent")
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        Log.d(TAG, "appLinkAction : $appLinkAction")
        Log.d(TAG, "appLinkData : $appLinkData")
        if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.getQueryParameter("state").also { state ->
                Log.d(TAG, "state : $state")
                val stateStored = BuildConfig.TODOIST_STATE_SECRET
                if (state == stateStored) {
                    appLinkData?.getQueryParameter("code").also { code ->
                        Log.d(TAG, "code : $code")
                        if (code != null) {
                            bundle.putString(todoistActionCodeKey, code)

                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostID) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }







}