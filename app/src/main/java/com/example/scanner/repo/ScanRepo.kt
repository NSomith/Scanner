package com.example.scanner.repo

import com.example.scanner.db.database.AppDb
import com.example.scanner.db.entity.Scan
import com.example.scanner.utils.Resource
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class ScanRepo(val db:AppDb) {
    private val dao = db.scanDao
    suspend fun insertScan(scan: Scan) = dao.insertScan(scan)
    suspend fun deleteScan(scan: Scan) = dao.deleteScan(scan)
    suspend fun getAllScan() = dao.getAllScans()
    suspend fun updateScan(scan: Scan) = dao.updateScan(scan)

    fun getScanById(id:Int) = flow<Resource<Scan>> {
        emit(Resource.Loading())
        try {
            val scan = dao.getScanById(id)
            emit(Resource.Success(scan))
        }catch (e:Exception){
            emit(Resource.Error(e))
        }
    }

}