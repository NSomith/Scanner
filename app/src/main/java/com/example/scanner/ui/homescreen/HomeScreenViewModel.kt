package com.example.scanner.ui.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanner.datastroe.AppPreference
import com.example.scanner.db.entity.FilterTextModel
import com.example.scanner.db.entity.Scan
import com.example.scanner.repo.FilterTextRepo
import com.example.scanner.repo.ScanRepo
import com.example.scanner.utils.Resource
import com.example.scanner.utils.getCurrentDateTime
import com.example.scanner.utils.setState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    val pref: AppPreference,
    private val scanRepo: ScanRepo,
    private val filteredTextModelRepo: FilterTextRepo
):ViewModel() {

    private val _viewState = MutableStateFlow(HomeState())
    val viewState:StateFlow<HomeState> = _viewState

//    channel handling events not to trigger multiple times snackbar
    private val _events = Channel<HomeScreenEvents>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        isFirstSceen()
        getScan()
    }

    fun createScan(text: String,filteredTextScan:List<Pair<String,String>>) = viewModelScope.launch {
        if(text.isNotEmpty() or text.isNotBlank()){
            val scan = Scan(
                scanText = text,
                dateCreated = getCurrentDateTime(),
                dateModified = getCurrentDateTime(),
                scanTitle ="",
                isPinned = false
            )
            val result = scanRepo.insertScan(scan)
            val scanId = Integer.parseInt(result.toString())
            filteredTextScan.forEach {
                val modal = FilterTextModel(scanId = scanId,type = it.first,content = it.second)
                filteredTextModelRepo.insertModel(modal)
            }
            _events.send(HomeScreenEvents.ShowCurrentScanSaved(scanId))
        }else{
            _events.send(HomeScreenEvents.ShowScanEmpty)

        }
    }

    fun isFirstSceen()  = viewModelScope.launch {
        val hasSceen = pref.isFirstLaunch.first()
        if(!hasSceen){
            _events.send(HomeScreenEvents.ShowOnboarding)
            pref.firstLaunchComplete()
        }
    }

    fun showLoadingDialog() = viewModelScope.launch {
        _events.send(HomeScreenEvents.ShowLoadingDialog)
    }

    fun deleteScan(scan:Scan) = viewModelScope.launch {
        scanRepo.deleteScan(scan)
        _events.send(element = HomeScreenEvents.ShowUndoDeleteScan(scan))
    }

    fun insertScan(scan:Scan) = viewModelScope.launch {
        scanRepo.insertScan(scan)
    }

    fun getScan() = viewModelScope.launch {
        try {
            scanRepo.getAllScan().collect {
                Log.d("DEBUGn", "getScans: ${it.size}")
//                _viewState.value = HomeState().copy()
                _viewState.setState { copy(scanList = Resource.Success(it)) }
            }
        } catch (e: Exception) {
            Log.e("DEBUGn", "getScans: ", e)
            _viewState.setState { copy(scanList = Resource.Error(e)) }
        }
    }
}