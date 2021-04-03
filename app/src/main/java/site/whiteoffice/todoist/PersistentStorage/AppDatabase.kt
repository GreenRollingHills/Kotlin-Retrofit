package site.whiteoffice.todoist.PersistentStorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import site.whiteoffice.todoist.Dao.NASADao
import site.whiteoffice.todoist.Dao.TodoistDao
import site.whiteoffice.todoist.DataClasses.*

@Database(entities = [Project::class, Task::class, PatentSummary::class], version = 8, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val todoistDao: TodoistDao
    abstract val nasaDao: NASADao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance =
                    INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "todoist_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}