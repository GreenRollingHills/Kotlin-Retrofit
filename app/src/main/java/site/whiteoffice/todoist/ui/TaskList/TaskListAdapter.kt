package site.whiteoffice.todoist.ui.TaskList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_list_view_holder.view.*
import site.whiteoffice.todoist.R

class TaskListAdapter (

    private val context: Context,
    private var adapterDataList: List<TaskListViewHolderData> = emptyList(),
    private var dataDelegate:DataDelegate

): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TaskType = 0

    }

    interface DataDelegate {
        fun presentAreYouSureAlert(taskID:String)
        fun closeTask(taskID: String)
        fun startSpinner()
        fun stopSpinner()
    }

    //----------------FamilyViewHolder | FamilyDataModel------------
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            val data = adapterDataList[position]
            println("data : $data")
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


    //--------onCreateViewHolder: inflate layout with view holder-------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return when (viewType) {
            TaskType -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.task_list_view_holder, parent, false)
                TaskViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
    //-----------onCreateViewHolder: bind view with data model---------
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //val data = adapterDataList[position]
        when (holder) {
            is TaskViewHolder -> holder.bind(position)

            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return adapterDataList[position].listViewType

    }

    override fun getItemCount(): Int {
        return adapterDataList.size
    }

    fun update (list:List<TaskListViewHolderData>) {
        this.adapterDataList = list
        notifyDataSetChanged()
    }

}