package david.itemtracker

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

@Dao
interface TrackerDao{

    @Query("SELECT name FROM TrackerEntity")
    fun getAllEntries(): List<String>

    @Query("DELETE FROM TrackerEntity")
    fun nukeTable()

    @Insert(onConflict = REPLACE)
    fun insert(character : TrackerEntity)

    @Delete
    fun delete(character : TrackerEntity)

}