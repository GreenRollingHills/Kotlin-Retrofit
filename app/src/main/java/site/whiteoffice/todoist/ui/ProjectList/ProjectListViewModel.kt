package site.whiteoffice.todoist.ui.ProjectList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.whiteoffice.todoist.DataClasses.Project
import site.whiteoffice.todoist.DataClasses.RepoResult
import site.whiteoffice.todoist.Repository.TodoistRepository

class ProjectListViewModel(
    application: Application
): AndroidViewModel(application) {

    private var todoistRepo: TodoistRepository = TodoistRepository(application)

    companion object {

        private val TAG = ProjectListViewModel::class.simpleName
    }

    private val callbackCreateProject = object : Callback<Project> {
        override fun onFailure(call: Call<Project>?, t: Throwable?) {
            Log.e(TAG, "onFailure message : ${t?.message}")
        }

        override fun onResponse(call: Call<Project>?, response: Response<Project>?) {
            response?.isSuccessful.let {
                Log.d(TAG, "response body : ${response?.body()}")
                val project = response?.body()

                viewModelScope.launch {
                    if (project != null) {
                        todoistRepo.cacheProject(project)
                    }
                }

            }

        }
    }

    fun getProjects (): LiveData<List<Project>> {

        return todoistRepo.getProjectsLiveData()
    }

    fun createNewProject (project:Project) {
        todoistRepo.createProject(project, callbackCreateProject)
    }



    private val callbackProjectLoad = object : Callback<List<Project>> {
        override fun onFailure(call: Call<List<Project>>?, t: Throwable?) {
            Log.e(TAG, "Problem calling todoist API ${t?.message}")
        }

        override fun onResponse(call: Call<List<Project>>?, response: Response<List<Project>>?) {
            response?.isSuccessful.let {
                Log.d(TAG, "response body : ${response?.body()}")
                val resultList = RepoResult(response?.body() ?: emptyList())



                viewModelScope.launch {
                    //cacheAll(resultList.projects)
                    /* TODO : don't delete all projects right before I get all projects.
                        The best way to do this is probably to use ToDoist's synch api.*/

                    todoistRepo.deleteAllProjectsInRoomDB()
                    todoistRepo.cacheAllProjects(resultList.projects)
                }


            }

        }
    }


    fun loadProjects () {
        viewModelScope.launch {
            todoistRepo.getProjects(callbackProjectLoad)
        }

    }


}