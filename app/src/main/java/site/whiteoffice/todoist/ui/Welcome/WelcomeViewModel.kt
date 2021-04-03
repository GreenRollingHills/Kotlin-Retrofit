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


    private val _loginStatus: MutableLiveData<LoginStatus> by lazy {
        savedStateHandle.getLiveData(loginStatusKey, LoginStatus.Default)
    }



    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus


    fun setLoginStatus(int: Int) {

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



    fun getToken (code:String) {

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