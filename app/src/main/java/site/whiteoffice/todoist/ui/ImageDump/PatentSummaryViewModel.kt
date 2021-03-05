package site.whiteoffice.todoist.ui.ImageDump

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.whiteoffice.todoist.DataClasses.PatentIDResults
import site.whiteoffice.todoist.Repository.NASARepository

class PatentSummaryViewModel(application: Application):AndroidViewModel(application) {


    companion object {
        private val TAG = PatentSummaryViewModel::class.java.simpleName

    }

    //var summary:String = "?"
    //var case_number:String? = null
    //var nasaData:ImageDumpViewHolderData? = null

    //private var repoNasa: NASARepository = NASARepository(application)


    //private var url = MutableLiveData<String>()

    /*private val callback = object : Callback<PatentIDResults> {
        override fun onFailure(call: Call<PatentIDResults>?, t: Throwable?) {
            Log.e(TAG, "nasa api issue : ${t?.message}")
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
                        url.value = "https://patents.google.com/patent/US$newURL/"

                    }
                }

                /*response?.body()?.get(0)?.patent_number?.let {patentID ->
                    //webView.loadUrl("https://patents.google.com/patent/US9382020/")
                    url.value = "https://patents.google.com/patent/US$patentID/"


                }*/

            }

        }


    }




    fun getUrl(): LiveData<String> {
        return url
    }

    fun loadPatent (case_number:String) {
        repoNasa.getPatentID(case_number, callback)
    }*/

}