package site.whiteoffice.todoist.ui

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import site.whiteoffice.todoist.ui.TaskList.TaskListViewModel

class TaskListViewModelFactory(
    val application: Application,
    val projectID:Long
): ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T = TaskListViewModel(application, projectID) as T

}


