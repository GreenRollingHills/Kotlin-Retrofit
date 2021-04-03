package site.whiteoffice.todoist.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import site.whiteoffice.todoist.DataClasses.PatentIDResults
import site.whiteoffice.todoist.DataClasses.PatentSummary
import site.whiteoffice.todoist.DataClasses.PatentSummaryResults

@Dao
interface NASADao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPatentSummaries (vararg patentSummary: PatentSummary)

    @Query("SELECT * FROM PatentSummary")
    fun getAllPatentSummaries(): LiveData<List<PatentSummary>>

    @Query ("DELETE FROM PatentSummary")
    suspend fun deleteAllPatentSummaries()

}