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
    //private var url = MutableLiveData<String>()

    private var spinnerStatus = MutableLiveData<Boolean>(false)

    fun getSpinnerStatusLiveData () : LiveData<Boolean> {
        return spinnerStatus
    }

    fun setSpinnerStatus (boolean: Boolean) {
        spinnerStatus.value = boolean
    }

    private val callback = object : Callback<PatentIDResults> {
        override fun onFailure(call: Call<PatentIDResults>?, t: Throwable?) {
            Log.e(TAG, "callback, api issue : ${t?.message}")
            //TODO : implement error handling
        }

        override fun onResponse(call: Call<PatentIDResults>?, response: Response<PatentIDResults>?) {
            response?.isSuccessful.let {
                Log.d(TAG, "response body : ${response?.body()}")
                Log.d(TAG, "response error body : ${response?.errorBody()}")

                response?.body()?.results?.let { list ->
                    if (list.isNotEmpty()) {
                        val data = list.first()
                        println("data : $data")
                        println("data patent number : ${data.patent_number}")
                        val newURL = data.patent_number.replace(",", "")
                        //url.value = "https://patents.google.com/patent/US$newURL/"
                        savedStateHandle.set(urlKey, "https://patents.google.com/patent/US$newURL/")

                    }
                }


            }

        }


    }

    fun getUrl(): LiveData<String> {
        //return url
        return savedStateHandle.getLiveData(urlKey)
    }

    fun loadPatent (case_number:String) {
        //repoNasa.getPatentID(case_number, callback)
        //TODO : implement error handling per app requirements

        setSpinnerStatus(true)


        val errorHandler = CoroutineExceptionHandler { _, exception ->
            setSpinnerStatus(false)

        }

        viewModelScope.launch(errorHandler) {
            val response = repoNasa.getPatentID(case_number)

            //val data = result.body()?.results?.first()
            response.body()?.results?.first().let { data ->
                println("data : $data")
                println("data patent number : ${data?.patent_number}")
                val newURL = data?.patent_number?.replace(",", "")
                //url.value = "https://patents.google.com/patent/US$newURL/"
                savedStateHandle.set(urlKey, "https://patents.google.com/patent/US$newURL/")
            }

            setSpinnerStatus(false)


        }
    }

}