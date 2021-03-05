package site.whiteoffice.todoist.ui.ImageDump

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.image_dump_card_view.view.*
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.ui.ProjectList.ProjectList
import site.whiteoffice.todoist.ui.ProjectList.ProjectListListAdapter
import site.whiteoffice.todoist.ui.ProjectList.ProjectListViewHolderData

class ImageDumpListAdapter (
    private val context: Context
) : ListAdapter<ImageDumpViewHolderData, ImageDumpListAdapter.ImageDumpViewHolder> (DiffCallback()) {

    var selectedItem:Int? = null

    companion object {
        const val NASAImageType = 0
        private val TAG = ProjectList::class.simpleName


    }

    inner class ImageDumpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            Log.d(TAG, "ImageDumpViewHolder onBind")
            val data = getItem(position)
            val iView = itemView.patentImage
            val textView = itemView.patentName

            Glide.with(itemView)
                //.load(data.patentImageUrl)
                .load(data.patentSummary.patentImageUrl)
                .placeholder(R.drawable.nasa_placeholder)
                .error(site.whiteoffice.todoist.R.drawable.nasa_placeholder)

                //.skipMemoryCache(true)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                //.transform(CircleCrop())
                .into(iView)

            //textView.text = ImageDump.removeSpanText(data.patentName)
            textView.text = ImageDump.removeSpanText(data.patentSummary.patentName)

            itemView.toPatentSummaryButton.setOnClickListener {
                //val action = ImageDumpDirections.actionImageDumpToPatentSummary(data.patentSummary, data.patentCaseNumber)
                val action = ImageDumpDirections.actionImageDumpToPatentSummary(data)

                itemView.findNavController().navigate(action)
            }

            setColor(position)
        }

        fun setColor(position:Int) {
            if(position == selectedItem) {
                //itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200))
                itemView.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.neonYellow))

            } else {
                val typedValue = TypedValue()
                val theme = context.theme

                theme.resolveAttribute(R.attr.cardBackgroundColor, typedValue, true)
                @ColorInt val color = typedValue.data
                itemView.cardView.setCardBackgroundColor(color)

            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageDumpViewHolder  {
        return when (viewType) {
            NASAImageType -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.image_dump_card_view, parent, false)
                //.inflate(R.layout.dummy_card, parent, false)
                ImageDumpViewHolder(view)

            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
    //-----------onCreateViewHolder: bind view with data model---------
    override fun onBindViewHolder(holder: ImageDumpViewHolder, position: Int) {
        //val data = adapterDataList[position]
        when (holder) {
            is ImageDumpViewHolder -> holder.bind(position)

            else -> throw IllegalArgumentException()
        }

        holder.itemView.setOnClickListener {

            if (selectedItem == position) {
                selectedItem = null
            } else {
                selectedItem = position

            }
            notifyDataSetChanged()
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<ImageDumpViewHolderData>() {

        override fun areItemsTheSame(oldItem: ImageDumpViewHolderData, newItem: ImageDumpViewHolderData): Boolean {
            //return oldItem?.id == newItem?.id
            Log.d(TAG, "are items same")
            Log.d(TAG, "old : $oldItem , newItem : $newItem")
            Log.d(TAG, "return ${oldItem.patentSummary.patentCaseNumber == newItem.patentSummary.patentCaseNumber}")
            return oldItem.patentSummary.patentCaseNumber == newItem.patentSummary.patentCaseNumber
            //return false
        }

        override fun areContentsTheSame(
            oldItem: ImageDumpViewHolderData,
            newItem: ImageDumpViewHolderData
        ): Boolean {
            Log.d(TAG, "areContentsTheSame")
            Log.d(TAG, "old : $oldItem , newItem : $newItem")
            Log.d(TAG, "return ${oldItem.patentSummary.patentCaseNumber == newItem.patentSummary.patentCaseNumber}")
            return oldItem.patentSummary.patentCaseNumber == newItem.patentSummary.patentCaseNumber
        }

    }

}