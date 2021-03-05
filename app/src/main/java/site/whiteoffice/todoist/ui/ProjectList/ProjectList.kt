package site.whiteoffice.todoist.ui.ProjectList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.project_list.*
import kotlinx.android.synthetic.main.project_list.view.*
import site.whiteoffice.todoist.DataClasses.Project
//import kotlinx.android.synthetic.main.add_task_projects.*
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.ui.DialogFrag_AddProject.NewProjectDialog


class ProjectList : Fragment(), ProjectListListAdapter.NavigationDelegate {

    val data by viewModels<ProjectListViewModel>()

    private val args: ProjectListArgs by navArgs()

    companion object {

        private val TAG = ProjectList::class.simpleName
        @JvmStatic
        fun newInstance() =
                ProjectList().apply {
                    arguments = Bundle().apply {

                    }
                }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.project_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newProjectButton.setOnClickListener{
            newProjectButtonAction()
        }

        val adapter = context?.let {ProjectListListAdapter(it, this)}

        data.getProjects().observe(viewLifecycleOwner, Observer { list ->
            Log.d(TAG, "onViewCreated, list : $list")

            view.pBar.visibility = View.INVISIBLE
            val newList = returnAdapterData(list)
            adapter?.submitList(newList)


            /*data.setRecyclerViewList(list)

            if (projectListRV.adapter == null) {
                projectListRV.adapter = context?.let { ProjectListAdapter(it, args.nasaData, data.list) }
            } else {
                val adapter = projectListRV.adapter as ProjectListAdapter
                adapter.update(data.list)

            }*/

        })

        projectListRV.adapter = adapter

        projectListRV.layoutManager = LinearLayoutManager(context)
        projectListRV.addItemDecoration(DividerItemDecoration(context,
            DividerItemDecoration.VERTICAL))

        view.pBar.visibility = View.VISIBLE

        Log.d(TAG, "ProjectList, savedInstanceState : $savedInstanceState")
        if (savedInstanceState == null) {
            data.loadProjects()

        }

    }

    private fun newProjectButtonAction () {
        NewProjectDialog.newInstance().show(this.childFragmentManager, NewProjectDialog::class.simpleName)


    }

    override fun navigateToTaskList(projectID: Long) {
        val action = ProjectListDirections.actionAddTaskProjectsToTaskList(projectID, args.nasaData)
        view?.findNavController()?.navigate(action)
    }

    fun returnAdapterData(list: List<Project>):MutableList<ProjectListViewHolderData> {

        val mutableList = mutableListOf<ProjectListViewHolderData>()

        for (i in list.indices) {
            val p = list[i]
            Log.d(TAG, "p name : ${p.name}")

            val data = ProjectListViewHolderData(ProjectListListAdapter.ProjectType, p)
            mutableList.add(data)
        }

        return mutableList
    }



}