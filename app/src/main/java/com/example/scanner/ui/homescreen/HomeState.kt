package com.example.scanner.ui.homescreen

import com.example.scanner.db.entity.Scan
import com.example.scanner.utils.Resource

data class HomeState(
    private val scanList:Resource<List<Scan>> = Resource.Loading()
){
    val itemCount = scanList.data?.size?:0
    val isEmpty  = scanList.data.isNullOrEmpty()
    val isPinned = scanList.data?.filter { it.isPinned }?: emptyList()
    val otherScan = scanList.data?.filter { !it.isPinned }?: emptyList()
}
