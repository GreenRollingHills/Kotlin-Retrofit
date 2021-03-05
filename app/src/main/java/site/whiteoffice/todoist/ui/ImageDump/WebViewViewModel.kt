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

class WebViewViewModel(application: Application):AndroidViewModel(application) {

    private var repo: NASARepository =
        NASARepository(application)

    private val tag = WebViewViewModel::class.java.simpleName

    private var url = MutableLiveData<String>()

    private val callback = object : Callback<PatentIDResults> {
        override fun onFailure(call: Call<PatentIDResults>?, t: Throwable?) {
            Log.d(tag, "nasa api issue : ${t?.message}")
        }

        override fun onResponse(call: Call<PatentIDResults>?, response: Response<PatentIDResults>?) {
            response?.isSuccessful.let {
                println("webView viewModel, response : $response")
                println("response body : ${response?.body()}")
                println("response raw : ${response?.raw()}")
                println("response error body : ${response?.errorBody()}")

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
    fun getUrl():LiveData<String> {
        return url
    }

    fun loadPatent (case_number:String) {
        repo.getPatentID(case_number, callback)
    }


}