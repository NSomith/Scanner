package com.example.scanner.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.scanner.db.entity.FilterTextModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterTextModelDao {
    @Insert
    suspend fun insertModel(model: FilterTextModel)

    @Query("SELECT * FROM FILTERED_TEXT_MODEL")
    fun getAllModels(): Flow<List<FilterTextModel>>

    @Query("SELECT * FROM FILTERED_TEXT_MODEL WHERE scan_id = :scanId")
    fun getModelsByScanId(scanId: Int): Flow<List<FilterTextModel>>

}