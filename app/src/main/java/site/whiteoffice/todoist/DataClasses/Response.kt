package site.whiteoffice.todoist.DataClasses

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

data class RepoResult(val projects: List<Project>)

@Entity()
data class Project(
        @PrimaryKey val id: Long,
        val name: String,
        val comment_count: Int?,
        val order: Int?,
        val color: Int?,
        val shared: Boolean?,
        val sync_id: Int?,
        val favorite:Boolean,
        val inbox_project:Boolean) {


        override fun toString(): String {
                return "id : $id, name : $name, color : $color"
        }
}

@Entity()
data class Task(
        @PrimaryKey val id: Long?,
        val project_id: Long?,
        val section_id: Int?,
        val parent_id: Int?,
        val content: String,
        val comment_count: Int?,
        val order: Int?,
        val priority:Int?,
        val url:String?) {


        override fun toString(): String {
                return "id : $id, content : $content"
        }
}

@Parcelize
@Entity()
data class PatentSummary(
        val patentName : String,
        val patentImageUrl : String,
        val patentSummary : String,
        @PrimaryKey val patentCaseNumber:String
) : Parcelable {

}


data class PatentSummaryResults (
        val results: List<List<String>>?,

        val count: Int,
        val total: Int,
        val perpage: Int,
        val page: Int) {
}


data class PatentIDResults (
        @PrimaryKey(autoGenerate = true) val id: Long,
        val results:List<PatentID>
        ) {
}


data class PatentID (
        val id:String,
        val _id:String,
        val subtitle:String,
        val tech_desc:String,
        val publications:String,
        val cname:String,
        val cphone:String,
        val cemail:String,
        /*val application:List<String>,*/
        val trl:String,
        val type:String,
        val center:String,
        val client_record_id:String,
        val reference_number:String,
        val patent_number:String,
        val license_fee:String,
        val annual_royalty:String,
        val license_term:String,
        val evaluation_fee:String,
        val evaluation_lic_term:String
) {


}


data class AuthData (
        val access_token:String,
        val token_type:String
) {

}


