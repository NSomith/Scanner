package com.example.scanner.ui.detailscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanner.repo.FilterTextRepo
import com.example.scanner.repo.ScanRepo
import com.example.scanner.utils.Resource
import com.example.scanner.utils.getCurrentDateTime
import com.example.scanner.utils.setState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailScanViewModel(
    val savedStateHandle: SavedStateHandle,
    val scanrepo:ScanRepo,
    val filterTextRepo: FilterTextRepo
):ViewModel() {
    val scanId = savedStateHandle.get<Int>("scan_id") ?: 0
    private val isJustCreated = savedStateHandle.get<Int>("is_created") ?: 0

    private val _viewState = MutableStateFlow(DetailScanState())
    val viewState: StateFlow<DetailScanState> = _viewState

    private val _events = Channel<DetailScanEvents>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        initializeScan()
    }
    fun scanUtteranceId(): String {
        return _viewState.value.scan.data?.scanId.toString()
    }
    fun deleteScan() = viewModelScope.launch {
        val current = _viewState.value.scan.data
        current?.let { scanrepo.deleteScan(it) }
    }

    fun onNavigateUp(title: String, content: String) {
        _viewState.value.scan.data?.let {
            if (it.scanTitle != title || it.scanText != content)
                viewModelScope.launch {
                    _events.send(DetailScanEvents.ShowUnsavedChanges)
                }
            else
                viewModelScope.launch {
                    _events.send(DetailScanEvents.NavigateUp)
                }
        }
    }


    fun updateScan(title: String, content: String) {
        viewModelScope.launch {
            _viewState.value.scan.data?.let { scan ->
                val updated = scan.copy(
                    scanTitle = title,
                    scanText = content,
                    dateModified = getCurrentDateTime()
                )
                scanrepo.updateScan(updated)
                _events.send(DetailScanEvents.ShowScanUpdated)
                initializeScan(false)
                return@launch
            }
            //maybe send event if something fails?
        }
    }

    fun updateScanPinned() = viewModelScope.launch {
        _viewState.value.scan.data?.let {
            val updated = it.copy(isPinned = !it.isPinned)
            scanrepo.updateScan(updated)
            initializeScan(false)
        }
    }

    private fun initializeScan(showKeyboard: Boolean = true) = viewModelScope.launch {
        combine(
            scanrepo.getScanById(scanId),
            filterTextRepo.getModelsByScanId(scanId)
        ) { scan, models ->
            _viewState.setState {
                copy(scan = scan, filteredTextModels = Resource.Success(models))
            }

        }.collect {
            delay(100)
            if (isJustCreated == 1 && showKeyboard) _events.send(DetailScanEvents.ShowSoftwareKeyboardOnFirstLoad)
        }
    }


}