package com.example.scanner.db.dao

import androidx.room.*
import com.example.scanner.db.entity.Scan
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {

    @Query("select * from scan order by date_created desc")
    fun getAllScans():Flow<List<Scan>>
    @Insert
    suspend fun insertScan(scan: Scan):Long

    @Delete
    suspend fun deleteScan(scan: Scan)
    @Update
    suspend fun updateScan(scan: Scan)

    @Query("select * from scan where scan_id=:id")
    suspend fun getScanById(id:Int):Scan

}