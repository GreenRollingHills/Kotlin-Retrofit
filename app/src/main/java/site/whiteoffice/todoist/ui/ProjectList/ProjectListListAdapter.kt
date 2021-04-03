package site.whiteoffice.todoist.ui.ProjectList

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import site.whiteoffice.todoist.DataClasses.Project
import site.whiteoffice.todoist.R

class ProjectListListAdapter(
    private val delegate:NavigationDelegate

): ListAdapter<Project, ProjectListListAdapter.ProjectListViewHolder> (DiffCallback()) {

    companion object {

        private val TAG = ProjectListListAdapter::class.java.simpleName

    }

    interface NavigationDelegate {
        fun navigateToTaskList(projectID:Long)
    }

    inner class ProjectListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            Log.d(TAG, "onBind")
            val data = getItem(position)

            val textView = itemView.findViewById<TextView>(R.id.addTask_projectName)
            textView.text = data.name

            itemView.setOnClickListener {
                delegate.navigateToTaskList(data.id)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectListListAdapter.ProjectListViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.project_list_view_holder, parent, false)
        return ProjectListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectListViewHolder, position: Int) {

        holder.bind(position)
    }


    class DiffCallback : DiffUtil.ItemCallback<Project>() {

        override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
            Log.d(TAG, "are items same")
            Log.d(TAG, "old : $oldItem , newItem : $newItem")
            Log.d(TAG, "return ${oldItem.id == newItem.id}")
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Project,
            newItem: Project
        ): Boolean {
            Log.d(TAG, "areContentsTheSame")
            Log.d(TAG, "old : $oldItem , newItem : $newItem")
            Log.d(TAG, "return ${oldItem.name == newItem.name}")
            return oldItem.name == newItem.name
        }

    }




}