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



class TaskList : Fragment(), TaskListListAdapter.DataDelegate {

    private var projectID:Long = -1

    //lateinit var data: TaskListViewModel

    //val data by viewModels<TaskListViewModel>()

    var data:TaskListViewModel? = null

    val args:TaskListArgs by navArgs()

    companion object {

        private val TAG = TaskList::class.java.simpleName

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskList().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        projectID = args.projectIDARG

        val application = activity?.application
        if (application != null) {
            data = ViewModelProvider(this, TaskListViewModelFactory(application, projectID)).get(TaskListViewModel::class.java)
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

        view.pBar.visibility = View.INVISIBLE

        view.createTaskButton.setOnClickListener {
            createTaskAction()
        }


        val adapter = context?.let { TaskListListAdapter(it, this) }

        //        data.getTasks(projectID).observe(viewLifecycleOwner, Observer { list ->
        data?.getTasksLiveData()?.observe(viewLifecycleOwner, Observer { list ->

            Log.d(TAG, "taskList, onViewCreated, list : $list")
            view.pBar.visibility = View.INVISIBLE
            val newList = returnAdapterData(list)
            adapter?.submitList(newList)


            /*data.setRecyclerViewList(list)

            if (recyclerView.adapter == null) {
                recyclerView.adapter = context?.let { TaskListAdapter(it, data.list, this) }
            } else {
                val adapter = recyclerView.adapter as TaskListAdapter
                adapter.update(data.list)
            }*/


        })

        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.addItemDecoration(
            DividerItemDecoration(context,
            DividerItemDecoration.VERTICAL)
        )

        if (savedInstanceState != null) {
            view.pBar.visibility = View.VISIBLE
            data?.loadTasks()
        }
    }

    override fun presentAreYouSureAlert(taskID:String) {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Are you sure you want to delete?")

        builder?.setPositiveButton(android.R.string.yes) { dialog, which ->
            view?.pBar?.visibility = View.VISIBLE
            data?.deleteTask(taskID)

        }

        builder?.setNegativeButton(android.R.string.no) { dialog, which ->
            //do nothing
        }

        builder?.show()
    }

    private fun createTaskAction () {

        //val patentName = args.nasaData?.patentName
        val patentName = args.nasaData?.patentSummary?.patentName

        var content = "?"
        if (patentName != null) {
            content = ImageDump.removeSpanText(patentName)

        }

        val newTask = Task(
            content = "Study $content",
            comment_count = null,
            id = null,
            project_id = projectID,
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
        view?.pBar?.visibility = View.VISIBLE
    }

    /*override fun stopSpinner() {
        view?.pBar?.visibility = View.INVISIBLE
    }*/

    fun returnAdapterData(list:List<Task>):MutableList<TaskListViewHolderData> {

        val mutableList = mutableListOf<TaskListViewHolderData>()

        for (t in list) {

            val data = TaskListViewHolderData(TaskListAdapter.TaskType, t)
            mutableList.add(data)
        }

        return mutableList
    }


}