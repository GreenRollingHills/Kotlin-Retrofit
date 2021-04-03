package site.whiteoffice.todoist.ui.ImageDump

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.whiteoffice.todoist.DataClasses.PatentSummary
import site.whiteoffice.todoist.DataClasses.PatentSummaryResults
import site.whiteoffice.todoist.Repository.NASARepository

class ImageDumpViewModel (
    application: Application,
    private val savedStateHandle: SavedStateHandle
):AndroidViewModel(application) {

    private var repo: NASARepository = NASARepository(application)

    companion object {
        private val TAG = ImageDumpViewModel::class.simpleName


    }


    private var spinnerStatus = MutableLiveData<Boolean>(false)

    fun getSpinnerStatusLiveData () : LiveData<Boolean> {
        return spinnerStatus
    }

    fun setSpinnerStatus (boolean: Boolean) {
        spinnerStatus.value = boolean
    }

    fun getPatentList ():LiveData<List<PatentSummary>> {
        return repo.getPatentSummariesLiveData()
    }



    fun loadPatents (keyword:String) {
        //TODO : implement error handling per app requirements

        setSpinnerStatus(true)

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            Log.d(TAG, "imageDumpVM, error : ${exception.localizedMessage}")
            setSpinnerStatus(false)
        }


        viewModelScope.launch (errorHandler) {
            val response = repo.getPatentSummaryResults(keyword)
            Log.d(TAG, "loadPatents response code : ${response.code()}")
            response.body()?.results?.let { list ->
                val tempList = mutableListOf<PatentSummary>()
                for (patent in list) {

                    val data = PatentSummary(patent[2],
                        patent[patent.size-3], patent[3], patent[1])

                    tempList.add(data)

                }
                Log.d(TAG, "tempList after : $tempList")
                repo.removeAllPatentSummariesFromRoomDB()
                repo.cachePatentSummaries(tempList)
            }
            setSpinnerStatus(false)



        }
    }

}