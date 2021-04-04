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


    fun getProjects (): LiveData<List<Project>> {

        return todoistRepo.getProjectsLiveData()
    }

    fun createNewProject (project:Project) {
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



    fun loadProjects () {
        //TODO : implement error handling per app requirements
        setSpinnerStatus(true)

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            setSpinnerStatus(false)

        }

        viewModelScope.launch (errorHandler) {
            val response = todoistRepo.getProjects()
            response.body()?.let { list ->
                val repoResult = RepoResult(list)

                /** because I don't auto synch todoist account with this app I need to
                delete all projects before I load all projects */

                todoistRepo.deleteAllProjectsInRoomDB()
                todoistRepo.cacheAllProjects(repoResult.projects)

            }



            setSpinnerStatus(false)


        }


    }


}