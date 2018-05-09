package david.itemtracker

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

@Dao
interface TrackerDao{

    @Query("SELECT itemName FROM TrackerEntity")
    fun getAllNames(): List<String>

    @Query("SELECT itemType FROM TrackerEntity")
    fun getTypes(): List<String>

    @Query("Select itemName,itemType FROM TrackerEntity")
    fun getNameTypePair(): List<itemNameType>

    @Query("Select * From TrackerEntity ORDER BY itemName")
    fun getEntities(): List<TrackerEntity>

    @Query("DELETE FROM TrackerEntity")
    fun nukeTable()

    @Insert(onConflict = REPLACE)
    fun insert(character : TrackerEntity)

    @Delete
    fun delete(character : TrackerEntity)

}