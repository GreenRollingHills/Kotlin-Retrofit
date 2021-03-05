package site.whiteoffice.todoist.ui.ImageDump

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.image_dump_view_holder.view.*
import site.whiteoffice.todoist.R

class PatentSummaryAdapter (
    private val context: Context,
    private var adapterDataList: List<PatentSummaryViewHolderData> = emptyList()
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val PatentSummaryType = 0

    }

    //----------------FamilyViewHolder | FamilyDataModel------------
    inner class PatentSummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            val data = adapterDataList[position]



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return when (viewType) {
            PatentSummaryType -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.patent_summary_view_holder, parent, false)
                PatentSummaryViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
    //-----------onCreateViewHolder: bind view with data model---------
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = adapterDataList[position]
        when (holder) {
            is PatentSummaryViewHolder -> holder.bind(position)

            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return adapterDataList[position].type

    }

    override fun getItemCount(): Int {
        return adapterDataList.size
    }


}