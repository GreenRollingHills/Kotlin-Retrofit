package site.whiteoffice.todoist.ui.TaskList

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_list_view_holder.view.*
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.ui.ProjectList.ProjectListListAdapter
import site.whiteoffice.todoist.ui.ProjectList.ProjectListViewHolderData

class TaskListListAdapter (
    private val context: Context,
    private var dataDelegate: DataDelegate
) : ListAdapter<TaskListViewHolderData, TaskListListAdapter.TaskViewHolder> (DiffCallback()) {


    companion object {
        private val TAG = TaskListListAdapter::class.java.simpleName

        const val TaskType = 0

    }

    interface DataDelegate {
        fun presentAreYouSureAlert(taskID:String)
        fun closeTask(taskID: String)
        fun startSpinner()
        //fun stopSpinner()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            val data = getItem(position)
            val textView = itemView.findViewById<TextView>(R.id.taskContent)
            textView.text = data.task.content

            itemView.taskCompletionCheckBox.setOnClickListener {
                if (itemView.taskCompletionCheckBox.isChecked) {
                    val id = data.task.id
                    if (id != null) {
                        dataDelegate.startSpinner()
                        dataDelegate.closeTask(id.toString())

                    }
                }
            }

            itemView.deleteTaskButton.setOnClickListener {
                dataDelegate.presentAreYouSureAlert(data.task.id.toString())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder  {
        return when (viewType) {
            TaskType -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.task_list_view_holder, parent, false)
                TaskViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        when (holder) {
            is TaskListListAdapter.TaskViewHolder -> holder.bind(position)

            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).listViewType

    }

    class DiffCallback : DiffUtil.ItemCallback<TaskListViewHolderData>() {

        override fun areItemsTheSame(oldItem: TaskListViewHolderData, newItem: TaskListViewHolderData): Boolean {
            //return oldItem?.id == newItem?.id
            Log.d(TAG, "are items same")
            Log.d(TAG, "old : $oldItem , newItem : $newItem")
            Log.d(TAG, "return ${oldItem.task.id == newItem.task.id}")
            return oldItem.task.id == newItem.task.id
            //return false
        }

        override fun areContentsTheSame(
            oldItem: TaskListViewHolderData,
            newItem: TaskListViewHolderData
        ): Boolean {
            Log.d(TAG, "areContentsTheSame")
            Log.d(TAG, "old : $oldItem , newItem : $newItem")
            Log.d(TAG, "return ${oldItem.task.content == newItem.task.content}")
            return oldItem.task.content== newItem.task.content
        }

    }

}