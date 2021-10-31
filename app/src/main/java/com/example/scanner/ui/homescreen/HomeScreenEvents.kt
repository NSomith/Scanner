package com.example.scanner.ui.homescreen

import com.example.scanner.db.entity.Scan

sealed class HomeScreenEvents{
    object ShowLoadingDialog: HomeScreenEvents()
    data class ShowCurrentScanSaved(val id: Int): HomeScreenEvents()
    object ShowScanEmpty: HomeScreenEvents()
    data class ShowUndoDeleteScan(val scan: Scan): HomeScreenEvents()
    object ShowOnboarding: HomeScreenEvents()
}
