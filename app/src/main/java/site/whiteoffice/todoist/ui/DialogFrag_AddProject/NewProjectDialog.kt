package site.whiteoffice.todoist.ui.DialogFrag_AddProject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.add_project_dialog.*
import kotlinx.android.synthetic.main.add_project_dialog.view.*
import site.whiteoffice.todoist.DataClasses.Project
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.ui.ProjectList.ProjectList
import java.util.*

class NewProjectDialog: DialogFragment() {

    companion object {

        const val TAG = "SimpleDialog"

        //private const val KEY_TITLE = "KEY_TITLE"
        //private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        //        fun newInstance(title: String, subTitle: String): NewProjectDialog {
        fun newInstance(): NewProjectDialog {
            val args = Bundle()
            //args.putString(KEY_TITLE, title)
            //args.putString(KEY_SUBTITLE, subTitle)
            val fragment = NewProjectDialog()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_project_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupClickListeners(view)
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
        view.newProjectTitle.text = "What is the new title?"
    }

    private fun setupClickListeners(view: View) {
        /*view.btnPositive.setOnClickListener {
            // TODO: Do some task here
            dismiss()
        }
        view.btnNegative.setOnClickListener {
            // TODO: Do some task here
            dismiss()
        }*/

        view.addProjectConfirmationButton.setOnClickListener {

            val id = UUID.randomUUID().leastSignificantBits
            //val id = 10L

            val project = Project(id,newProjectEditText.text.toString(),0,1,1,
                false,0,false,false)

            val parent = parentFragment as? ProjectList
            parent?.data?.createNewProject(project)
            dismiss()
        }
    }

}