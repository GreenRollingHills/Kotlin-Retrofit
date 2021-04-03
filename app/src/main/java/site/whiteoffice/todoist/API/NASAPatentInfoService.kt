package site.whiteoffice.todoist.API

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import site.whiteoffice.todoist.DataClasses.PatentSummaryResults

interface NASAPatentInfoService {

    @Headers("Authorization: Bearer DEMO_KEY")
    @GET("patent/")
    suspend fun getPatents (@QueryName keyword: String, @Query("api_key") key:String = "DEMO_KEY"): Response<PatentSummaryResults>


}
