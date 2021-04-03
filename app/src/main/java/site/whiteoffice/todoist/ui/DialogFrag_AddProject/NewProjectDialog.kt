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
        view.newProjectTitle.text = "What is the new title?"
    }

    private fun setupClickListeners(view: View) {


        view.addProjectConfirmationButton.setOnClickListener {

            val id = UUID.randomUUID().leastSignificantBits
            val project = Project(id,newProjectEditText.text.toString(),0,1,1,
                false,0,false,false)

            val parent = parentFragment as? ProjectList
            parent?.data?.createNewProject(project)
            dismiss()
        }
    }

}