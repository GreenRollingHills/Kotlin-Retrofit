package site.whiteoffice.todoist.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import site.whiteoffice.todoist.DataClasses.Project
import site.whiteoffice.todoist.DataClasses.Task

@Dao
interface TodoistDao {

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun insertAll(vararg projects: Project)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProjects (vararg projects: Project)

    @Delete
    fun delete(project: Project)

    @Query("SELECT * FROM Project")
    fun getAllProjects(): LiveData<List<Project>>

    @Query ("DELETE FROM Project")
    suspend fun deleteAllProjects()

    @Query ("SELECT * FROM Task WHERE project_id == :projectID")
    fun getTasksFor(projectID:Long):LiveData<List<Task>>

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun insertAll(vararg tasks: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTasks(vararg tasks: Task)

    @Query ("DELETE FROM Task")
    suspend fun deleteAllTasks()

}