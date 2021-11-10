package com.example.scanner.ui.detailscreen

sealed class DetailScanEvents {
    object ShowSoftwareKeyboardOnFirstLoad: DetailScanEvents()
    object ShowScanUpdated: DetailScanEvents()
    object ShowUnsavedChanges: DetailScanEvents()
    object NavigateUp: DetailScanEvents()
}