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
import site.whiteoffice.todoist.R

class ProjectListListAdapter(
    private val context: Context,
    private val delegate:NavigationDelegate

): ListAdapter<ProjectListViewHolderData, ProjectListListAdapter.ProjectListViewHolder> (DiffCallback()) {

    companion object {

        private val TAG = ProjectListListAdapter::class.java.simpleName

        const val ProjectType = 0

    }

    interface NavigationDelegate {
        fun navigateToTaskList(projectID:Long)
    }

    inner class ProjectListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            Log.d(TAG, "onBind")
            val data = getItem(position)

            val textView = itemView.findViewById<TextView>(R.id.addTask_projectName)
            textView.text = data.project.name

            itemView.setOnClickListener {
                delegate.navigateToTaskList(data.project.id)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectListListAdapter.ProjectListViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        return when (viewType) {
            ProjectType -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.project_list_view_holder, parent, false)
                ProjectListViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ProjectListViewHolder, position: Int) {

        when (holder) {
            is ProjectListViewHolder -> holder.bind(position)

            else -> throw IllegalArgumentException()
        }
    }


    override fun getItemViewType(position: Int): Int {
        return getItem(position).type

    }

    class DiffCallback : DiffUtil.ItemCallback<ProjectListViewHolderData>() {

        override fun areItemsTheSame(oldItem: ProjectListViewHolderData, newItem: ProjectListViewHolderData): Boolean {
            //return oldItem?.id == newItem?.id
            Log.d(TAG, "are items same")
            Log.d(TAG, "old : $oldItem , newItem : $newItem")
            Log.d(TAG, "return ${oldItem.project.id == newItem.project.id}")
            return oldItem.project.id == newItem.project.id
            //return false
        }

        override fun areContentsTheSame(
            oldItem: ProjectListViewHolderData,
            newItem: ProjectListViewHolderData
        ): Boolean {
            Log.d(TAG, "areContentsTheSame")
            Log.d(TAG, "old : $oldItem , newItem : $newItem")
            Log.d(TAG, "return ${oldItem.project.name == newItem.project.name}")
            return oldItem.project.name == newItem.project.name
        }

    }




}