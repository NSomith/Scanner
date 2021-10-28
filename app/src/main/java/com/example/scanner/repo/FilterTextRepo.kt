package com.example.scanner.repo

import com.example.scanner.db.database.AppDb
import com.example.scanner.db.entity.FilterTextModel

class FilterTextRepo(val db: AppDb) {
    private val dao = db.filteredTextModelDao
    fun getAllModels() = dao.getAllModels()

    fun getModelsByScanId(scanId: Int) = dao.getModelsByScanId(scanId)

    suspend fun insertModel(model: FilterTextModel) = dao.insertModel(model)
}