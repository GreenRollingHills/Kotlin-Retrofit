package site.whiteoffice.todoist.API

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import site.whiteoffice.todoist.DataClasses.Project
import site.whiteoffice.todoist.DataClasses.Task

interface TodoistService {


    @GET("v1/projects")
    suspend fun getProjects():Response<List<Project>>

    @GET("v1/tasks")
    suspend fun getTasks(@Query("project_id") projectID: Long):Response<List<Task>>

    @Headers("Content-Type: application/json")
    @POST("v1/projects")
    suspend fun createProject(@Body body: Project): Response<Project>

    @DELETE("v1/tasks/{taskID}")
    suspend fun deleteTask(@Path("taskID") taskID: String):Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("v1/tasks")
    suspend fun createTask(@Body body:Task, @Header("requestID") requestID:String):Response<Task>

    @POST("v1/tasks/{taskID}/close")
    suspend fun closeTask(@Path ("taskID") taskID:String):Response<ResponseBody>


}

