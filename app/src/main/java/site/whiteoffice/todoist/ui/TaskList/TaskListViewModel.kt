package site.whiteoffice.todoist.ui.TaskList

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.whiteoffice.todoist.DataClasses.Task
import site.whiteoffice.todoist.Repository.TodoistRepository
import java.lang.Exception

class TaskListViewModel(
    application: Application,
    private val projectID: Long
):AndroidViewModel(application) {

    private var repo: TodoistRepository = TodoistRepository(application)

    companion object {
        private val TAG = TaskListViewModel::class.java.simpleName
    }

    private var spinnerStatus = MutableLiveData<Boolean>(false)

    fun getSpinnerStatusLiveData () : LiveData<Boolean> {
        return spinnerStatus
    }

    fun setSpinnerStatus (boolean: Boolean) {
        spinnerStatus.value = boolean
    }




    fun loadTasks () {
        //TODO : implement error handling per app requirements
        setSpinnerStatus(true)

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            setSpinnerStatus(false)

        }

        viewModelScope.launch (errorHandler) {
            val response = repo.getTasks(projectID)

            response.body()?.let { list ->
                /** because I don't auto synch todoist account with this app I need to
                delete all tasks before I load all tasks */
                repo.deleteAllTasksInRoomDB()
                repo.cacheAllTasks(list)
            }


            setSpinnerStatus(false)

        }
    }

    fun getTasksLiveData (): LiveData<List<Task>> {
        return repo.getTasksLiveData(projectID)
    }





    fun deleteTask (id:String) {
        //TODO : implement error handling per app requirements
        setSpinnerStatus(true)

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            setSpinnerStatus(false)

        }

        viewModelScope.launch (errorHandler) {
            val response = repo.deleteTask(id)
            if (response.isSuccessful) {
                repo.deleteTaskFromDB(id)
            }
            setSpinnerStatus(false)

        }
    }



    fun createTask(task: Task) {
        //TODO : implement error handling per app requirements
        setSpinnerStatus(true)

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            setSpinnerStatus(false)

        }

        viewModelScope.launch(errorHandler) {
            val response = repo.createTask(task)
            response.body()?.let { task ->
                repo.cacheTasks(task)

            }
            setSpinnerStatus(false)

        }
    }


    fun closeTask(taskID: String) {
        //TODO : implement error handling per app requirements
        setSpinnerStatus(true)

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            setSpinnerStatus(false)

        }

        viewModelScope.launch (errorHandler) {
            val response = repo.closeTask(taskID)
            if (response.isSuccessful) {
                repo.deleteTaskFromDB(taskID)
            }
            setSpinnerStatus(false)

        }
    }
}