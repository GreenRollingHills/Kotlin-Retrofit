package site.whiteoffice.todoist.ui.TaskList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
//import kotlinx.android.synthetic.main.project_list.*
import kotlinx.android.synthetic.main.task_list.view.*
import kotlinx.android.synthetic.main.task_list.*
import site.whiteoffice.todoist.DataClasses.Task
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.ui.ImageDump.ImageDump
import site.whiteoffice.todoist.ui.TaskListViewModelFactory
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.image_dump_list.view.*
import kotlinx.android.synthetic.main.task_list.view.pBar


class TaskList : Fragment(), TaskListListAdapter.DataDelegate {


    var data:TaskListViewModel? = null

    val args:TaskListArgs by navArgs()



    companion object {

        private val TAG = TaskList::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        val application = activity?.application
        if (application != null) {
            data = ViewModelProvider(this, TaskListViewModelFactory(application, args.projectIDARG)).get(TaskListViewModel::class.java)



        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.createTaskButton.setOnClickListener {
            createTaskAction()
        }


        val adapter = TaskListListAdapter(this)

        data?.getSpinnerStatusLiveData()?.observe(viewLifecycleOwner, Observer { bool ->
            view.pBar.visibility = if (bool) View.VISIBLE else View.INVISIBLE

        })
        data?.getTasksLiveData()?.observe(viewLifecycleOwner, Observer { list ->

            Log.d(TAG, "taskList, onViewCreated, list : $list")

            adapter.submitList(list)



        })

        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.addItemDecoration(
            DividerItemDecoration(context,
            DividerItemDecoration.VERTICAL)
        )

        if (savedInstanceState == null) {
            data?.loadTasks()
        }
    }

    override fun presentAreYouSureAlert(taskID:String) {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Are you sure you want to delete?")

        builder?.setPositiveButton(android.R.string.yes) { dialog, which ->
            data?.deleteTask(taskID)

        }

        builder?.setNegativeButton(android.R.string.no) { dialog, which ->
            //do nothing
        }

        builder?.show()
    }

    private fun createTaskAction () {

        val patentName = args.nasaData?.patentName

        var content = "?"
        if (patentName != null) {
            content = ImageDump.removeSpanText(patentName)

        }

        val newTask = Task(
            content = "Study $content",
            comment_count = null,
            id = null,
            //project_id = projectID,
            project_id = args.projectIDARG,

            section_id = null,
            parent_id = null,
            order = null,
            priority = null,
            url = null
        )

        data?.createTask(newTask)
    }

    override fun closeTask(taskID: String) {

        data?.closeTask(taskID)
    }

    override fun startSpinner() {
        data?.setSpinnerStatus(true)
    }




}