package site.whiteoffice.todoist.API

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import site.whiteoffice.todoist.DataClasses.PatentIDResults

interface NASAPatentIDService {

    @GET("geturl/{id}")
    suspend fun getPatentID (@Path("id") case_number: String): Response<PatentIDResults>

}