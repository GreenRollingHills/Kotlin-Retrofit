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
import site.whiteoffice.todoist.ui.ProjectList.ProjectListViewHolderData
import site.whiteoffice.todoist.ui.ProjectList.ProjectListViewModel


class ImageDump : Fragment()/*, ImageDumpAdapter.FragmentDelegate */ {


    val data by viewModels<ImageDumpViewModel>()

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImageDump().apply {
                arguments = Bundle().apply {
                    //putString(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }

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

        val adapter = context?.let { ImageDumpListAdapter(it) }

        data.getPatentList().observe(viewLifecycleOwner, Observer { list ->

            view.pBar.visibility = View.INVISIBLE
            val newList = returnAdapterData(list)
            adapter?.submitList(newList)

            /*if (recyclerView2.adapter == null) {
                recyclerView2.adapter = context?.let { ImageDumpAdapter(it, list) }
            } else {
                val adapter = recyclerView2.adapter as ImageDumpAdapter
                adapter.updateList(list)
            }*/


        })

        recyclerView2.layoutManager = LinearLayoutManager(context)

        val lastQuery =
            getLastQuery(activity)
        if (lastQuery != null) {
            setCurrentQueryTitle(lastQuery)
            data.loadPatents(view.pBar, lastQuery)
        }

    }

    private fun newTaskAction () {

        //val adapter = recyclerView2.adapter as ImageDumpAdapter
        val adapter = recyclerView2.adapter as ImageDumpListAdapter
        val selectedItem = adapter.selectedItem
        //        if (adapter.selected_item != null) {
        if (selectedItem != null) {
            //val nasaData = data.getPatentList().value?.get(adapter.selected_item!!)
            val nasaData = adapter.currentList[selectedItem]
            val action = ImageDumpDirections.actionImageDumpToAddTaskProjects2(nasaData)
            view?.findNavController()?.navigate(action)
        }


    }

    private fun displayQueryDialog () {
        QueryDialog.newInstance().show(this.childFragmentManager, QueryDialog::class.simpleName)

    }

    fun setCurrentQueryTitle (string:String) {
        currentQuery.text = "Searching for : $string"
    }

    fun returnAdapterData(list: List<PatentSummary>):MutableList<ImageDumpViewHolderData> {

        val mutableList = mutableListOf<ImageDumpViewHolderData>()

        for (i in list.indices) {
            val p = list[i]
            val data = ImageDumpViewHolderData(ImageDumpListAdapter.NASAImageType, p)
            mutableList.add(data)
        }

        return mutableList
    }


}