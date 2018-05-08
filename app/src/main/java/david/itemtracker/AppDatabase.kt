package david.itemtracker

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(TrackerEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun TrackerDao() : TrackerDao
}