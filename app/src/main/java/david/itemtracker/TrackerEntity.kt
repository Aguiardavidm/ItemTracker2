package david.itemtracker

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class TrackerEntity(
    @PrimaryKey()
    val name: String
)