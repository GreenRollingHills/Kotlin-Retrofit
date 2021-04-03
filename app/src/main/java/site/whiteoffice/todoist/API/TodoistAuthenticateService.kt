package site.whiteoffice.todoist.API

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import site.whiteoffice.todoist.DataClasses.AuthData

interface TodoistAuthenticateService {

    @POST("access_token")
    /*fun getAccessToken(@Query ("client_id") client_id: String,
                       @Query("client_secret") client_secret: String,
                       @Query ("code") code:String): Call<AuthData>*/

    suspend fun getAccessToken(@Query ("client_id") client_id: String,
                       @Query("client_secret") client_secret: String,
                       @Query ("code") code:String): Response<AuthData>
}