package site.whiteoffice.todoist.ui.ImageDump

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.query_dialog.view.*
import site.whiteoffice.todoist.R
import kotlinx.android.synthetic.main.image_dump_list.view.*
import site.whiteoffice.todoist.PersistentStorage.setLastQuery

class QueryDialog:DialogFragment() {


    companion object {

        const val tag = "QueryDialog"
        //val lastQueryKey = "lastQueryKey"

        fun newInstance(): QueryDialog {
            val args = Bundle()

            val fragment = QueryDialog()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.query_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {
        //view.tvTitle.text = arguments?.getString(KEY_TITLE)
        //view.tvSubTitle.text = arguments?.getString(KEY_SUBTITLE)
        view.queryTitle.text = "What do you want to search?"
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val query = view?.queryEditText?.text.toString()

        //val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        //sharedPref?.edit()?.putString(lastQueryKey, query)?.apply()

        setLastQuery(activity, query)

        val dumper = parentFragment as ImageDump
        dumper.setCurrentQueryTitle(query)

        dumper.data.loadPatents(dumper.view?.pBar, query)

        //val mainActivity = activity as MainActivity
        //mainActivity.updateToolBarTitleForImageDump()


    }

}