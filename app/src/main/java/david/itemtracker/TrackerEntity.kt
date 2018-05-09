package david.itemtracker

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class TrackerEntity(
    @PrimaryKey()
    var itemName: String,
    var itemDescription: String,
    var itemType: String
)