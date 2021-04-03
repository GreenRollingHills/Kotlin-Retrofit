package site.whiteoffice.todoist.ui.ImageDump

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.whiteoffice.todoist.DataClasses.PatentIDResults
import site.whiteoffice.todoist.Repository.NASARepository

class PatentSummaryViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
): AndroidViewModel(application) {


    companion object {
        private val TAG = PatentSummaryViewModel::class.java.simpleName
        const val urlKey = "urlKey"

    }

    private var repoNasa: NASARepository = NASARepository(application)

    private var spinnerStatus = MutableLiveData<Boolean>(false)

    fun getSpinnerStatusLiveData () : LiveData<Boolean> {
        return spinnerStatus
    }

    fun setSpinnerStatus (boolean: Boolean) {
        spinnerStatus.value = boolean
    }



    fun getUrl(): LiveData<String> {
        return savedStateHandle.getLiveData(urlKey)
    }

    fun loadPatent (case_number:String) {
        //TODO : implement error handling per app requirements

        setSpinnerStatus(true)


        val errorHandler = CoroutineExceptionHandler { _, exception ->
            setSpinnerStatus(false)

        }

        viewModelScope.launch(errorHandler) {
            val response = repoNasa.getPatentID(case_number)

            response.body()?.results?.first().let { data ->
                println("data : $data")
                println("data patent number : ${data?.patent_number}")
                val newURL = data?.patent_number?.replace(",", "")
                savedStateHandle.set(urlKey, "https://patents.google.com/patent/US$newURL/")
            }

            setSpinnerStatus(false)


        }
    }

}