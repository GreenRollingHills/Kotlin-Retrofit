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
import kotlinx.android.synthetic.main.image_dump_list.view.*
import kotlinx.android.synthetic.main.project_list.*
import kotlinx.android.synthetic.main.project_list.view.*
import kotlinx.android.synthetic.main.project_list.view.pBar
import site.whiteoffice.todoist.DataClasses.Project
//import kotlinx.android.synthetic.main.add_task_projects.*
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.ui.DialogFrag_AddProject.NewProjectDialog


class ProjectList : Fragment(), ProjectListListAdapter.NavigationDelegate {

    val data by viewModels<ProjectListViewModel>()

    private val args: ProjectListArgs by navArgs()

    companion object {

        private val TAG = ProjectList::class.simpleName

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

        val adapter = ProjectListListAdapter(this)

        data.getSpinnerStatusLiveData().observe(viewLifecycleOwner, Observer { bool ->
            view.pBar.visibility = if (bool) View.VISIBLE else View.INVISIBLE

        })

        data.getProjects().observe(viewLifecycleOwner, Observer { list ->
            Log.d(TAG, "onViewCreated, list : $list")


            adapter.submitList(list)



        })

        projectListRV.adapter = adapter

        projectListRV.layoutManager = LinearLayoutManager(context)
        projectListRV.addItemDecoration(DividerItemDecoration(context,
            DividerItemDecoration.VERTICAL))

        Log.d(TAG, "ProjectList, savedInstanceState : $savedInstanceState")
        if (savedInstanceState == null) {
            data.loadProjects()

        }

    }

    private fun newProjectButtonAction () {
        NewProjectDialog().show(this.childFragmentManager, NewProjectDialog::class.simpleName)


    }

    override fun navigateToTaskList(projectID: Long) {
        val action = ProjectListDirections.actionAddTaskProjectsToTaskList(projectID, args.nasaData)
        view?.findNavController()?.navigate(action)
    }





}