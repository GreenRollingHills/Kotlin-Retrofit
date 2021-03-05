package site.whiteoffice.todoist.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import site.whiteoffice.todoist.DataClasses.PatentIDResults

interface NASAPatentIDService {

    //@Headers("Authorization: Bearer DEMO_KEY")
    //@GET("patent")
    //fun getPatents (@Query("keyword") keyword: String):Call<List<Patent>>}
    //@GET("gquh-watm.json")
    @GET("geturl/{id}")
    //fun getPatentID (@Query("case_number") case_number: String): Call<List<PatentID>>
    fun getPatentID (@Path("id") case_number: String): Call<PatentIDResults>

}