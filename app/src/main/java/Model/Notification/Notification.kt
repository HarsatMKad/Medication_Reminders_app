package com.example.medication_reminders_app.data.Notification

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.medication_reminders_app.data.Cure.Cure

@Entity(tableName = "notification",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Cure::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("cureId"),
            onDelete = CASCADE
        )
    )
)
class Notification(
    @ColumnInfo(name = "header")val header: String,
    @ColumnInfo(name = "description")val description: String,
    @ColumnInfo(name = "shortDescription")val shortDescription: String,
    @ColumnInfo(name = "hours") val hours: Int,
    @ColumnInfo(name = "minutes") var minutes: Int,
    @ColumnInfo(name = "days")var days: Int,
    @ColumnInfo(name = "period")val period: Long,
    @ColumnInfo(name = "cureId")val cureId: Long) {
        @PrimaryKey(autoGenerate = true) var id = 0
}

class NotificationWithCure(
    @Embedded val notification: Notification,
    @Relation(parentColumn = "cureId", entityColumn = "id") val cure: Cure
)