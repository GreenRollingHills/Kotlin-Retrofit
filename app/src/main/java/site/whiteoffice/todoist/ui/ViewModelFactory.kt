package site.whiteoffice.todoist.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import site.whiteoffice.todoist.ui.TaskList.TaskListViewModel

class TaskListViewModelFactory(
    val application: Application,
    val projectID:Long
):ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = TaskListViewModel(application, projectID) as T

}