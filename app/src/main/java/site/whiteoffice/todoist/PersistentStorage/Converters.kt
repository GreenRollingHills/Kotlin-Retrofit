package site.whiteoffice.todoist.PersistentStorage

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import site.whiteoffice.todoist.DataClasses.PatentID

class Converters {

    @TypeConverter
    fun fromPatentID(patentID: PatentID) = Gson().toJson(patentID)

    @TypeConverter
    fun toPatentID(s: String): PatentID =
        Gson().fromJson(s, PatentID::class.java)


    @TypeConverter
    fun fromPatentIDListToJson(list:List<PatentID>):String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromJsonToPatenIDList(string:String):List<PatentID> {
        return Gson().fromJson(string, Array<PatentID>::class.java).toList()
    }

}