package site.whiteoffice.todoist.ui.ImageDump

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.image_dump_card_view.view.*
import kotlinx.coroutines.launch
import site.whiteoffice.todoist.DataClasses.PatentSummary
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.ui.ProjectList.ProjectList
import site.whiteoffice.todoist.ui.ProjectList.ProjectListListAdapter

class ImageDumpListAdapter () : ListAdapter<PatentSummary, ImageDumpListAdapter.ImageDumpViewHolder> (DiffCallback()) {

    var selectedItem:Int? = null

    companion object {
        private val TAG = ImageDumpListAdapter::class.simpleName

    }

    inner class ImageDumpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            Log.d(TAG, "ImageDumpViewHolder onBind")
            val data = getItem(position)
            val iView = itemView.patentImage
            val textView = itemView.patentName

            Glide.with(itemView)
                .load(data.patentImageUrl)
                .placeholder(R.drawable.nasa_placeholder)
                .error(R.drawable.nasa_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(iView)

            textView.text = ImageDump.removeSpanText(data.patentName)

            itemView.toPatentSummaryButton.setOnClickListener {
                val action = ImageDumpDirections.actionImageDumpToPatentSummary(data)

                itemView.findNavController().navigate(action)
            }

            setColor(itemView.context, position)
        }

        fun setColor(context:Context, position:Int) {
            if(position == selectedItem) {
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
        Log.d(TAG, "onCreateViewHolder, viewType : $viewType")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_dump_card_view, parent, false)
        return ImageDumpViewHolder(view)
    }
    override fun onBindViewHolder(holder: ImageDumpViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        Log.d(TAG, "position : $position")

        holder.bind(position)

        holder.itemView.setOnClickListener {

            if (selectedItem == position) {
                selectedItem = null
            } else {
                selectedItem = position

            }
            notifyDataSetChanged()
        }

    }


    class DiffCallback : DiffUtil.ItemCallback<PatentSummary>() {

        override fun areItemsTheSame(oldItem: PatentSummary, newItem: PatentSummary): Boolean {
            return oldItem.patentCaseNumber == newItem.patentCaseNumber
        }

        override fun areContentsTheSame(
            oldItem: PatentSummary,
            newItem: PatentSummary
        ): Boolean {
            return oldItem.patentCaseNumber == newItem.patentCaseNumber
        }

    }

}