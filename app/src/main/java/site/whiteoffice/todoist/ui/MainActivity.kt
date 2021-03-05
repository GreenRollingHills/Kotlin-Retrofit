package site.whiteoffice.todoist.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import com.bumptech.glide.Glide
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.PersistentStorage.getTokenFromSharedPreferences
import site.whiteoffice.todoist.ui.Welcome.Welcome

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val tag = MainActivity::class.java.simpleName



    //private val repo = RepositoryRetriever()

    companion object {
        val secretString = "secretString"
        //var token = ""
        //val accessTokenKey = "accessTokenKey"
        //val sharedPreferenceName = "myPref"

        val todoistActionKey = "todoistActionKey"
        val todoistActionCodeKey = "todoistActionCodeKey"

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("main activity, onCreate")


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostID) as NavHostFragment
        val navController = navHostFragment.navController

        Log.d(tag, "intent : $intent")

        val bundle = Bundle()

        if (intent != null && intent.action == Intent.ACTION_MAIN) {
            //testMethodRemoveToken(application)
            //val tokenStored = Welcome.TodoistAction.getToken()
            val tokenStored =
                getTokenFromSharedPreferences(
                    application
                )
            println("tokenStored : $tokenStored")
            if (tokenStored == null) {
                bundle.putInt(todoistActionKey, Welcome.TodoistAction.getCode.raw)

            } else {
                bundle.putInt(todoistActionKey, Welcome.TodoistAction.start.raw)

            }

        } else if (intent != null && intent.action == Intent.ACTION_VIEW) { //category = BROWSABLE
            bundle.putInt(todoistActionKey, Welcome.TodoistAction.getToken.raw)
            handleIntent(intent, bundle)

        }
        navController.setGraph(R.navigation.nav_graph, bundle)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBarConfiguration)


        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.my_toolbar)
        //setSupportActionBar(toolBar)
        setupWithNavController(toolBar, navController)
        //toolBar.setTitleTextAppearance(this, R.style.sans_serif_style)

        navController.addOnDestinationChangedListener {navCon, des, arg ->
            println("navController, OnDestinationChangedListener")
            println("des id : ${des.id}")
            println("des display name : ${des.label}")
            //toolBar.setNavigationIcon(R.drawable.ic_launcher_background)

            /*if (des.id == R.id.imageDump) {
                updateToolBarTitleForImageDump()
            } else {
                my_toolbar.title = des.label
                my_toolbar.toolBarTextView.text = ""

            }*/

        }

    }

    /*fun updateToolBarTitleForImageDump () {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val lastQuery = sharedPref?.getString(QueryDialog.lastQueryKey, "computer")
        println("lastQuery : $lastQuery")
        if (lastQuery != null) {
            //des.label = "$lastQuery PATENT RESULTS"
            //supportActionBar?.title = "$lastQuery PATENT RESULTS"
            //my_toolbar.title = "${lastQuery.toUpperCase(Locale.ROOT)} PATENT RESULTS 123456789  asdf asdf asdf 123456789 asdf asdf asdf"
            //actionBar?.title = "${lastQuery.toUpperCase(Locale.ROOT)} PATENT RESULTS"
            my_toolbar.title = ""
            //my_toolbar.toolBarTextView.text = "${lastQuery.toUpperCase(Locale.ROOT)} PATENT RESULTS"
            my_toolbar.toolBarTextView.text = "PATENT RESULTS"

        }


    }*/

    private fun handleIntent(intent: Intent, bundle:Bundle) {
        Log.d(tag, "handleIntent")
        Log.d(tag, "intent : $intent")
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        Log.d(tag, "appLinkAction : $appLinkAction")
        Log.d(tag, "appLinkData : $appLinkData")
        if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.getQueryParameter("state").also { state ->
                Log.d(tag, "state : $state")
                if (state == secretString) {
                    appLinkData?.getQueryParameter("code").also { code ->
                        Log.d(tag, "code : $code")
                        if (code != null) {
                            //repo.getToken(code, callbackAccessToken)
                            bundle.putString(todoistActionCodeKey, code)

                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //val navController = findNavController(R.id.fragment)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostID) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    fun clearCache () {
        Thread(Runnable {
            Glide.get(this).clearDiskCache() //1
        }).start()
        Glide.get(this).clearMemory() //2
    }

    /*fun testMethodRemoveToken() {
        val sharedPref = getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
        sharedPref.edit().remove(accessTokenKey).apply()

    }

    fun getToken():String? {

        val sharedPref = getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
        return sharedPref?.getString(accessTokenKey, null)
    }*/



}