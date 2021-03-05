package site.whiteoffice.todoist.Repository

import android.app.Application
import androidx.lifecycle.LiveData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import site.whiteoffice.todoist.DataClasses.AuthData
import site.whiteoffice.todoist.DataClasses.Project
import site.whiteoffice.todoist.DataClasses.Task
import site.whiteoffice.todoist.API.TodoistAuthenticateService
import site.whiteoffice.todoist.API.TodoistService
import site.whiteoffice.todoist.PersistentStorage.AppDatabase
import site.whiteoffice.todoist.PersistentStorage.getTokenFromSharedPreferences
import java.util.*

class TodoistRepository (
    val application: Application
) {

    private val service: TodoistService
    private val serviceAuthenticate: TodoistAuthenticateService


    companion object {
        private const val BASE_URL = "https://api.todoist.com/rest/"
        private const val BASE_URL_AUTH = "https://todoist.com/oauth/"

    }

    class AuthInterceptor (
        val application: Application
    ):Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + getTokenFromSharedPreferences(application))
                .build()
            return chain.proceed(newRequest)
        }
    }

    init {

        val client = OkHttpClient.Builder().addInterceptor(
            AuthInterceptor(
                application
            )
        )
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(TodoistService::class.java)


        val retrofitAuthenticate = Retrofit.Builder()
            .baseUrl(BASE_URL_AUTH)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        serviceAuthenticate = retrofitAuthenticate.create(TodoistAuthenticateService::class.java)
    }

    // region authenticate
    fun getToken(code:String, callback:Callback<AuthData>) {
        val clientID = "e87862c9e3c64471878820dda9fa5aaa"
        val clientSecret = "f604143a35b043d1acc250140549b742"
        serviceAuthenticate.getAccessToken(clientID, clientSecret, code).enqueue(callback)
    }
    //endregion


    //region Projects
    fun getProjects(callback: Callback<List<Project>>) {
        service.getProjects().enqueue(callback)
    }

    fun getProjectsLiveData (): LiveData<List<Project>> {
        return AppDatabase.getInstance(application).todoistDao.getAllProjects()
    }

    fun createProject(project:Project, callback:Callback<Project>) {
        service.createProject(project).enqueue(callback)
    }

    suspend fun cacheAllProjects (list:List<Project>) {
        AppDatabase.getInstance(application).todoistDao.insertAllProjects(*list.toTypedArray())
    }

    suspend fun cacheProject (project:Project) {
        AppDatabase.getInstance(application).todoistDao.insertAllProjects(project)
    }

    suspend fun deleteAllProjectsInRoomDB () {
        AppDatabase.getInstance(application).todoistDao.deleteAllProjects()
    }

    //endregion

    // region Tasks
    fun getTasks(projectID: Long, callback: Callback<List<Task>>) {
        service.getTasks(projectID).enqueue(callback)
    }

    fun getTasksLiveData (projectID: Long):LiveData<List<Task>> {
        return AppDatabase.getInstance(application).todoistDao.getTasksFor(projectID)
    }

    suspend fun cacheAllTasks (list:List<Task>) {
        AppDatabase.getInstance(application).todoistDao.insertAllTasks(*list.toTypedArray())

    }

    suspend fun cacheTasks (task:Task) {
        AppDatabase.getInstance(application).todoistDao.insertAllTasks(task)

    }

    fun createTask(task: Task, callback:Callback<Task>) {
        val id = UUID.randomUUID().leastSignificantBits
        service.createTask(task, id.toString()).enqueue(callback)
    }

    fun closeTask(taskID: String, callback: Callback<ResponseBody>) {
        service.closeTask(taskID).enqueue(callback)
    }

    fun deleteTask (id:String, callback:Callback<ResponseBody>) {
        service.deleteTask(id).enqueue(callback)
    }

    suspend fun deleteAllTasksInRoomDB () {
        AppDatabase.getInstance(application).todoistDao.deleteAllTasks()

    }
    


    //endregion


}
