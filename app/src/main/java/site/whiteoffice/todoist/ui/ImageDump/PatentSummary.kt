package site.whiteoffice.todoist.ui.ImageDump

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.image_dump_list.view.*
import kotlinx.android.synthetic.main.patent_summary.*
import site.whiteoffice.todoist.R


class PatentSummary:Fragment() {

    private val args: PatentSummaryArgs by navArgs()

    private val data by viewModels<PatentSummaryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.patent_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        data.getSpinnerStatusLiveData().observe(viewLifecycleOwner, Observer { bool ->
            view.pBar.visibility = if (bool) View.VISIBLE else View.INVISIBLE

        })

        data.getUrl().observe(viewLifecycleOwner, Observer { url ->
            if (! url.isNullOrEmpty()) {

                println("about to display : $url")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)

            }
        })



        toPatentWebViewButton.setOnClickListener {
            args.nasaData?.let { it ->  data.loadPatent(it.patentCaseNumber)}

        }

        toProjectListFromPatentSummary.setOnClickListener {
            createTaskAction()
        }

        patentSummaryTextView.text = "?"
        val summaryText = args.nasaData?.patentSummary

        if (summaryText != null) {
            patentSummaryTextView.text = ImageDump.removeSpanText(summaryText)
        }


    }

    fun createTaskAction () {
        val action = PatentSummaryDirections.actionPatentSummaryToAddTaskProjects(args.nasaData)

        view?.findNavController()?.navigate(action)
    }

    override fun onStop() {
        super.onStop()

        pBar.hide()
    }


}