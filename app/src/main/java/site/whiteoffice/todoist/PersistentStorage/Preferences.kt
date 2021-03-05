package site.whiteoffice.todoist.PersistentStorage

import android.app.Activity
import android.app.Application
import android.content.Context

const val accessTokenKey = "tokenKey"
const val lastQueryKey = "lastQueryKey"

const val preferenceNameForToken = "preferenceNameForToken"


fun getTokenFromSharedPreferences(application: Application):String? {

    val sharedPref = application.getSharedPreferences(preferenceNameForToken, Context.MODE_PRIVATE)
    return sharedPref?.getString(accessTokenKey, null)

}

fun saveToken(application: Application, token:String) {
    val sharedPref = application.getSharedPreferences(preferenceNameForToken, Context.MODE_PRIVATE)
    sharedPref.edit()?.putString(accessTokenKey, token)?.apply()
}

fun testMethodRemoveToken(application: Application) {
    println("testMethodRemoveToken")
    val sharedPref = application.getSharedPreferences(preferenceNameForToken, Context.MODE_PRIVATE)
    sharedPref.edit().remove(accessTokenKey).apply()

}




fun getLastQuery (activity:Activity?):String? {
    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
    return sharedPref?.getString(lastQueryKey, "computer")

}

fun setLastQuery (activity:Activity?, query:String) {
    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
    sharedPref?.edit()?.putString(lastQueryKey, query)?.apply()
}



