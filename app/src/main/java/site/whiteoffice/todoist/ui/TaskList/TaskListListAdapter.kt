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
import site.whiteoffice.todoist.DataClasses.Task
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.ui.ProjectList.ProjectListListAdapter

class TaskListListAdapter (
    private var dataDelegate: DataDelegate
) : ListAdapter<Task, TaskListListAdapter.TaskViewHolder> (DiffCallback()) {


    companion object {
        private val TAG = TaskListListAdapter::class.java.simpleName

    }

    interface DataDelegate {
        fun presentAreYouSureAlert(taskID:String)
        fun closeTask(taskID: String)
        fun startSpinner()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            val data = getItem(position)
            val textView = itemView.findViewById<TextView>(R.id.taskContent)
            textView.text = data.content

            itemView.taskCompletionCheckBox.setOnClickListener {
                if (itemView.taskCompletionCheckBox.isChecked) {
                    val id = data.id
                    if (id != null) {
                        dataDelegate.startSpinner()
                        dataDelegate.closeTask(id.toString())

                    }
                }
            }

            itemView.deleteTaskButton.setOnClickListener {
                dataDelegate.presentAreYouSureAlert(data.id.toString())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder  {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_list_view_holder, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        holder.bind(position)

    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {

        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            Log.d(TAG, "are items same")
            Log.d(TAG, "old : $oldItem , newItem : $newItem")
            Log.d(TAG, "return ${oldItem.id == newItem.id}")
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Task,
            newItem: Task
        ): Boolean {
            Log.d(TAG, "areContentsTheSame")
            Log.d(TAG, "old : $oldItem , newItem : $newItem")
            Log.d(TAG, "return ${oldItem.content == newItem.content}")
            return oldItem.content== newItem.content
        }

    }

}