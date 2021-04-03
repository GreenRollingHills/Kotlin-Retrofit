package site.whiteoffice.todoist.Repository

import android.app.Application
import androidx.compose.runtime.key
import androidx.lifecycle.LiveData
import retrofit2.Callback
import retrofit2.Response
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

    private val service_info: NASAPatentInfoService
    private val service_patentID: NASAPatentIDService

    companion object {
        private const val BASE_URL_PATENT_INFO = "https://technology.nasa.gov/api/query/"
        private const val BASE_URL_PATENT_ID = "https://technology-api.ndc.nasa.gov/api/patent/"

    }

    init {

        val retrofitPatentInfo = Retrofit.Builder()
            .baseUrl(BASE_URL_PATENT_INFO)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitPatentID = Retrofit.Builder()
            .baseUrl(BASE_URL_PATENT_ID)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service_info = retrofitPatentInfo.create(NASAPatentInfoService::class.java)
        service_patentID = retrofitPatentID.create(NASAPatentIDService::class.java)

    }

    suspend fun getPatentSummaryResults(keyword:String): Response<PatentSummaryResults> {
        return service_info.getPatents(keyword)

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

    suspend fun getPatentID(case_number: String) : Response<PatentIDResults> {
        return service_patentID.getPatentID(case_number)

    }

}