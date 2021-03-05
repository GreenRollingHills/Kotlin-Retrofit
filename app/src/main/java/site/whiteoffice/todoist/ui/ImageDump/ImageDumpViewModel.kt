package site.whiteoffice.todoist.ui.ImageDump

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.whiteoffice.todoist.DataClasses.PatentSummary
import site.whiteoffice.todoist.DataClasses.PatentSummaryResults
import site.whiteoffice.todoist.Repository.NASARepository

class ImageDumpViewModel (
    application: Application
):AndroidViewModel(application) {

    private var repo: NASARepository = NASARepository(application)

    //private var list = MutableLiveData<List<ImageDumpViewHolderData>>()


    companion object {
        private val TAG = ImageDumpViewModel::class.simpleName


    }

    //    fun getPatentList ():LiveData<List<ImageDumpViewHolderData>> {
    fun getPatentList ():LiveData<List<PatentSummary>> {
        //return list
        return repo.getPatentSummariesLiveData()
    }

    private val callback = object : Callback<PatentSummaryResults> {
        override fun onFailure(call: Call<PatentSummaryResults>?, t: Throwable?) {
            //Log.e("MainActivity", "Problem calling todoist API ${t?.message}")
            Log.e(TAG, "nasa api issue : ${t?.message}")
            //println("call result : $call")


        }

        override fun onResponse(call: Call<PatentSummaryResults>?, response: Response<PatentSummaryResults>?) {
            response?.isSuccessful.let {
                Log.d(TAG, "response body : ${response?.body()}")


                //val tempList = mutableListOf<ImageDumpViewHolderData>()

                response?.body()?.results?.let {results ->

                    viewModelScope.launch {
                        val tempList = mutableListOf<PatentSummary>()
                        for (patent in results) {
                            /*for (j in patent.indices) {
                                val data = patent[j]
                                Log.d(TAG, "data : $data")
                            }*/

                            //Log.d(TAG, "patent[patent.size-2] : ${patent[patent.size-3]}")

                            val data = PatentSummary(patent[2],
                                patent[patent.size-3], patent[3], patent[1])

                            tempList.add(data)


                            //val data = ImageDumpViewHolderData(ImageDumpAdapter.NASAImageType, patent[2],
                            //patent[patent.size-3], patent[3], patent[1])
                            //tempList.add(data)

                        }

                        repo.removeAllPatentSummariesFromRoomDB()
                        repo.cachePatentSummaries(tempList)
                    }

                    //Log.d(TAG, "size : ${results.size}")
                    //Log.d(tag, "first.size : ${results.first().size}")

                }

                //list.value = tempList

            }

        }
    }

    fun loadPatents (spinner: ProgressBar?, keyword:String) {
        spinner?.visibility = View.VISIBLE
        repo.getPatentSummaryResults(keyword, callback)
    }

}