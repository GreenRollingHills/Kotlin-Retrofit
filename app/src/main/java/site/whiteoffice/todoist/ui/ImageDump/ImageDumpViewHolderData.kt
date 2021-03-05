package site.whiteoffice.todoist.ui.ImageDump

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import site.whiteoffice.todoist.DataClasses.PatentSummary

/*@Parcelize
class ImageDumpViewHolderData (
    val type: Int = ImageDumpAdapter.NASAImageType,
    //val url:String? = "https://source.unsplash.com/random"
    //val patent: Patent
    ////val patentName : String,
    ////val patentImageUrl : String,
    ////val patentSummary : String,
    ////val patentCaseNumber:String
    val patentSummary:PatentSummary
):Parcelable {


}*/

@Parcelize
class ImageDumpViewHolderData (
    val type: Int = ImageDumpListAdapter.NASAImageType,
    val patentSummary:PatentSummary
):Parcelable {


}