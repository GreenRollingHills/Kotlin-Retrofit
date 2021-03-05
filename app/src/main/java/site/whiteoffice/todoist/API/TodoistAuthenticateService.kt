package site.whiteoffice.todoist.API

import retrofit2.Call
import retrofit2.http.*
import site.whiteoffice.todoist.DataClasses.AuthData

interface TodoistAuthenticateService {

    //@Headers("Content-Type: application/json")
    @POST("access_token")
    fun getAccessToken(@Query ("client_id") client_id: String,
                       @Query("client_secret") client_secret: String,
                       @Query ("code") code:String): Call<AuthData>
}