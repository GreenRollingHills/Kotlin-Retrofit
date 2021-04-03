package site.whiteoffice.todoist.ui.ImageDump

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.image_dump_list.*
import kotlinx.android.synthetic.main.image_dump_list.view.*
import site.whiteoffice.todoist.DataClasses.PatentSummary
import site.whiteoffice.todoist.DataClasses.Project
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.PersistentStorage.getLastQuery
import site.whiteoffice.todoist.ui.ProjectList.ProjectList
import site.whiteoffice.todoist.ui.ProjectList.ProjectListListAdapter
import site.whiteoffice.todoist.ui.ProjectList.ProjectListViewModel


class ImageDump : Fragment() {

    val data by viewModels<ImageDumpViewModel>()

    companion object {

        private val TAG = ImageDump::class.java.simpleName

        fun removeSpanText(string:String):String {
            val string1 = string.replace("</span>", "")
            return string1.replace("<span class=\"highlight\">", "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.image_dump_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newTask.setOnClickListener {
            newTaskAction()
        }

        queryButton.setOnClickListener {
            displayQueryDialog()
        }

        val adapter = ImageDumpListAdapter()
        recyclerView2.adapter = adapter
        recyclerView2.layoutManager = LinearLayoutManager(context)

        data.getSpinnerStatusLiveData().observe(viewLifecycleOwner, Observer { bool ->
            view.pBar.visibility = if (bool) View.VISIBLE else View.INVISIBLE

        })

        data.getPatentList().observe(viewLifecycleOwner, Observer { list ->

            Log.d(TAG, "list : $list")
            adapter.submitList(list) {
                //view.pBar.visibility = View.INVISIBLE
                data.setSpinnerStatus(false)
                (recyclerView2.layoutManager as LinearLayoutManager).scrollToPosition(0)

            }


        })

        Log.d(TAG, "test value : ${data.getTest()}")

        val lastQuery =
            getLastQuery(activity)
        Log.d(TAG, "lastQuery : $lastQuery")
        if (lastQuery != null) {
            setCurrentQueryTitle(lastQuery)
            //data.loadPatents(view.pBar, lastQuery)
            data.loadPatents(lastQuery)

        }

    }

    private fun newTaskAction () {

        val adapter = recyclerView2.adapter as ImageDumpListAdapter
        val selectedItem = adapter.selectedItem
        if (selectedItem != null) {
            val nasaData = adapter.currentList[selectedItem]
            val action = ImageDumpDirections.actionImageDumpToAddTaskProjects2(nasaData)
            view?.findNavController()?.navigate(action)
        }


    }

    private fun displayQueryDialog () {
        QueryDialog().show(this.childFragmentManager, QueryDialog::class.simpleName)

    }

    fun setCurrentQueryTitle (string:String) {
        currentQuery.text = "Searching for : $string"
    }


}