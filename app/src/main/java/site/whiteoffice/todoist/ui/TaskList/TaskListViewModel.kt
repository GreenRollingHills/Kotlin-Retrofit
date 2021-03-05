package site.whiteoffice.todoist.ui.TaskList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.whiteoffice.todoist.DataClasses.Task
import site.whiteoffice.todoist.Repository.TodoistRepository

class TaskListViewModel(
    application: Application,
    private val projectID: Long
):AndroidViewModel(application) {



    private var repo: TodoistRepository = TodoistRepository(application)

    //var list = mutableListOf<TaskListViewHolderData>()

    companion object {
        private val TAG = TaskListViewModel::class.java.simpleName
    }

    private val callbackLoadTasks = object : Callback<List<Task>> {
        override fun onFailure(call: Call<List<Task>>?, t: Throwable?) {
            Log.e(TAG, "api issue : ${t?.message}")


        }

        override fun onResponse(call: Call<List<Task>>?, response: Response<List<Task>>?) {
            response?.isSuccessful.let {
                Log.d(TAG, "response body : ${response?.body()}")

                val list = response?.body()
                if (list != null) {
                    viewModelScope.launch {
                        /*TODO instead of deleting all tasks before inserting all tasks I should
                        *  delete/update just one task at a time */

                        //AppDatabase.getInstance(getApplication()).todoistDao.deleteAllTasks()
                        //AppDatabase.getInstance(getApplication()).todoistDao.insertAll(*list.toTypedArray())

                        repo.deleteAllTasksInRoomDB()
                        repo.cacheAllTasks(list)
                    }
                }
            }
        }
    }


    fun loadTasks () {
        repo.getTasks(projectID, callbackLoadTasks)
    }

    //    fun getTasks (projectID:Long): LiveData<List<Task>> {
    fun getTasksLiveData (): LiveData<List<Task>> {
        //return AppDatabase.getInstance(getApplication()).todoistDao.getTasksFor(projectID)
        //return repo.getCachedProjects(getApplication())
        return repo.getTasksLiveData(projectID)
    }

    /*fun setRecyclerViewList (list:List<Task>) {
        println("setRecyclerViewList")
        this.list.clear()

        for (t in list) {
            println("t : $t")
            println("t content : ${t.content}")

            val holder = TaskListViewHolderData(TaskListAdapter.TaskType, t)
            this.list.add(holder)
        }
    }*/

    private val callbackDeleteTask = object : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
            Log.e(TAG, "api issue : ${t?.message}")


        }

        override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
            response?.isSuccessful.let {
                Log.d(TAG, "response body : ${response?.body()}")
                Log.d(TAG, "error body ${response?.errorBody()}")
                viewModelScope.launch {
                    //TodoistDatabase.getInstance(getApplication()).projectDao.deleteAllTasks()
                    //loadTasks()
                    repo.getTasks(projectID, callbackLoadTasks)


                }

            }

        }
    }



    fun deleteTask (id:String) {
        repo.deleteTask(id, callbackDeleteTask)
    }

    private val callbackCreateTask = object : Callback<Task> {
        override fun onFailure(call: Call<Task>?, t: Throwable?) {
            Log.e(TAG, "api issue : ${t?.message}")

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
        repo.createTask(task, callbackCreateTask)
    }

    private val callbackCloseTask = object : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
            Log.e(TAG, "api issue : ${t?.message}")

        }

        override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
            response?.isSuccessful.let {

                Log.d(TAG, "response body : ${response?.body()}")
                Log.d(TAG, "error body ${response?.errorBody()}")

                viewModelScope.launch {
                    //TodoistDatabase.getInstance(getApplication()).projectDao.deleteAllTasks()
                    //loadTasks()
                    repo.getTasks(projectID, callbackLoadTasks)


                }


            }

        }
    }

    fun closeTask(taskID: String) {
        repo.closeTask(taskID, callbackCloseTask)
    }
}