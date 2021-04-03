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

        private val TAG = QueryDialog::class.java.simpleName

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
        view.queryTitle.text = "What do you want to search?"
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val query = view?.queryEditText?.text.toString()

        setLastQuery(activity, query)

        val dumper = parentFragment as ImageDump
        dumper.setCurrentQueryTitle(query)

        //dumper.data.loadPatents(dumper.view?.pBar, query)
        dumper.data.loadPatents(query)


    }

}