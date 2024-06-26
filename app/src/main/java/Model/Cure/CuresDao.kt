package Model.Cure

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import Model.Cure.Cure

@Dao
interface CuresDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cure: Cure)

    @Delete
    suspend fun delete(cure: Cure)

    @Query("Select * from cure")
    fun getAllCures(): LiveData<List<Cure>>

    @Update
    suspend fun update(cure: Cure)
}