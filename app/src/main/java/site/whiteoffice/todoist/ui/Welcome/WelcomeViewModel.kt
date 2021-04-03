package site.whiteoffice.todoist.ui.Welcome

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.whiteoffice.todoist.DataClasses.AuthData
import site.whiteoffice.todoist.Repository.TodoistRepository
import site.whiteoffice.todoist.PersistentStorage.saveToken

class WelcomeViewModel (
    application: Application,
    private val savedStateHandle: SavedStateHandle

) : AndroidViewModel(application) {

    private var repo: TodoistRepository =
        TodoistRepository(application)

    companion object {
        private val TAG = WelcomeViewModel::class.java.simpleName
        private const val loginStatusKey = "loginStatusKey"
        private const val errorMessageStateKey = "errorStatusKey"
    }


    enum class LoginStatus (val raw: Int) {
        Default (0), NeedCode(1), NeedTokenActive (2), NeedTokenButton(3), HaveToken(4)

    }

    /*val filteredData: LiveData<List<String>> =
        savedStateHandle.getLiveData<String>("query").switchMap { query ->
            repository.getFilteredData(query)
        }*/

    //private val _test: MutableLiveData<LoginStatus> =
        //savedStateHandle.getLiveData("loginStatus", LoginStatus.Default)

    private val _loginStatus: MutableLiveData<LoginStatus> by lazy {
        savedStateHandle.getLiveData(loginStatusKey, LoginStatus.Default)
    }

    /*private val _loginStatus: MutableLiveData<LoginStatus> by lazy {
        MutableLiveData<LoginStatus>().also {
            it.value = LoginStatus.Default
        }
    }*/


    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus


    fun setLoginStatus(int: Int) {
        /*_loginStatus.value = LoginStatus.values().firstOrNull {
            it.raw == int
        }*/

        val status = LoginStatus.values().firstOrNull {
            it.raw == int
        }

        savedStateHandle.set(loginStatusKey, status)
    }

    private val _errorMessageState:MutableLiveData<Boolean> by lazy {
        savedStateHandle.getLiveData(errorMessageStateKey, false)
    }

    val errorMessageState:LiveData<Boolean>
    get() = _errorMessageState

    fun setErrorMessageState(boolean: Boolean) {
        savedStateHandle.set(errorMessageStateKey, boolean)
    }

    /*fun setLoginStatusToHaveToken() {
        _loginStatus.value = LoginStatus.HaveToken

    }

    fun setLoginStatusToNeedCode() {
        _loginStatus.value = LoginStatus.NeedCode
    }*/


    private val callbackAccessToken = object : Callback<AuthData> {
        override fun onFailure(call: Call<AuthData>?, t: Throwable?) {
            Log.d(TAG, "Problem calling todoist API ${t?.message}")
            println("call result : $call")
            //TODO : implement error handling
            _errorMessageState.value = true
            _loginStatus.value = LoginStatus.NeedTokenButton

        }

        override fun onResponse(call: Call<AuthData>?, response: Response<AuthData>?) {
            response?.isSuccessful.let {
                println("callbackAccessToken, response : $response")
                println("response body : ${response?.body()}")
                var error = true

                if (response?.isSuccessful!!) {
                    println("isSuccessful")
                    response?.body()?.access_token.let { string ->
                        if (string != null) {

                            saveToken(
                                application,
                                string
                            )
                            error = false
                            _loginStatus.value = LoginStatus.HaveToken

                        }
                    }

                    if (error) {
                        _errorMessageState.value = true
                        _loginStatus.value = LoginStatus.NeedTokenButton
                    }

                } else {
                    _errorMessageState.value = true
                    _loginStatus.value = LoginStatus.NeedTokenButton

                    response.errorBody().let { eBody ->
                        if (eBody != null) {
                            println("eBody : ${eBody.string()}")
                        }
                    }

                }

            }

        }
    }

    fun getToken (code:String) {
        //_loginStatus.value = LoginStatus.NeedToken
        //repo.getToken(code, callbackAccessToken)
        //TODO : implement error handling per app requirements

        val errorHandler = CoroutineExceptionHandler { _, exception ->

            _errorMessageState.value = true
            _loginStatus.value = LoginStatus.NeedTokenButton

        }

        viewModelScope.launch(errorHandler) {
            val response = repo.getToken(code)
            response.body()?.let { authData ->
                saveToken(
                    getApplication(),
                    authData.access_token
                )
                _loginStatus.value = LoginStatus.HaveToken
            }


        }

    }



}