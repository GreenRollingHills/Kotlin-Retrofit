package site.whiteoffice.todoist.API

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import site.whiteoffice.todoist.DataClasses.Project
import site.whiteoffice.todoist.DataClasses.Task

interface TodoistService {


    //@Headers("Authorization: Bearer 6f6a2b18078132f45595b62d7e3ff7a68b64967a")
    @GET("v1/projects")
    //fun getProjects():Call<RepoResult>
    fun getProjects():Call<List<Project>>

    //@Headers("Authorization: Bearer 6f6a2b18078132f45595b62d7e3ff7a68b64967a")
    @GET("v1/tasks")
    fun getTasks(@Query("project_id") projectID: Long):Call<List<Task>>

    //@Headers("Authorization: Bearer 6f6a2b18078132f45595b62d7e3ff7a68b64967a", "Content-Type: application/json")
    @Headers("Content-Type: application/json")
    @POST("v1/projects")
    fun createProject(@Body body: Project):Call<Project>

    //@Headers("Authorization: Bearer 6f6a2b18078132f45595b62d7e3ff7a68b64967a")
    //@GET("v1/tasks/{taskID}")
    //fun deleteTask(@Path("taskID") taskID: String):Call<ResponseBody>
    @DELETE("v1/tasks/{taskID}")
    fun deleteTask(@Path("taskID") taskID: String):Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("v1/tasks")
    fun createTask(@Body body:Task, @Header("requestID") requestID:String):Call<Task>

    @POST("v1/tasks/{taskID}/close")
    fun closeTask(@Path ("taskID") taskID:String):Call<ResponseBody>

}

