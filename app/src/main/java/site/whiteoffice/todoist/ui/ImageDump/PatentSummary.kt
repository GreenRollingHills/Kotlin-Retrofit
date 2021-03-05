package site.whiteoffice.todoist.ui.ImageDump

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.patent_summary.*
import site.whiteoffice.todoist.R


class PatentSummary:Fragment() {

    lateinit var data: PatentSummaryViewModel
    private val args: PatentSummaryArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        data = ViewModelProvider(this).get(PatentSummaryViewModel::class.java)

        arguments?.let {


        }

        //data.summary = args.summaryString
        //data.case_number = args.caseNumber

        //data.nasaData = args.nasaData

        /*ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifeCycleListener({
            println("startCallback")

        }, {
            println("stopCallback")
        }))*/


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.patent_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pBar.hide()

        /*data.getUrl().observe(viewLifecycleOwner, Observer { url ->
            if (! url.isNullOrEmpty()) {

                println("about to display : $url")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)

            }
        })



        toPatentWebViewButton.setOnClickListener {
            //val action = PatentSummaryDirections.actionPatentSummaryToWebView(data.case_number)
            //view.findNavController().navigate(action)
            pBar.show()

           // data.case_number?.let { it1 -> data.loadPatent(it1) }
            data.nasaData?.let { it ->  data.loadPatent(it.patentCaseNumber)}
        }*/

        toProjectListFromPatentSummary.setOnClickListener {
            createTaskAction()
        }

        //patentSummaryTextView.text = ImageDump.removeSpanText(data.summary)
        patentSummaryTextView.text = "?"
        //val summaryText = data.nasaData?.patentSummary
        val summaryText = args.nasaData?.patentSummary?.patentSummary

        if (summaryText != null) {
            patentSummaryTextView.text = ImageDump.removeSpanText(summaryText)
        }


    }

    fun createTaskAction () {
        //val action = PatentSummaryDirections.actionPatentSummaryToAddTaskProjects(data.nasaData)
        val action = PatentSummaryDirections.actionPatentSummaryToAddTaskProjects(args.nasaData)

        view?.findNavController()?.navigate(action)
    }

    override fun onStop() {
        super.onStop()

        pBar.hide()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        println("patentSummary, onHiddenChanged : $hidden")
    }

}