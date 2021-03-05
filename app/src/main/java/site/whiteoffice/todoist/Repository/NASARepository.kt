package site.whiteoffice.todoist.Repository

import android.app.Application
import androidx.lifecycle.LiveData
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import site.whiteoffice.todoist.DataClasses.PatentIDResults
import site.whiteoffice.todoist.DataClasses.PatentSummaryResults
import site.whiteoffice.todoist.API.NASAPatentIDService
import site.whiteoffice.todoist.API.NASAPatentInfoService
import site.whiteoffice.todoist.DataClasses.PatentSummary
import site.whiteoffice.todoist.PersistentStorage.AppDatabase

class NASARepository (
    val application: Application
) {

    //private val patentInfoService: NASAPatentInfoService
    private val service_info: NASAPatentInfoService

    private val service_patentID: NASAPatentIDService

    companion object {
        //private const val BASE_URL = "https://api.nasa.gov/techtransfer/patent/"
        //private const val BASE_URL_PATENT_INFO = "https://api.nasa.gov/techtransfer/"
        private const val BASE_URL_PATENT_INFO = "https://technology.nasa.gov/api/query/"
        //private const val BASE_URL_PATENT_ID = "https://data.nasa.gov/resource/gquh-watm.json/"
        //private const val BASE_URL_PATENT_ID = "https://data.nasa.gov/resource/"
        private const val BASE_URL_PATENT_ID = "https://technology-api.ndc.nasa.gov/api/patent/"

        //https://data.nasa.gov/resource/gquh-watm.json
    }

    init {
        val retrofitPatentInfo = Retrofit.Builder()
            //1
            .baseUrl(BASE_URL_PATENT_INFO)
            .addConverterFactory(GsonConverterFactory.create())
            //2
            .build()

        val retrofitPatentID = Retrofit.Builder()
            .baseUrl(BASE_URL_PATENT_ID)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //3
        //service = retrofit.create(GithubService::class.java)
        service_info = retrofitPatentInfo.create(NASAPatentInfoService::class.java)
        service_patentID = retrofitPatentID.create(NASAPatentIDService::class.java)

    }

    fun getPatentSummaryResults(keyword:String, callback: Callback<PatentSummaryResults>) {
        service_info.getPatents(keyword).enqueue(callback)
    }

    fun getPatentSummariesLiveData ():LiveData<List<PatentSummary>> {
        return AppDatabase.getInstance(application).nasaDao.getAllPatentSummaries()

    }

    suspend fun cachePatentSummaries(list:List<PatentSummary>) {
        AppDatabase.getInstance(application).nasaDao.insertAllPatentSummaries(*list.toTypedArray())
    }

    suspend fun removeAllPatentSummariesFromRoomDB () {
        AppDatabase.getInstance(application).nasaDao.deleteAllPatentSummaries()
    }

    fun getPatentID(case_number: String, callback: Callback<PatentIDResults>) {
        service_patentID.getPatentID(case_number).enqueue(callback)
    }

}