package com.example.scanner.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.StringBuilder

inline fun <T> MutableStateFlow<T>.setState(action:T.()->T){
   this.value = this.value.action()
}
inline fun <T> Fragment.collectStateFlow(state: StateFlow<T>, crossinline render: ((T) -> Unit)) {
   viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
         state.collect { state ->
            render(state)
         }
      }
   }
}

inline fun <T> Fragment.collectFlow(state: Flow<T>, crossinline render: ((T) -> Unit)) {
   viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
         state.collect { state ->
            render(state)
         }
      }
   }
}

//fun build(action: StringBuilder.() -> Unit){
//    val s = StringBuilder().action()
//}

