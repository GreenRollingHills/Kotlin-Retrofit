package site.whiteoffice.todoist.ui.Welcome

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.whiteoffice.todoist.DataClasses.AuthData
import site.whiteoffice.todoist.Repository.TodoistRepository
import site.whiteoffice.todoist.PersistentStorage.saveToken

class WelcomeViewModel (
    application: Application

) : AndroidViewModel(application) {

    private val tag = WelcomeViewModel::class.java.simpleName
    private var repo: TodoistRepository =
        TodoistRepository(application)


    enum class LoginStatus {
        //InComplete, ProceedYes
        Default, NeedCode, NeedToken, HaveToken

    }

    private val _loginStatus: MutableLiveData<LoginStatus> by lazy {
        MutableLiveData<LoginStatus>().also {
            it.value = LoginStatus.Default
        }
    }

    //val loginStatus = MutableLiveData<LoginStatus>().apply { postValue(LoginStatus.default)}


    /*fun getLoginStatus(): LiveData<LoginStatus> {
        return loginStatus
    }*/

    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

    //    fun setLoginStatusToProceed () {
    fun setLoginStatusToHaveToken() {
        //loginStatus.value = LoginStatus.ProceedYes
        _loginStatus.value = LoginStatus.HaveToken

    }

    fun setLoginStatusToNeedCode() {
        _loginStatus.value = LoginStatus.NeedCode
    }

    /*private val callbackToken = object : Callback<AuthData> {
        override fun onFailure(call: Call<AuthData>?, t: Throwable?) {
            Log.d(tag, "Problem calling todoist API ${t?.message}")
            println("call result : $call")
        }

        override fun onResponse(call: Call<AuthData>?, response: Response<AuthData>?) {
            response?.isSuccessful.let {
                println("response : $response")
                println("response body : ${response?.body()}")


            }

        }
    }*/

    private val callbackAccessToken = object : Callback<AuthData> {
        override fun onFailure(call: Call<AuthData>?, t: Throwable?) {
            Log.d(tag, "Problem calling todoist API ${t?.message}")
            println("call result : $call")
        }

        override fun onResponse(call: Call<AuthData>?, response: Response<AuthData>?) {
            response?.isSuccessful.let {
                println("callbackAccessToken, response : $response")
                println("response body : ${response?.body()}")

                //println("response code : ${response?.code()}")
                //println("response message : ${response?.message()}")

                //println("request : ${call?.request()}")
                //println("request : ${call?.request()?.body()}")
                //println("request : ${call?.request()?.url()}")

                if (response?.isSuccessful!!) {
                    println("isSuccessful")
                    response?.body()?.access_token.let { string ->
                        if (string != null) {
                            //println("token : $string")
                            //MainActivity.token = string
                            //saveToken(string)
                            saveToken(
                                application,
                                string
                            )
                            //loginStatus.value = LoginStatus.ProceedYes
                            _loginStatus.value = LoginStatus.HaveToken

                        }
                    }

                } else {
                    response.errorBody().let { eBody ->
                        if (eBody != null) {
                            println("eBody : ${eBody.string()}")
                        }
                    }

                }

            }

        }
    }

    /*fun saveToken(token:String) {
        val sharedPref = getApplication<Application>().getSharedPreferences(MainActivity.sharedPreferenceName, Context.MODE_PRIVATE)
        sharedPref.edit()?.putString(MainActivity.accessTokenKey, token)?.apply()
    }*/

    fun getToken (code:String) {
        _loginStatus.value = LoginStatus.NeedToken
        repo.getToken(code, callbackAccessToken)
    }

    fun testMethodToSetLoginStatus () {
        //loginStatus.value = LoginStatus.ProceedYes
        _loginStatus.value = LoginStatus.HaveToken

    }


}