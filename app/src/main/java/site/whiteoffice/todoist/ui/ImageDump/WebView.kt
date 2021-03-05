package site.whiteoffice.todoist.ui.ImageDump

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.web_view.*
import site.whiteoffice.todoist.R

class WebView:Fragment() {

    //lateinit var data: PatentSummaryViewModel
    private val args: WebViewArgs by navArgs()

    lateinit var data:WebViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //data = ViewModelProvider(this).get(PatentSummaryViewModel::class.java)

        arguments?.let {
            //data.summary = args.summaryString

        }

        data = ViewModelProvider(this).get(WebViewViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.caseNumber?.let { data.loadPatent(it) }

        data.getUrl().observe(viewLifecycleOwner, Observer { url ->
            if (! url.isNullOrEmpty()) {

                println("about to load : $url")
                webView.loadUrl(url)

            }
        })

        //webView.loadUrl("https://patents.google.com/patent/US9382020/")

    }
}