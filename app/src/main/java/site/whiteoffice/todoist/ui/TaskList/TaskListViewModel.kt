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

class TaskListViewModel(
    application: Application,
    private val projectID: Long/*,
    private val savedStateHandle: SavedStateHandle*/
):AndroidViewModel(application) {

    private var repo: TodoistRepository = TodoistRepository(application)

    //var list = mutableListOf<TaskListViewHolderData>()

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

    private val callbackLoadTasks = object : Callback<List<Task>> {
        override fun onFailure(call: Call<List<Task>>?, t: Throwable?) {
            Log.e(TAG, "callbackLoadTasks, api issue : ${t?.message}")
            //TODO : implement error handling

        }

        override fun onResponse(call: Call<List<Task>>?, response: Response<List<Task>>?) {
            response?.isSuccessful.let {
                Log.d(TAG, "response body : ${response?.body()}")

                val list = response?.body()
                if (list != null) {
                    viewModelScope.launch {
                        /* TODO : don't delete all tasks right before I cache all tasks.
                            The best way to do this is probably to use ToDoist's synch api. */

                        repo.deleteAllTasksInRoomDB()
                        repo.cacheAllTasks(list)
                    }
                }
            }
        }
    }


    fun loadTasks () {
        //repo.getTasks(projectID, callbackLoadTasks)
        //TODO : implement error handling per app requirements
        setSpinnerStatus(true)

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            setSpinnerStatus(false)

        }

        viewModelScope.launch (errorHandler) {
            val response = repo.getTasks(projectID)

            response.body()?.let { list ->
                /* TODO : don't delete all tasks right before I cache all tasks.
                The best way to do this is probably to use ToDoist's synch api. */

                repo.deleteAllTasksInRoomDB()
                repo.cacheAllTasks(list)
            }


            setSpinnerStatus(false)

        }
    }

    fun getTasksLiveData (): LiveData<List<Task>> {
        return repo.getTasksLiveData(projectID)
    }


    private val callbackDeleteTask = object : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
            Log.e(TAG, "callbackDeleteTask, api issue : ${t?.message}")
            //TODO : implement error handling

        }

        override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
            response?.isSuccessful.let {
                Log.d(TAG, "response body : ${response?.body()}")
                Log.d(TAG, "error body ${response?.errorBody()}")

                //repo.getTasks(projectID, callbackLoadTasks)
                loadTasks()

            }

        }
    }



    fun deleteTask (id:String) {
        //repo.deleteTask(id, callbackDeleteTask)
        //TODO : implement error handling per app requirements
        setSpinnerStatus(true)

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            setSpinnerStatus(false)

        }

        viewModelScope.launch (errorHandler) {
            val response = repo.deleteTask(id)
            loadTasks()
            setSpinnerStatus(false)

        }
    }

    private val callbackCreateTask = object : Callback<Task> {
        override fun onFailure(call: Call<Task>?, t: Throwable?) {
            Log.e(TAG, "callbackCreateTask, api issue : ${t?.message}")
            //TODO : implement error handling

        }

        override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
            response?.isSuccessful.let {

                Log.d(TAG, "response body : ${response?.body()}")
                Log.d(TAG, "error body : ${response?.errorBody()?.string()}")

                val task = response?.body()
                if (task != null) {
                    viewModelScope.launch {
                        //AppDatabase.getInstance(getApplication()).todoistDao.insertAll(task)
                        repo.cacheTasks(task)

                    }
                }


            }

        }
    }

    fun createTask(task: Task) {
        //repo.createTask(task, callbackCreateTask)
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

    private val callbackCloseTask = object : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
            Log.e(TAG, "callbackCloseTask, api issue : ${t?.message}")
            //TODO : implement error handling

        }

        override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
            response?.isSuccessful.let {

                Log.d(TAG, "response body : ${response?.body()}")
                Log.d(TAG, "error body ${response?.errorBody()}")

                //repo.getTasks(projectID, callbackLoadTasks)
                loadTasks()


            }

        }
    }

    fun closeTask(taskID: String) {
        //repo.closeTask(taskID, callbackCloseTask)
        //TODO : implement error handling per app requirements
        setSpinnerStatus(true)

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            setSpinnerStatus(false)

        }

        viewModelScope.launch (errorHandler) {
            val response = repo.closeTask(taskID)
            loadTasks()
            setSpinnerStatus(false)

        }
    }
}