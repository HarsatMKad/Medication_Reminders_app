package Model.Journal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal")
class Journal(
    @ColumnInfo(name = "cureTitle")val cureTitle: String,
    @ColumnInfo(name = "cureDosage")val dosage: Int,
    @ColumnInfo(name = "date")val date: String,
    @ColumnInfo(name = "isTaken")val isTaken: Boolean) {
    @PrimaryKey(autoGenerate = true) var id = 0
}

