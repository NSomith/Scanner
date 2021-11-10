package com.example.scanner.ui.detailscreen


import com.example.scanner.db.entity.FilterTextModel
import com.example.scanner.db.entity.Scan
import com.example.scanner.utils.Resource

data class DetailScanState(
    val scan: Resource<Scan> = Resource.Loading(),
    val filteredTextModels: Resource<List<FilterTextModel>> = Resource.Loading()
) {

    val isError = scan is Resource.Error

}