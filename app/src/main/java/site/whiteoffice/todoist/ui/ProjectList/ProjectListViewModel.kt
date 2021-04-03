package site.whiteoffice.todoist.ui.ProjectList

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.whiteoffice.todoist.DataClasses.Project
import site.whiteoffice.todoist.DataClasses.RepoResult
import site.whiteoffice.todoist.Repository.TodoistRepository

class ProjectListViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
): AndroidViewModel(application) {

    private var todoistRepo: TodoistRepository = TodoistRepository(application)

    companion object {

        private val TAG = ProjectListViewModel::class.simpleName
    }

    private var spinnerStatus = MutableLiveData<Boolean>(false)

    fun getSpinnerStatusLiveData () : LiveData<Boolean> {
        return spinnerStatus
    }

    fun setSpinnerStatus (boolean: Boolean) {
        spinnerStatus.value = boolean
    }

    private val callbackCreateProject = object : Callback<Project> {
        override fun onFailure(call: Call<Project>?, t: Throwable?) {
            Log.e(TAG, "callbackCreateProject, api issue : ${t?.message}")
            //TODO : implement error handling
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
        //todoistRepo.createProject(project, callbackCreateProject)
        //TODO : implement error handling per app requirements
        setSpinnerStatus(true)

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            setSpinnerStatus(false)

        }

        viewModelScope.launch (errorHandler) {
            val response = todoistRepo.createProject(project)
            response.body()?.let { project ->
                todoistRepo.cacheProject(project)

            }

            setSpinnerStatus(false)

        }

    }



    private val callbackProjectLoad = object : Callback<List<Project>> {
        override fun onFailure(call: Call<List<Project>>?, t: Throwable?) {
            Log.e(TAG, "callbackProjectLoad, api issue : ${t?.message}")
            //TODO : implement error handling
        }

        override fun onResponse(call: Call<List<Project>>?, response: Response<List<Project>>?) {
            response?.isSuccessful.let {
                Log.d(TAG, "response body : ${response?.body()}")
                val resultList = RepoResult(response?.body() ?: emptyList())



                viewModelScope.launch {
                    /* TODO : don't delete all projects right before I get all projects.
                        The best way to do this is probably to use ToDoist's synch api.*/

                    todoistRepo.deleteAllProjectsInRoomDB()
                    todoistRepo.cacheAllProjects(resultList.projects)
                }


            }

        }
    }


    fun loadProjects () {
        //todoistRepo.getProjects(callbackProjectLoad)
        //TODO : implement error handling per app requirements
        setSpinnerStatus(true)

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            setSpinnerStatus(false)

        }

        viewModelScope.launch (errorHandler) {
            val response = todoistRepo.getProjects()
            response.body()?.let { list ->
                val repoResult = RepoResult(list)
                /* TODO : don't delete all projects right before I get all projects.
                    The best way to do this is probably to use ToDoist's synch api.*/

                todoistRepo.deleteAllProjectsInRoomDB()
                //todoistRepo.cacheAllProjects(resultList.projects)
                todoistRepo.cacheAllProjects(repoResult.projects)

            }



            setSpinnerStatus(false)


        }


    }


}